const router = require('express').Router();
const notificationController = require('../controllers/notification-controller');
const verifyToken = require('./verify-token').verifyToken;
const verifyPatient = require('./verify-token').verifyPatient;

router.post('/request',verifyToken,verifyPatient,notificationController.postRequestNotification);

router.post('/accept/:notificationId',verifyToken,notificationController.postAcceptNotifications);

router.post('/refuse/:notificationId',verifyToken,notificationController.postRefuseNotifications);

router.post('/',verifyToken,verifyPatient,notificationController.postNotification);

router.get('/',verifyToken,notificationController.getAllNotifications);

module.exports = router;