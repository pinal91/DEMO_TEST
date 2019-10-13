
package  secure.com.giphydemo.apimanager

import com.github.simonpercic.oklog3.OkLogInterceptor
import com.google.gson.GsonBuilder
import com.leza.lawyers.apimanager.WebServices

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import secure.com.giphydemo.helper.ArrayAdapterFactory
import secure.com.giphydemo.model.ResponseModel
import java.util.concurrent.TimeUnit

interface RestClient {
    companion object {
        fun create(): RestClient {


            val okLogInterceptor = OkLogInterceptor.builder().build()
            val okHttpBuilder = OkHttpClient.Builder()
            okHttpBuilder.connectTimeout(20, TimeUnit.SECONDS)
            okHttpBuilder.readTimeout(20, TimeUnit.SECONDS).build()
            okHttpBuilder.writeTimeout(20, TimeUnit.SECONDS)
            okHttpBuilder.addInterceptor(okLogInterceptor)
            val okHttpClient = okHttpBuilder.build()
            val gson = GsonBuilder().setLenient().registerTypeAdapterFactory(ArrayAdapterFactory()).create()
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(WebServices.DOMAIN)
                    .client(okHttpClient)
                    .build()
            return retrofit.create(RestClient::class.java)
        }
    }

    interface OnConnectionTimeoutListener {
        fun onConnectionTimeout()
    }


    @GET()
    fun videoList(@Url url: String): Observable<ResponseModel>



}
