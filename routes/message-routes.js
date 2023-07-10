const router = require('express').Router();
const { postMessage, deleteMessage, getAllMessages } = require('../controllers/message-controller');
const { postConversation } = require('../controllers/conversation-controller');
const { verifyToken } = require('../routes/verify-token')


router.post('/:receiverId', verifyToken, postConversation, postMessage);

router.get('/:receiverId', verifyToken, getAllMessages);

router.delete('/:messageId', verifyToken, deleteMessage);

module.exports=router;