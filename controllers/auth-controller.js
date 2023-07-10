const { User, validate } = require("../models/user-model");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
require("dotenv/config");
const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
      user: 'sherieffool@gmail.com',
      pass: process.env.SENGRID_PASSWORD
  }
});
  
exports.signup = async (req, res, next) => {
  try {
    const { error } = validate(req.body);
    if (error) {
      return res.status(400).send(error.details[0].message);
    }
    const hashedPassword = await bcrypt.hash(req.body.password.trim(), 12);
    const user = new User({
      fullname: req.body.fullname.trim(),
      email: req.body.email.trim(),
      phone: req.body.phone.trim(),
      dateOfBirth: req.body.dateOfBirth,
      gender: req.body.gender.toLowerCase().trim(),
      registAs: req.body.registAs.toLowerCase().trim(),
      fcmToken: req.body.fcmToken.trim(),
      password: hashedPassword,
    });
    const upladedUser = await user.save();
    return res.status(200).send(upladedUser);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.login = async (req, res, next) => {
  try {
    let user;
    const emailOrPhone = req.body.emailOrPhone.trim();
    if (!emailOrPhone) {
      const error = new Error("Select input type Email OR Phone !");
      error.statusCode = 400;
      throw error;
    }
    emailOrPhone.toLowerCase() == "email"
      ? (user = await User.findOne({ email: req.body.email.trim() }))
      : (user = await User.findOne({ phone: req.body.phone.trim() }));
    if (!user) {
      const error = new Error("Wrong input value !");
      error.statusCode = 400;
      throw error;
    }
    const isEqual = await bcrypt.compare(req.body.password.trim(), user.password);
    if (!isEqual) {
      const error = new Error("Wrong input values !");
      error.statusCode = 400;
      throw error;
    }
    if(req.body.fcmToken != user.fcmToken){
      user.fcmToken = req.body.fcmToken;
      await user.save();
    }
    const token = jwt.sign(
      {
        id: user._id,
        name: user.fullname,
        registAs: user.registAs,
        isAdmin: user.isAdmin,
      },
      process.env.JWT_SECRET
    );
    const returnData = {
      token : token,
      registAs: user.registAs,
      id: user.id
    }
    return res.status(200).send(returnData);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.postReset = async (req, res, next) => {
  try {
    const user = await User.findOne({ email: req.body.email })
      if (!user) {
        return res.status(404).send("User not found !");
      }
      const token = parseInt(Math.random() * (999999 - 100000) + 100000);
      user.resetToken = token;
      user.resetTokenExpiration = Date.now() + 3600000;
      await user.save();
      transporter.sendMail({
        to: req.body.email,
        from: "sherieffool@gmail.com",
        subject: "Password reset",
        html: `
            <p>You requested a password reset</p>
            <p>Verification code : ${token}</p>
            `,
      }, ()=>{
        res.status(200).json({
          success: true,
          message: "Check your mail !"
        });
      });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.postCheckToken = async (req, res, next) => {
  try {
    const user = await User.findOne({resetToken : req.body.token, resetTokenExpiration: { $gt: Date.now() }})
    if(!user){
      return res.status(404).send("Invaled Token !");
    }
    return res.status(200).json({
      success: true,
      message: "Token is Ok !"
    })
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}

exports.postResetNewPassword = async (req, res, next) => {
    try {
      const user = await User.findOne({resetToken : req.body.token, resetTokenExpiration: { $gt: Date.now() }})
      if(!user){
        return res.status(404).send("Invaled Token !");
      }
      user.password = await bcrypt.hash(req.body.password, 12);
      user.resetToken = undefined;
      user.resetTokenExpiration = undefined;
      const updatedUser = await user.save();
      return res.status(200).send(updatedUser);
    } catch (err) {
      if (!err.statusCode) {
        err.statusCode = 500;
      }
      next(err);
    }
  }

