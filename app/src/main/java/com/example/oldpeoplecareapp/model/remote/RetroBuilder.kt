package com.example.oldpeoplecareapp.model.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit



object RetroBuilder {
    private const val BaseUrl: String = "https://old-care.onrender.com"

     var okHttp = OkHttpClient.Builder()
         .connectTimeout(30, TimeUnit.MINUTES) // connect timeout
         .writeTimeout(30, TimeUnit.MINUTES) // write timeout
         .readTimeout(30, TimeUnit.MINUTES) // read timeout
         .build()


    var gson = GsonBuilder()
        .setLenient()
        .create()

    val builder = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .client(okHttp)
        .addConverterFactory(ScalarsConverterFactory.create()) //important
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ServiceAPI::class.java)
}

//private val retrofit= builder.build()

//    fun <T> buildService(serviceType:Class<T>):T{
//        return retrofit.create(serviceType)
//    }
//////////////////////////////

//
//class RetroBuilder {
//    companion object{
//        private const val BaseUrl:String ="https://old-care.onrender.com/"
//
//        private val okHttp=OkHttpClient.Builder()
//
//
//        fun getInterceptor(): HttpLoggingInterceptor {
//            val loggingInterceptor =  HttpLoggingInterceptor()
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            return loggingInterceptor
//        }
//
//
//
//        fun getRetroBuilder():Retrofit{
//            return Retrofit.Builder()
//                .baseUrl(BaseUrl)
//                .apply { getInterceptor() }
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttp.build())
//                .build()
//
//
//        }
//
//    }
//}