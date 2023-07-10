const router = require('express').Router();
const { deleteConversation, getAllConversations } = require('../controllers/conversation-controller');
const { verifyToken } = require('../routes/verify-token')


router.get('/', verifyToken, getAllConversations);

router.delete('/:conversationId', verifyToken, deleteConversation);

module.exports=router;