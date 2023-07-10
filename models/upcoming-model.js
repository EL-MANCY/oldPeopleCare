const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Upcomings = mongoose.Schema({
    user:{
        type: Schema.Types.ObjectId,
        ref: "Users"
    },
    createdAt: {
        type: String,
        default: new Date().toISOString().split('T')[0]
    },
    medicines:[{
        _id: false,
        medicine:{
            type: Schema.Types.ObjectId,
            ref: "Medicines"
        },
        state:{
            type: String,
            enum: {
                values:[
                    "Waiting",
                    "Completed",
                    "Missed"
                ],
                message: 'should be one of [Waiting, Completed, Missed]'
              }
        }
    }]
})

exports.Upcomings = mongoose.model("Upcomings", Upcomings);