const { Notification } = require("../models/notification-model");
const { User } = require("../models/user-model");
const fcmHndler = require('../utils/fcmHndler');

// from patient
exports.postRequestNotification = async (req, res, next) => {
  try {
    if (!req.body.email) {
      return res.status(400).send("email is missed !");
    }
    if (!req.body.role) {
      return res.status(400).send("role is missed !");
    }
    const user = await User.findOne({ email: req.body.email });
    if (!user) {
      return res.status(404).send("User not found !");
    }
    if(user._id == req.user.id){
      return res.status(404).send("You can't send request to yourself !");
    }
    if(user.registAs == 'patient'){
      return res.status(404).send("You can't send request to patient !");
    }
    const wasSent = await Notification.find({
      "sender.id": req.user.id,
      "receiver.id": user._id,
      type: "request",
    });
    if (wasSent.length > 0) {
      return res.status(404).send("You have already sent it before !");
    }
    if (user.circles.find((user) => user.id == req.user.id)) {
      return res.status(404).send("This user is one of your caregivers");
    }

    const notification = new Notification({
      type: "request",
      description: "sent you invitation request",
      sender: {
        id: req.user.id,
        name: req.user.name,
      },
      receiver: {
        id: user._id,
        name: user.fullname,
      },
      caregiverRole: req.body.role.toLowerCase(),
    });

    const postedNotification = await notification.save();
    fcmHndler.sendFcmNotify(user.fcmToken, 'Request', `${notification.sender.name} ${notification.description}`)

    res.status(200).send({
      success: true,
      notificationBody: postedNotification,
    });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

//from caregiver
exports.postAcceptNotifications = async (req, res, next) => {
  try {
    let requestNotification = await Notification.findById(
      req.params.notificationId
    );
    const notification = new Notification({
      type: "accept",
      description: "accepted your request",
      sender: {
        id: req.user.id,
        name: req.user.name,
      },
      receiver: {
        id: requestNotification.sender.id,
        name: requestNotification.sender.name,
      },
    });
    const postedNotification = await notification.save();

    let caregiver = await User.findById(req.user.id);
    let patient = await User.findById(requestNotification.sender.id);

    caregiver.circles.push({
      id: patient._id,
      role: requestNotification.caregiverRole,
    });
    await caregiver.save();

    patient.circles.push({
      id: caregiver._id,
      role: requestNotification.caregiverRole,
    });
    await patient.save();

    requestNotification.type = "request done";
    await requestNotification.save();

    res.status(200).send({
      success: true,
      notificationBody: postedNotification,
    });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

//from caregiver
exports.postRefuseNotifications = async (req, res, next) => {
  try {
    let requestNotification = await Notification.findById(
      req.params.notificationId
    );
    const notification = new Notification({
      type: "refuse",
      description: "refused your request",
      sender: {
        id: req.user.id,
        name: req.user.name,
      },
      receiver: {
        id: requestNotification.sender.id,
        name: requestNotification.sender.name,
      },
    });
    const refuseNotification = await notification.save();
    requestNotification.type = "request done";
    await requestNotification.save();
    res.status(200).send({
      success: true,
      notificationBody: refuseNotification,
    });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.postNotification = async (req, res, next) => {
  try {
    const patient = await User.findById(req.user.id);

    patient.circles.map(async (caregiver) => {
      const caregiverData = await User.findById(caregiver.id);
      const notification = new Notification({
        type: req.query.medicine ? "missing" : "alert",
        description: req.query.medicine
          ? `missed ${req.query.medicine}`
          : "may be in trouble !!",
        sender: {
          id: req.user.id,
          name: req.user.name,
        },
        receiver: {
          id: caregiverData._id,
          name: caregiverData.fullname,
        },
      });
      await notification.save();
    });

    res.status(200).send({
      success: true,
      message: req.query.medicine
        ? `A notification with missing ${req.query.medicine} was sent !!`
        : "Alert notification sent !!",
    });
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

// to all users (patient,caregiver)
exports.getAllNotifications = async (req, res, next) => {
  const notifications = await Notification.find({
    "receiver.id": req.user.id,
  }).populate("sender.id", "image");
  res.status(200).send(notifications);
};
