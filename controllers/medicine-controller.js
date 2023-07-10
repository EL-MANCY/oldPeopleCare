const { User } = require("../models/user-model");
const { Medicine, validate } = require("../models/medicine-model");
const { Upcomings } = require('../models/upcoming-model');
const _ = require("lodash");

exports.postMedicines = async (req, res, next) => {
  try {
    const { error } = validate(req.body);
    if (error) {
      return res.status(400).send(error.details[0].message);
    }
    let user = await User.findById(req.params.patientId);
    if(user.registAs != 'patient' || !user){
      return res.status(404).send('No patients with that Id !');
    }
    let medicine = new Medicine(
      _.pick(req.body, [
        "name",
        "imgUrl",
        "recordUrl",
        "type",
        "description",
        "time",
        "weakly",
      ])
    );
    medicine.lastUpdatedUserID = req.user.id  ;
    const uploadedMedicine = await medicine.save();
    user.medicines.unshift(uploadedMedicine);
    await user.save();
    res.status(200).send(uploadedMedicine);
    next();
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.getAllUserMedicines = async (req, res, next) => {
  try {
    const user = await User.findById(req.params.patientId).populate("medicines");
    return res.status(200).send(user.medicines);
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.getSingleMedicine = async (req, res, next) => {
  try {
    const user = await User.findById(req.params.patientId).populate("medicines");
    let medicine;
    user.medicines.map((med) => {
      if (med._id == req.params.id) medicine = med;
    });
    if (medicine) {
      return res.status(200).send(medicine);
    }
    return res.status(404).send('Medicine not found !');
  } catch (err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
};

exports.updateMedicines = async (req,res,next) => {
    try {
        const { error } = validate(req.body);
        if (error) {
            return res.status(400).json(error.details[0].message);
        }
        const user = await User.findById(req.params.patientId).populate("medicines");
        let medicine;
        user.medicines.map((med) => {
        if (med._id == req.params.id) medicine = med;
        });
        if(!medicine){
            return res.status(404).send("Medicine not found !");
        }
        req.body.lastUpdatedUserID = req.user.id;
        const updatedMedicines = await Medicine.findByIdAndUpdate(req.params.id,req.body,{ new: true });
        res.status(200).send(updatedMedicines);
        next();
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
}

exports.deleteMedicines = async (req,res,next) => {
    try {
        let user = await User.findById(req.params.patientId).populate("medicines");
        let medicine;
        user.medicines.map((med) => {
          if (med._id == req.params.id) 
            medicine = med;
        });
        if(!medicine){
            return res.status(404).send('Medicine not found !');
        }
        
        let medicines = user.medicines.filter( value => {
            return value._id != req.params.id
        })
        user.medicines = medicines;
        await user.save()
        res.status(200).send({
          success: true,
          message: "Medicine deleted !"
        });
        next()
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
}

exports.afterChanges = async (req,res,next) => {
  try{
    const isThere = await Upcomings.find({user: req.params.patientId, createdAt: new Date().toISOString().split('T')[0]})
    
    const date = new Date();
    var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const today = days[date.getDay()];

    const user = await User.findById(req.params.patientId).populate('medicines');
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
    
    if(isThere.length == 0){
        // In case of add new medicine and need to create upcoming

        let upcoming = new Upcomings({
          user: req.user.id,
          medicines: medicines
        })
        await upcoming.save();
      } else {
        // Already created before and need to update it 
        let upcoming =  isThere[0];
        upcoming.medicines = medicines
        await upcoming.save();
      }
  } catch(err) {
    if (!err.statusCode) {
      err.statusCode = 500;
    }
    next(err);
  }
}