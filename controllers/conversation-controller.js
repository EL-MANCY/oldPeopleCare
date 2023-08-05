const { Conversation } = require('../models/conversation-model');
const { Message } = require('../models/message-model');

exports.postConversation = async (req, res, next) => {
    try{
      const conversation = await Conversation.findOne({
        "participants.user": {
          $all: [           
              req.params.receiverId ,
              req.user.id 
          ]
        }       
      });
      if (conversation) {
        req.conversation = conversation
      } else {
        let newconversation = new Conversation(
          { participants: [             
               {user: req.user.id , lastRead: Date.now()},    
               {user:req.params.receiverId, lastRead: null}
           ]           
           })
           newconversation = await newconversation.save();
           req.conversation = newconversation
      }
   
  next();
} catch (err) {
  if (!err.statusCode) {
    err.statusCode = 500;
  }
  next(err);
}
};

exports.getAllConversations = async (req, res, next) => {
  let conversation = await Conversation.find({
    "participants.user": {
      $all: [           
          req.user.id 
      ]
    }       
  }).populate('participants.user', 'image fullname').select('-__v')
  .populate({
    path: "messages",
    options: {
      sort: {timestamp: -1},
      limit: 1
    }
  });

  res.json(conversation);
}

exports.deleteConversation = async (req,res,next) => {
  try {
    const conversation = await Conversation.findById(req.params.conversationId)

    if(!conversation){
      return res.status(404).json({
        success: false,
        message: `No conversations with Id: ${req.params.conversationId}`
      })
    }

    const participantExists = conversation.participants.some(participant => {
      return participant.user.toString() === req.user.id;
    });

    if(!participantExists){
      return res.status(404).json({
        success: false,
        message: `You can't do that !`
      })
    }

    await Message.deleteMany({ _id: { $in: conversation.messages } });
    await Conversation.findByIdAndDelete(req.params.conversationId);

    return res.status(200).json({
      success: true,
      message: 'Conversation deleted !'
    })
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
}
};

