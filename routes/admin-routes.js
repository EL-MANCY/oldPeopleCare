const router = require('express').Router();
const {getUsersDetails, updateAdmin} = require('../controllers/admin-controller');
const {verifyAdmin, verifyToken} = require('../routes/verify-token')

router.get('/users', verifyToken, verifyAdmin, getUsersDetails);

router.post('/update/:userId', verifyToken, verifyAdmin, updateAdmin);

module.exports = router;