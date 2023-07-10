const cloudinary = require('../helpers/image-uploader');

exports.UploadImage = async (imagePath, folderPath) => {
    const uploadResult = await cloudinary.uploader.upload(imagePath, {
        folder: `old-care/${folderPath}`
    });
    return uploadResult;
}

exports.DeleteOneImage = async (public_id) => {
    if(public_id != 'old-care/user1/maleCloudinary')
        await cloudinary.uploader.destroy(public_id);
    return true
}