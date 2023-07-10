const jwt = require("jsonwebtoken");
const { User } = require('../models/user-model');
require("dotenv/config");

exports.verifyToken = (req, res, next) => {
  const authHeader = req.headers.token;
  if (authHeader) {
    jwt.verify(
      authHeader.split(" ")[1],
      process.env.JWT_SECRET,
      (err, user) => {
        if (err) {
          return res.status(403).send("Token is not valid !");
        }
        req.user = user;
        next()
      }
    );
  } else {
    return res.status(401).send("You are not authenticated !");
  }
};

exports.verifyPatient = (req,res,next) => {
  return req.user.registAs == 'patient'
          ? next()
          : res.status(403).send("You are not allowed to do that !");
}

exports.verifyUserAccess = async (req,res,next) => {
  const patient = await User.findOne({ _id : req.params.patientId });
  var caregiver = false;
  patient.circles.filter(circle => {
    if(circle.id == req.user.id && circle.role == 'editor')
      caregiver = true;
  })
  return caregiver || req.params.patientId == req.user.id
  ? next()
  : res.status(403).send("You are not allowed to do that !");
}

exports.verifyUserAsCaregiver = async (req,res,next) => {
  const patient = await User.findOne({ _id : req.params.patientId });
  var caregiver = false;
  patient.circles.filter(circle => {
    if(circle.id == req.user.id)
      caregiver = true;
  })
  return caregiver || req.params.patientId == req.user.id
  ? next()
  : res.status(403).send("You are not allowed to do that !");
}

exports.verifyAdmin = (req,res,next) => {
    return req.user.isAdmin
          ? next()
          : res.status(403).send("You are not allowed to do that !");
}
