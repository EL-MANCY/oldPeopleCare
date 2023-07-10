const router = require('express').Router();
const userController = require('../controllers/user-controller');
const {verifyAdmin, verifyToken, verifyPatient} = require('../routes/verify-token')

router.get('/',verifyToken,verifyAdmin,userController.getAllUsers);

router.get('/:id',verifyToken,userController.getUser);

// search for users
router.post('/find/more',verifyToken,userController.searchForUser)

router.get('/user/circles',verifyToken,userController.getCircles);

router.delete('/user/circles/:userId',verifyToken,userController.deleteCircle);

router.put('/circles/editRole/:id', verifyToken, verifyPatient, userController.editRole);

router.put('/', verifyToken, userController.updateUser);

module.exports = router;