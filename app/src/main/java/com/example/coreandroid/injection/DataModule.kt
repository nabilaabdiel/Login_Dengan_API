package com.example.coreandroid.injection

import android.content.Context
import androidx.databinding.ktx.BuildConfig
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.example.coreandroid.api.ApiService
import com.example.coreandroid.data.room.AppDatabase
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    fun provideSession(@ApplicationContext context: Context) = CoreSession(context)

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getDatabase(context)

    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    fun provideGson() = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    fun providesOkHttpClient(): OkHttpClient {

        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)

        if(BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }
        return okHttpClient.build()
    }

    //Note: add base url
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://api.app.reprime.id/v3/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }
}