const { Upcomings } = require('../models/upcoming-model');
const { User } = require('../models/user-model');

exports.addUpcoming =  async (req, res, next) => {
    try{
      const isThere = await Upcomings.find({user: req.user.id, createdAt: new Date().toISOString().split('T')[0]})
      if(isThere.length == 0){
        const date = new Date();
        var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        const today = days[date.getDay()];

        const user = await User.findById(req.user.id).populate('medicines');
        var medicines = user.medicines.map(medicine => {
          if(medicine.weakly.includes(today))
            return {
              medicine: medicine,
              state: "Waiting"
            }
          });
        medicines = medicines.filter(function (el) {
          return el != null;
        });
        let upcoming = new Upcomings({
          user: req.user.id,
          medicines: medicines
        })
        upcoming = await upcoming.save();
        res.status(200).json({
          success: true,
          message: "Created successfuly !"
        });
      } else {
        res.status(404).json({
          success: false,
          message: "Already created before !"
        })
      }
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
}

exports.getUpcoming =  async (req, res, next) => {
  try{
    const upcoming = await Upcomings.findOne({
      user: req.params.patientId, 
      createdAt: new Date().toISOString().split('T')[0]
    }).populate('medicines.medicine', '_id name image audio time')
    if(!upcoming)
      return res.status(404).send('User not found !');
    
    let result;
    if(!req.params.medicineId){
      result = upcoming.medicines.filter(medicine => {
        if(medicine.state == req.query.state){
          return medicine
        }
      })
    } else{
      if(req.query.state == 'Missed')
        req.query.state = 'Completed';
      else
        req.query.state = 'Missed'

      result = upcoming.medicines.filter(medicine => {
        if(medicine.state == req.query.state){
          return medicine
        }
      })
    }
    res.send(result)
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}

exports.getUpcomingCregiver = async (req, res, next) => {
  try {
    const caregiver = await User.findById(req.user.id);
    let upcomings = [];
    const promises = caregiver.circles.map(circle => {
      return Upcomings.find({
        user: circle.id,
        createdAt: new Date().toISOString().split('T')[0]
      }).populate("user", "_id image audio fullname").populate('medicines.medicine', '_id name image audio time')
    });

    Promise.all(promises).then(results => {
      results.forEach(result => {
        upcomings.push(result[0]);
      });
      res.json(upcomings);
    })
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}

exports.changeState =  async (req, res, next) => {
  try{
    const state = req.query.state;
    if( state != 'Completed' && state != 'Missed')
      return res.status(404).send("State should be Missed or Completed !");
    
    let upcoming = await Upcomings.findOne({user: req.params.patientId, createdAt: new Date().toISOString().split('T')[0]});
    if(!upcoming)
      return res.status(404).send('User not found !');
    const medicines = upcoming.medicines.map(medicine => {
      if(medicine.medicine != req.params.medicineId){
        return medicine
      } else {
        return {
          medicine : medicine.medicine,
          state: state
        }
      }
    })
    upcoming.medicines = medicines;

    await upcoming.save();

    next();
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}
