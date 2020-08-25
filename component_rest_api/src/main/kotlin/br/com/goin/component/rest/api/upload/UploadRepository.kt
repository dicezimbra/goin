package br.com.goin.component.rest.api.upload


import br.com.goin.component.rest.api.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class UploadRepository {
    companion object {
        var service = RetrofitService(UploadApi::class.java, BuildConfig.BASE_UPLOAD_URL)
        var bucket = br.com.goin.base.BuildConfig.BUCKET_NAME
    }

    fun createUrl(fileName: String) : Observable<CreateUploadUrlResponse> {
        return service.apiService.getUrl(bucket, fileName)
    }

    fun uploadFile(uploadURl: String,  file : File) : Observable<ResponseBody>{
        val requestFile = RequestBody.create(
                null,
                file
        )
       return service.apiService.upload(uploadURl, requestFile)
    }

}