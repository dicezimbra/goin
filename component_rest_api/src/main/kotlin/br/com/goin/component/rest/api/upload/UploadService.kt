package br.com.goin.component.rest.api.upload

import android.graphics.Bitmap
import br.com.goin.base.BaseApplication
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.rest.api.helpers.ImageHelper
import io.reactivex.Observable
import me.shaohui.advancedluban.Luban
import okhttp3.ResponseBody
import java.io.File

open class UploadService {


    private val repository = UploadRepository()

    fun uploadImage(picture: Bitmap, fileName: String): Observable<ResponseBody> {

        return Luban.compress(BaseApplication.instance.context, ImageHelper.getFile(picture))
                .putGear(Luban.THIRD_GEAR)
                .asObservable()                             // generate Observable
                .flatMap {
                    val file = it
                    repository.createUrl(fileName).ioThread()
                            .flatMap {
                                repository.uploadFile(it.signedUrl, file).ioThread()
                            }
                }

    }


    fun uploadImage(file: File, fileName: String): Observable<ResponseBody> {
        return Luban.compress(BaseApplication.instance.context, file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()                             // generate Observable
                .flatMap {
                    val file = it
                    repository.createUrl(fileName).ioThread()
                            .flatMap {
                                repository.uploadFile(it.signedUrl, file).ioThread()
                            }
                }

    }

    fun uploadVideo(file: File, fileName: String): Observable<ResponseBody> {
        return repository.createUrl(fileName).ioThread()
                .flatMap {
                    repository.uploadFile(it.signedUrl, file).ioThread()
                }
    }
}


