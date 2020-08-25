package br.com.legacy.viewModels.base;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import br.com.legacy.utils.rx.DisposableManager;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends AndroidViewModel {

    public interface HandlerError {
        void onError(String message);
    }

    public HandlerError handlerError;

    public boolean initialized = false;

    public DisposableManager disposableManager = new DisposableManager();

    public BaseViewModel(Application application) {
        super(application);
    }


    @Override
    protected void onCleared() {
        disposableManager.dispose();
        super.onCleared();
    }

    public void init(HandlerError handlerError) {
        if (initialized) return;
        initialized = true;
        this.handlerError = handlerError;
    }

    public void collectDisposable(Disposable disposable) {
        disposableManager.add(disposable);
    }

    public void handleError(Error error) {
        this.handlerError.onError(error.getMessage());
    }
}