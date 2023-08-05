const cloudinary = require('../helpers/image-uploader');

exports.UploadImage = async (imagePath, folderPath, type) => {
    let uploadResult
    if(type == "audio"){
        uploadResult = await cloudinary.uploader.upload(imagePath, {
            folder: `old-care/${folderPath}`,
            resource_type: "video",
        });
    } else {
        uploadResult = await cloudinary.uploader.upload(imagePath, {
            folder: `old-care/${folderPath}`,
        });
    }
    
    return uploadResult;
}

exports.DeleteOneImage = async (public_id) => {
    if(public_id != 'old-care/user1/maleCloudinary')
        await cloudinary.uploader.destroy(public_id);
    return true
}