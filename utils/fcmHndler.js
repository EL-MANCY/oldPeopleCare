const FCM = require('fcm-node');
require('dotenv/config');

exports.sendFcmNotify = (receiverToken, title, body) => {
    let fcm = new FCM(process.env.FCM_SERVER_KEY);

    let message = {
        to: receiverToken,
        notification: {
            title: title,
            body: body
        }
    }
    fcm.send(message, (err, response) => {
        if(err){
            return false
        } else {
            return true
        }
    })
}