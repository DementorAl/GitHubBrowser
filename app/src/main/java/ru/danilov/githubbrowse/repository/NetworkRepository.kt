package ru.danilov.githubbrowse.repository

import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.danilov.githubbrowse.model.GitHubSearchResponse
import ru.danilov.githubbrowse.model.VkCustomResponse
import java.io.EOFException
import java.lang.reflect.Type

object NetworkRepository {

    private const val baseUrl = "https://api.vk.com/method/"
    private const val vkApiVer = 5.74
    val service: ApiProtocol

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L)
                        try {
                            nextResponseBodyConverter.convert(value)
                        } catch (e: EOFException) {
                        }
                    else Unit
        }
    }

    private val defaultGson by lazy {
        GsonBuilder().create()
    }

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(logging).build()
        val gson = defaultGson
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        service = retrofit.create(ApiProtocol::class.java)

    }

    interface ApiProtocol {
        @GET("users.get")
        fun getVKUserInfo(@Query("v") version: Double = vkApiVer,
                          @Query("access_token") accessToken: String = PreferenceRepository.token,
                          @Query("fields") fields: String = "photo_200,screen_name"): Single<VkCustomResponse>

        @GET("https://api.github.com/search/users")
        fun getPeopleList(@Query("q") q: String,
                          @Query("page") page: Int,
                          @Query("per_page") perPage: Int = 30,
                          @Query("rev") rev: String = "next"): Single<GitHubSearchResponse>
    }
}