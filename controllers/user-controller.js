const { User, UpdateValidate} = require("../models/user-model");
const imageHandler = require('../utils/imagesHandler');
const _ = require("lodash");

exports.getAllUsers = async (req, res, next) => {
  try {
    const users = await User.find(
      {},
      { medicines: 0, notifications: 0, circles: 0, password: 0, __v: 0 }
    );
    return res.status(200).send(users);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.getUser = async (req, res, next) => {
  try {
    const users = await User.findById(req.params.id, {
      medicines: 0,
      notifications: 0,
      password: 0,
      __v: 0,
    });

    if(!users){
      return res.status(404).send("User not found !")
    }

    const isInCircle = users.circles.filter(circle => circle.id == req.user.id).length > 0;
    const modifiedUser = {
      ...users.toObject(),
      isInCircle: isInCircle
    };
    delete modifiedUser.circles;
    res.status(200).send(modifiedUser)
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.searchForUser = async (req, res, next) => {
  try {
    const bodyData = req.body.user;
    let user;
    if(bodyData.match(/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/)){
      user = await User.find({email : bodyData}).select("image fullname ").limit(7);
    } else {
      user = await User.find({fullname: {$regex: bodyData, $options: 'i'}}).select("image fullname ").limit(7)
    }
    if(!user){
      return res.status(404).json("User not found !");
    }
    return res.status(200).json(user);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}

exports.getCircles = async (req, res, next) => {
  try {
    const users = await User.findById(req.user.id, {
      circles: 1,
      _id: 0,
    }).populate('circles.id', 'fullname image phone')
    return res.status(200).send(users.circles);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

// patient and caregiver
exports.deleteCircle = async (req, res, next) => {
  try {
    let user = await User.findById(req.user.id)
    let circle = await User.findById(req.params.userId);
    if(!circle){
      return res.status(404).send('User not found !');
    }
    const isFound = user.circles.filter(c => {
      if(c.id == req.params.userId)
        return c
    })
    if(isFound.length == 0){
      return res.status(404).send('User not is not in your circle !');
    }
    user.circles = user.circles.filter(c => {
      if(c.id != req.params.userId)
        return c
    })
    circle.circles = circle.circles.filter(c => {
      if(c.id != req.user.id)
        return c
    })
    await user.save();
    await circle.save();
    return res.json({
      success: true,
      message: "Circle deleted !"
    })
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}

// for patient only
exports.editRole = async (req, res, next) => {
  try {
    let patient = await User.findById(req.user.id).populate('circles.id');
    let caregiver = patient.circles.find(user => user.id._id == req.params.id);
    if(caregiver.role == req.query.role){
      return res.status(404).send(`${caregiver.id.fullname} is already ${req.query.role}`)
    }

    patient.circles.filter(user => {
      if(user.id._id == req.params.id){
        return user.role = req.query.role;
      }
      return user;
    })
    await patient.save();

    caregiver.id.circles.filter(user => {
      if(user.id == req.user.id){
        return user.role = req.query.role;
      }
      return user;
    })
    await caregiver.id.save()
    
    res.status(200).send({
      success: true,
      message: `${caregiver.id.fullname} become ${req.query.role}`
    })
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}


exports.updateUser = async (req, res, next) => {
  try{
    const { error } = UpdateValidate(req.body);
    if (error) {
      return res.status(400).send(error.details[0].message);
    }
    const user = await User.findById(req.user.id);
    if(req.files.image){
      await imageHandler.DeleteOneImage(user.image.public_id);
      const uploadedImage = await imageHandler.UploadImage(req.files.image[0].path, req.user.id, "image");
      req.body.image = {
        public_id: uploadedImage.public_id,
        url: uploadedImage.url
      }
    }

    const updatedUser = await User.findByIdAndUpdate(req.user.id, req.body, {new : true});

    res.send({
      success : true,
      updatedUser
    });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}
