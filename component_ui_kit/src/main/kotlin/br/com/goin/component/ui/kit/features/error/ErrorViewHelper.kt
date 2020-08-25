package br.com.goin.component.ui.kit.features.error

import android.content.Context
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorViewHelper {

    fun showErrorModal(context: Context, throwable: Throwable, retryListener: () -> Unit) {
        val dialog = ErrorDialog(context)

        when (throwable) {
            is SocketTimeoutException -> { dialog.configureInternetError() }
            is IOException -> { dialog.configureInternetError() }
            else -> { dialog.configureUnknownError() }
        }

        dialog.onClickRetry = {
            retryListener()
        }

        dialog.show()
    }

    fun showErrorModal(context: Context, id: String, retryListener: () -> Unit) {
        val dialog = ErrorDialog(context)

        dialog.configureMessageError(id)
        dialog.onClickRetry = {
            retryListener()
        }

        dialog.show()
    }

    fun handleErrorView(view: ErrorConstraintLayout, throwable: Throwable, retryListener: () -> Unit) {
        when (throwable) {
            is SocketTimeoutException -> { view.configureInternetError() }
            is IOException -> { view.configureInternetError() }
            else -> { view.configureUnknownError() }
        }

        view.onClickRetry = {
            view.hideError()
            retryListener()
        }
        view.showError()
    }

    fun handleErrorView(view: ErrorCoordinatorLayout, throwable: Throwable, retryListener: () -> Unit) {
        when (throwable) {
            is SocketTimeoutException -> { view.configureInternetError() }
            is IOException -> { view.configureInternetError() }
            else -> { view.configureUnknownError() }
        }

        view.onClickRetry = {
            view.hideError()
            retryListener()
        }
        view.showError()
    }
}

