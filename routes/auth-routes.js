const router = require('express').Router();
const authController = require('../controllers/auth-controller');

router.post('/signup',authController.signup);

router.post('/login',authController.login);

router.post('/reset', authController.postReset);

router.post('/reset/token', authController.postCheckToken);

router.post('/new-password', authController.postResetNewPassword);

module.exports = router;