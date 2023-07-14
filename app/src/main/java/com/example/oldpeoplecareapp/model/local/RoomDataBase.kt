package com.example.oldpeoplecareapp.model.local

import android.content.Context
import androidx.room.*
import com.example.oldpeoplecareapp.model.entity.*
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.UiModel.MedicineUiModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

private val DATABASE_NAME="oldCare"
@Database(entities = [AllMedicineResponseItem::class , Medicine::class, notificationData::class , Circles::class, MedicineUiModel::class,SingleUserResponse::class], version = 18, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OldCareDB: RoomDatabase() {
    abstract fun dataDao(): DataDao

    //singleton pattern
    companion object {
        @Volatile
        private var instance: OldCareDB? = null

        fun getInstance(context: Context): OldCareDB {
            return instance ?: synchronized(Any()) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        private fun buildDataBase(context: Context): OldCareDB {
            return Room.databaseBuilder(
                context.applicationContext, OldCareDB::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }

    }
}


class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun fromJson(json: String): MedicineX {
        return Gson().fromJson(json, MedicineX::class.java)
    }

    @TypeConverter
    fun toJson(medicineX: MedicineX): String {
        return Gson().toJson(medicineX)
    }




    ////////
    private val gson = Gson()

    @TypeConverter
    fun fromSender(sender: Sender): String {
        return gson.toJson(sender)
    }

    @TypeConverter
    fun toSender(senderString: String): Sender {
        val senderType = object : TypeToken<Sender>() {}.type
        return gson.fromJson(senderString, senderType)
    }

    // TypeConverter for IDnotify class
    @TypeConverter
    fun fromIDnotify(idNotify: IDnotify): String {
        return gson.toJson(idNotify)
    }

    @TypeConverter
    fun toIDnotify(idNotifyString: String): IDnotify {
        val idNotifyType = object : TypeToken<IDnotify>() {}.type
        return gson.fromJson(idNotifyString, idNotifyType)
    }

    // TypeConverter for imageNotify class
    @TypeConverter
    fun fromImageNotify(imageNotify: imageNotify): String {
        return gson.toJson(imageNotify)
    }

    @TypeConverter
    fun toImageNotify(imageNotifyString: String): imageNotify {
        val imageNotifyType = object : TypeToken<imageNotify>() {}.type
        return gson.fromJson(imageNotifyString, imageNotifyType)
    }

    // TypeConverter for Reciever class
    @TypeConverter
    fun fromReceiver(receiver: Reciever): String {
        return gson.toJson(receiver)
    }

    @TypeConverter
    fun toReceiver(receiverString: String): Reciever {
        val receiverType = object : TypeToken<Reciever>() {}.type
        return gson.fromJson(receiverString, receiverType)
    }

    @TypeConverter
    fun fromID(id: ID): String {
        return Gson().toJson(id)
    }

    // Convert JSON string to ID object
    @TypeConverter
    fun toID(json: String): ID {
        return Gson().fromJson(json, ID::class.java)
    }

    // Convert Image object to JSON string
    @TypeConverter
    fun fromImage(image: Image): String {
        return Gson().toJson(image)
    }

    // Convert JSON string to Image object
    @TypeConverter
    fun toImage(json: String): Image {
        return Gson().fromJson(json, Image::class.java)
    }

    // Convert Circles object to JSON string
    @TypeConverter
    fun fromCircles(circles: Circles): String {
        return Gson().toJson(circles)
    }

    // Convert JSON string to Circles object
    @TypeConverter
    fun toCircles(json: String): Circles {
        return Gson().fromJson(json, Circles::class.java)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromJson2(value: String): Audio {
        return Gson().fromJson(value, object : TypeToken<Audio>() {}.type)
    }

    @TypeConverter
    fun toJson(audio: Audio): String {
        return Gson().toJson(audio)
    }
}

