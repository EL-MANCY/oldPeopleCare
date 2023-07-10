const mongoose = require("mongoose");
const joi = require("joi");
const { joiPasswordExtendCore } = require('joi-password');
const joiPassword = joi.extend(joiPasswordExtendCore);
const Schema = mongoose.Schema;
const Users = mongoose.Schema(
  {
    fullname: {
      type: String,
      required: true,
    },
    email: {
      type: String,
      required: true,
      unique: true,
    },
    phone: {
        type: String,
        required: true,
        unique: true
    },
    dateOfBirth: {
        type: Date,
        required: true
    },
    gender: {
        type: String,
        required : true,
    },
    registAs: {
        type: String,
        required : true,
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
    password: {
      type: String,
      required: true,
    },
    isAdmin: {
      type: Boolean,
      default: false,
    },
    medicines: [{ type: Schema.Types.ObjectId, ref: "Medicines" }],
    circles: [{
      _id: false,
      id : {
        type: Schema.Types.ObjectId,
        ref: "Users"
      },
      role: {
        type: String,
        enum:{
          values: [
              'viewer',
              'editor'
          ],
          message: 'role should be viewer or editor'
      }
      }
    }],
    fcmToken: String,
    resetToken: String,
    resetTokenExpiration: Date,
  },
  { timestamps: true }
);

// set default image avatar
Users.pre('save', async function (next) {
  if(!this.image.public_id){
    this.image.public_id = 'old-care/user1/maleCloudinary';
    this.image.url = 'http://res.cloudinary.com/dkx70o8tt/image/upload/v1676820727/old-care/user1/maleCloudinary.png'
  }
});


// module.exports = mongoose.model("User", Users);
const userSchema = mongoose.model("Users", Users);

function userValidation(user) {
  const schema = joi.object({
    fullname: joi.string().trim().max(255).required(),
    email: joi.string().trim().max(255).email().lowercase().required(),
    phone: joi.string().trim().length(11).pattern(/^[0-9]+$/).required(),
    dateOfBirth: joi.date().required(),
    fcmToken: joi.string().trim(),
    gender: joi.string().trim().lowercase().valid('male','female').required(),
    registAs: joi.string().trim().lowercase().valid('patient','caregiver').required(),
    password: joiPassword.string().minOfSpecialCharacters(1).minOfLowercase(1).minOfUppercase(1).minOfNumeric(1).trim().min(12).required()
  });

  return schema.validate(user);
}

function userUpdateValidation(user) {
  const schema = joi.object({
    fullname: joi.string().trim().max(255).required(),
    email: joi.string().trim().max(255).email().lowercase().required(),
    phone: joi.string().trim().length(11).pattern(/^[0-9]+$/).required(),
    dateOfBirth: joi.date().required(),
    gender: joi.string().trim().lowercase().valid('male','female').required(),
  });

  return schema.validate(user);
}

exports.validate = userValidation;
exports.UpdateValidate = userUpdateValidation;
exports.User = userSchema;