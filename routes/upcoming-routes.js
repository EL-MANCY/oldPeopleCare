const router = require('express').Router();
const { addUpcoming, getUpcoming, changeState, getUpcomingCregiver} = require('../controllers/upcoming-controller');
const {verifyToken} = require('./verify-token')

router.post('/', verifyToken, addUpcoming);

// for patient
router.get('/:patientId', verifyToken, getUpcoming);

// for caregiver
router.get('/', verifyToken, getUpcomingCregiver);

router.put('/:patientId/:medicineId', verifyToken, changeState, getUpcoming);

module.exports = router;