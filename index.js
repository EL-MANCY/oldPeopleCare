const express = require("express");
const mongoose = require("mongoose");
const cors = require('cors');
const bodyParser = require('body-parser');
const multer = require('multer');
const socket = require('socket.io');
require('dotenv/config');

const app = express();

app.use(cors());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: false }));

// multer config to allow upload images and records (files)
const fileFilter = (req,file, cb) => {
  const allowedMimeTypes = ["image", "audio"];
  if (allowedMimeTypes.some(type => file.mimetype.startsWith(type))) {
    cb(null, true);
  } else {
    cb(new Error('Only images and records are allowed!'));
  }
};

app.use(
  multer({
    storage: multer.diskStorage({}),
    fileFilter: fileFilter
  }).fields([
    { name: 'image', maxCount: 1 },
    { name: 'audio', maxCount: 1 }
  ])
);

// old-care APIs
app.use('/auth',require('./routes/auth-routes'));
app.use('/medicine',require('./routes/medicine-routes'));
app.use('/notification',require('./routes/notification-routes'));
app.use('/user',require('./routes/user-routes'));
app.use('/upcoming',require('./routes/upcoming-routes'));
app.use('/message',require('./routes/message-routes'));
app.use('/conversation',require('./routes/conversation-routes'));

// Dashboard
app.use('/admin',require('./routes/admin-routes'));
app.use('/feedback',require('./routes/feedback-routes'));

// error middleware
app.use((error,req, res, next) => {
    let statusCode = 400;
    if(error.statusCode)
        statusCode = error.statusCode;
    res.status(statusCode).send(error.message);
    next();
})

// connect to database and server

mongoose
  .connect(process.env.DATABASE_LINK)
  .then(() => {
    console.log("connected to db")
  })
  .catch((err) => {
    console.log(err);
  });

  const server = app.listen(process.env.PORT, () => {
    console.log("Connected !");
  });

const io = socket(server, {
  cors: {
      origin: "*",
      credentials: true
  }
});

global.onlineUsers  = new Map()

io.on('connection', (socket) => {
    global.chatSocket = socket;
    socket.on('add-user', (userId) => {
        onlineUsers.set(userId, socket.id)
    })

    socket.on('send-msg', (data) => {
        const sendUserSocket = onlineUsers.get(data.to)
        if(sendUserSocket){
            socket.to(sendUserSocket).emit('msg-recieve', data.message)
        }
    })
})
