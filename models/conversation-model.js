const { Message } = require('./message-model')

const mongoose = require("mongoose");
const Conversation = new mongoose.Schema({
    participants: [
      {
        user: {
          type: mongoose.Schema.Types.ObjectId,
          ref: 'Users',
          required: true,
          
        },
        _id: false,  
        lastRead: {
          type: Date,
        },
      },
    ],
    messages: [{
          type: mongoose.Schema.Types.ObjectId,
          ref: 'Message',
        }]
  });

  exports.Conversation = mongoose.model("Conversation", Conversation);
