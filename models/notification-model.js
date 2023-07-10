const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const Notification = mongoose.Schema(
  {
    type: {
      type: String,
      required: true,
      enum: {
        values:[
            'request',
            'accept',
            'refuse',
            'request done',
            'missing',
            'alert'
        ],
        message: 'should be one of [request, accept, refuse, messing, alert]'
      }
    },
    read: {
      type: String,
      default: "false",
    },
    description: {
      type: String,
      required: true,
    },
    sender: {
        _id: false,
        id: {
            required: true,
            type: Schema.Types.ObjectId,
            ref: "Users"
        },
        name: {
            type: String,
            required: true,
        }
    },
    receiver: {
        _id: false,
        id: {
            required: true,
            type: Schema.Types.ObjectId,
            ref: "Users"
        },
        name: {
            type: String,
            required: true,
        }
    },
    caregiverRole: {
        type: String,
        enum:{
            values: [
                'viewer',
                'editor'
            ],
            message: 'role should be viewer or editor'
        }
    },
  },
  { timestamps: true }
);

exports.Notification = mongoose.model("Notifications", Notification);
