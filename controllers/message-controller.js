const { Conversation } = require("../models/conversation-model");

const { Message } = require("../models/message-model");

exports.postMessage = async (req, res, next) => {
  try{
      let message = new Message({
        sender: req.user.id,
        receiver: req.params.receiverId,
        content: req.body.content.trim()
      });

      message = await message.save();
      const conversation = req.conversation
      const participantIndex = conversation.participants.findIndex(
        (participant) => participant.user.toString() === req.user.id
      );
      conversation.participants[participantIndex].lastRead = Date.now();
      conversation.messages.push(message);
      await conversation.save();
      res.json(message);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
}
};

exports.getAllMessages = async (req, res, next) => {
  const conversation = await Conversation.findOne({
    "participants.user": {
      $all: [           
          req.params.receiverId ,
          req.user.id 
      ]
    }       
  }).populate('messages')
  if(!conversation){
    return res.status(404).send(`No conversation between <${req.user.id}> and <${req.params.receiverId}>`)
  }
  res.json(conversation.messages);
};

exports.deleteMessage = async (req, res, next) => {

    let message = await Message.findById(req.params.messageId);

    if (message.sender != req.user.id) {
      return res.status(404).json({
        success: true,
        message: `You can't delete this message !`
      })
    }

    if (!message) {
      return res.status(404).json({
        success: true,
        message: 'No messages found !'
      })
    }

    await Message.findByIdAndDelete(req.params.messageId);

    await Conversation.updateOne({
        "participants.user": {
          $all: [           
              message.sender ,
              message.receiver
          ]
        } 
      },
      { $pull: { messages: message._id } }
    )

    res.status(200).json({
      success: true,
      message: 'Message deleted !'
    })
};



