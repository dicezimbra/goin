package br.com.legacy.utils.rx;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class DisposableManager {
    private CompositeDisposable compositeDisposable;

    public void add(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    public void dispose() {
        getCompositeDisposable().dispose();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    public DisposableManager() {}

    public static Action getDisposableAction(final DisposableManager disposable) {
        return new Action() {
            @Override
            public void run() {
                disposable.dispose();
            }
        };
    }
}
