const router = require('express').Router();
const {postFeedback , getAllFeedbacks, deleteFeedback} = require('../controllers/feedback-controller');
const {verifyAdmin, verifyToken} = require('../routes/verify-token')

router.post('/post', verifyToken, postFeedback);

router.get('/all', verifyToken, verifyAdmin, getAllFeedbacks);

router.delete('/delete/:feedbackId', verifyToken, verifyAdmin, deleteFeedback);

module.exports = router;