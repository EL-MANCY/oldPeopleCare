// (dashboard)

const { User } = require("../models/user-model")

exports.getUsersDetails = async (req, res, next) => {
    try {
        let users;

        if(req.query.user != 'admin'){
            users = await User.find({
                registAs : req.query.user, isAdmin: false 
            })
            .select('image fullname email phone dateOfBirth gender medicines circles').populate('medicines').populate('circles.id', '_id image fullname email phone dateOfBirth gender');    
        } else if(req.query.user == 'admin'){
            console.log('there')
            users = await User.find({
                isAdmin: true
            })
            .select('image fullname email phone dateOfBirth gender circles')
        }
        
        res.json(users);
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
  }
  
exports.updateAdmin = async (req, res, next) => {
    try{
        let user = await User.findById(req.params.userId);
        if(!user){
            return res.status(404).send('User not found !');
        }else if(req.query.state != 'true' && req.query.state != 'false'){
            return res.status(404).send('State should be true or false !');
        }

        if (req.query.state === 'true') req.query.state = true; else req.query.state = false;
        
        if(user.isAdmin != req.query.state){
            await User.findByIdAndUpdate(req.params.userId, {isAdmin: req.query.state}, {new : true})
            let message;
            if(req.query.state == true){
                message = `User with id : ${req.params.userId} become Admin !`
            }else{
                message = `User with id : ${req.params.userId} become not Admin !`
            }
            return res.status(200).json({
                success: true,
                message
            })
        }
        return res.status(200).json({
            success: false,
            message: `User with id : ${req.params.userId} is already with state ${req.query.state} !`
        })
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
}