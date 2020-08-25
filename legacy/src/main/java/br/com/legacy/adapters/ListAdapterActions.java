package br.com.legacy.adapters;

import androidx.databinding.ObservableList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListAdapterActions {

    public enum ListAction {
        CHANGED,
        ITEM_RANGE_CHANGED,
        ITEM_RANGE_INSERTED,
        ITEM_RANGE_MOVED,
        ITEM_RANGE_REMOVED,
    }

    public Integer i, i1, i2;
    public ListAction action;

    public ListAdapterActions(Integer i, Integer i1, Integer i2, ListAction action) {
        this.i = i;
        this.i1 = i1;
        this.i2 = i2;
        this.action = action;
    }


    public static Disposable observeList(ObservableList list, final DataBindingAdapter adapter) {
        return ListAdapterActions.getListObservable(list)
                .doOnNext(new Consumer<ListAdapterActions>() {
                    @Override
                    public void accept(ListAdapterActions listAdapterActions) {
//                        switch (listAdapterActions.action) {
//                            case CHANGED:
//                                break;
//                            case ITEM_RANGE_REMOVED:
//                                adapter.notifyItemRangeRemoved(listAdapterActions.i, listAdapterActions.i1);
//                                break;
//                            case ITEM_RANGE_INSERTED:
//                                adapter.notifyItemRangeInserted(listAdapterActions.i, listAdapterActions.i1);
//                                break;
//                            case ITEM_RANGE_MOVED:
//                                adapter.notifyItemRangeChanged(listAdapterActions.i, listAdapterActions.i1);
//                                break;
//                        }
                        // TODO debug error
                        adapter.notifyDataSetChanged();
                    }
                }).subscribe();
    }

    public static Observable<ListAdapterActions> getListObservable(final ObservableList list) {
        return Observable.create(new ObservableOnSubscribe<ListAdapterActions>() {
            @Override
            public void subscribe(final ObservableEmitter<ListAdapterActions> emitter) {
                final ObservableList.OnListChangedCallback callback = new ObservableList.OnListChangedCallback<ObservableList>() {
                    @Override
                    public void onChanged(ObservableList observableList) {
                        emitter.onNext(new ListAdapterActions(null, null, null, ListAction.CHANGED));
                    }

                    @Override
                    public void onItemRangeChanged(ObservableList observableList, int i, int i1) {
                        emitter.onNext(new ListAdapterActions(i, i1, null, ListAction.ITEM_RANGE_CHANGED));
                    }

                    @Override
                    public void onItemRangeInserted(ObservableList observableList, int i, int i1) {
                        emitter.onNext(new ListAdapterActions(i, i1, null, ListAction.ITEM_RANGE_INSERTED));
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList observableList, int i, int i1, int i2) {
                        emitter.onNext(new ListAdapterActions(i, i1, i2, ListAction.ITEM_RANGE_MOVED));
                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList observableList, int i, int i1) {
                        emitter.onNext(new ListAdapterActions(i, i1, null, ListAction.ITEM_RANGE_REMOVED));
                    }
                };
                try {

                    list.addOnListChangedCallback(callback);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
