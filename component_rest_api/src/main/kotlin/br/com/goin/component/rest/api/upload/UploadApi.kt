package br.com.goin.component.rest.api.upload

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface UploadApi {


    @GET("upload/getsignedurl/{bucket}")
    fun getUrl( @Path("bucket" ) bucket: String, @Query("file") file: String? ) : Observable<CreateUploadUrlResponse>


    @PUT
    fun upload(@Url url: String,  @Body file: RequestBody): Observable<ResponseBody>
}