const mongoose = require("mongoose");
const joi = require("joi");
const Schema = mongoose.Schema;

const Medicines = mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    image: {
        _id: false,
        public_id: {
          type: String,
        },
        url: {
          type: String,
        }
    },
    audio: {
        _id: false,
        public_id: {
          type: String,
        },
        url: {
          type: String,
        }
    },
    type: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    time: [{
        _id: false,
        type: String,
        required: true
    }],
    weakly: [{
        _id: false,
        type: String,
        required: true
    }],
    lastUpdatedUserID: {
        type: Schema.Types.ObjectId,
        required : true
    },
},{ timestamps: true });

const medicineSchema = mongoose.model("Medicines", Medicines);

function medicineValidation(medicine) {
    const schema = joi.object({
        name: joi.string().trim().max(255).required(),
        type: joi.string().trim().lowercase().valid('pills','injection','drink','other').required(),
        description: joi.string().trim().max(255).required(),
        // time: joi.array().items(
        //     joi.string().trim().regex(/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/).required(),
        // ),
        // weakly: joi.array().items(
        //     joi.string().trim().valid('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday').required(),
        // ),
        time: joi.any(),
        weakly: joi.any()
    });
  
    return schema.validate(medicine);
  }
  
  exports.validate = medicineValidation;
  exports.Medicine = medicineSchema;