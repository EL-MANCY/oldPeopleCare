const router = require('express').Router();
const {postMedicines, getAllUserMedicines, getSingleMedicine, updateMedicines, deleteMedicines, afterChanges} = require('../controllers/medicine-controller');

const {verifyToken, verifyUserAccess , verifyUserAsCaregiver} = require('./verify-token')


router.post('/:patientId',verifyToken,verifyUserAccess,postMedicines, afterChanges);

router.get('/:patientId',verifyToken,verifyUserAsCaregiver,getAllUserMedicines);

router.get('/:id/:patientId',verifyToken,verifyUserAsCaregiver,getSingleMedicine);

router.put('/:id/:patientId',verifyToken,verifyUserAccess,updateMedicines, afterChanges);

router.delete('/:id/:patientId',verifyToken,verifyUserAccess,deleteMedicines, afterChanges);

module.exports = router;