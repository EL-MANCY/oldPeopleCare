const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Feedback = mongoose.Schema({
    user:{
        type: Schema.Types.ObjectId,
        ref: "Users",
        required: true
    },
    feedback: {
        type: String,
        required: true
    },
    createdAt: {
        type: String,
        default: new Date().toISOString().split('T')[0]
    },
})

exports.Feedback = mongoose.model("Feedback", Feedback);