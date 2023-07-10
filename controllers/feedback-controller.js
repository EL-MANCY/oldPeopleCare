const { Feedback } = require('../models/feedback-model');

exports.postFeedback = async (req, res, next) => {
    try {
        if(!req.body.feedback || req.body.feedback == ''){
            return res.status(400).json({
                success: false,
                messgae: "Enter a feedback !"
            })
        }
        const feedback = new Feedback({
            user: req.user.id,
            feedback: req.body.feedback.trim()
        })
        await feedback.save();
        return res.status(200).json({
            success: true,
            messgae: "Your feedback saved successfully !"
        })
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
    }
}

exports.getAllFeedbacks = async (req, res, next) => {
    const feedbacks = await Feedback.find().select('-__v').populate('user', 'email');
    res.status(200).json(feedbacks);
}

exports.deleteFeedback = async (req, res, next) => {
    try{
        const feedback = await Feedback.findByIdAndRemove(req.params.feedbackId)

        if(!feedback){
            return res.status(404).json({
                success: false,
                messgae: 'Feedback not found !'
            })
        }

        return res.status(200).json({
            success: true,
            messgae: `Feedback with id: ${req.params.feedbackId} deleted successfully !`
        })
    } catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
    }
}
