package com.example.selfiepal.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.selfiePal.R
import com.example.selfiepal.network.NetworkService
import com.example.selfiepal.preferences.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val token = Preferences(sharedPreferences).getToken()
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:3000/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideSocketClient(): Socket {
//        val option = IO.Options.builder()
//            .build()
//        return IO.socket("http://localhost:3000", option)
//    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNotificationBuilder(
        @ApplicationContext context: Context):NotificationCompat.Builder{
       return NotificationCompat.Builder(context,"Main Channel ID")
           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
           .setSmallIcon(R.drawable.ic_launcher_background)
    }


    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context):NotificationManagerCompat{
      val notificationManager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel("Main Channel ID","Main Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }





































}