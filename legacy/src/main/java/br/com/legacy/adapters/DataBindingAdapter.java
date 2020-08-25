package br.com.legacy.adapters;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.AnyRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by williamwebb on 11/15/15.
 * <p>
 * RecycleView Adapter class for use with DataBinding.
 */
public class DataBindingAdapter<DataModel, ViewBinder extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnViewBindCallback<ViewBinder> onViewBindCallback;
    private final List<DataModel> data = new ArrayList<>();
    private final int layoutId;
    private final int variableId;
    public Disposable removeItemDisposable;
    public Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    /**
     * Constructor.
     *
     * @param data       data to populate the Adapter with
     * @param layoutId   layout used by the adapter
     * @param variableId variable id used to set DataBinding. Ex: BR.data
     */
    public DataBindingAdapter(@NonNull final List<DataModel> data, @LayoutRes int layoutId, @AnyRes int variableId, RecyclerView recyclerView, Context context) {
        this.data.addAll(data);
        this.layoutId = layoutId;
        this.variableId = variableId;
        if (recyclerView != null) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem) + 1) {
                        isLoading = true;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
        if (context != null) {
            this.context = context;
        }
    }

    /**
     * Constructor.
     *
     * @param layoutId   layout used by the adapter
     * @param variableId variable id used to set DataBinding. Ex: BR.data
     */
    public DataBindingAdapter(@LayoutRes int layoutId, @AnyRes int variableId, RecyclerView recyclerView, Context context) {
        this(new ArrayList<DataModel>(), layoutId, variableId, recyclerView, context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return DataBindingViewHolder.from(parent, layoutId, variableId);
        } else if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_loading,parent,false);
//            return new LoadingViewHolder(view);
        }
        return null;

    }

    public void setData(ObservableList<DataModel> data) {
        this.data.clear();
        this.data.addAll(data);
        if (isLoading) {
            isLoading = false;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataBindingViewHolder) {
            ((DataBindingViewHolder<DataModel, ViewBinder>) holder).bind(data.get(position));
            if (onViewBindCallback != null)
                onViewBindCallback.onViewBind(((DataBindingViewHolder<DataModel, ViewBinder>) holder).dataBinding);
        }
//        else if (holder instanceof LoadingViewHolder){
//            ((LoadingViewHolder)holder).progressBar.setIndeterminate(true);
//        }

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    /**
     * Callback used to customize onBind
     *
     * @param callback
     */
    public DataBindingAdapter setOnBindViewCallback(OnViewBindCallback<ViewBinder> callback) {
        this.onViewBindCallback = callback;
        return this;
    }

    public interface OnViewBindCallback<BinderType extends ViewDataBinding> {
        void onViewBind(BinderType type);
    }

    static class DataBindingViewHolder<DataModel, ViewBinder extends ViewDataBinding> extends RecyclerView.ViewHolder {

        private final ViewBinder dataBinding;
        private final int variableId;

        static <DataModel, ViewBinder extends ViewDataBinding> DataBindingViewHolder<DataModel, ViewBinder> from(ViewGroup parent, int layoutId, int variableId) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new DataBindingViewHolder<>(v, variableId);
        }

        DataBindingViewHolder(View itemView, int variableId) {
            super(itemView);
            this.variableId = variableId;

            dataBinding = DataBindingUtil.bind(itemView);
        }

        void bind(DataModel data) {
            dataBinding.setVariable(variableId, data);
            dataBinding.executePendingBindings();
        }
    }


    @SuppressWarnings("unchecked")
    @BindingAdapter({"binding_data", "binding_layout", "binding_variable"})
    public static void bindRecyclerView(RecyclerView view, ObservableList binding_data, int layoutId, String bindingVariableId) {
        final DataBindingAdapter adapter;
        if (view.getAdapter() != null) {
            ((DataBindingAdapter) view.getAdapter()).setData(binding_data);
            return;
        }
        adapter = createBindingAdapter(view, view.getContext(), layoutId, bindingVariableId);
        view.setAdapter(adapter);
        adapter.setData(binding_data);
        adapter.notifyDataSetChanged();
        final Disposable d = ListAdapterActions.observeList(binding_data, adapter);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                d.dispose();
            }
        });
    }

    private static DataBindingAdapter createBindingAdapter(RecyclerView recyclerView, Context context, int bindingLayout, String bindingVariable) {
        try {
            Class BR_CLASS = Class.forName("br.com.goin" + ".BR");
            Field BR = BR_CLASS.getField(bindingVariable);

            int bindingVariableId = BR.getInt(null);

            return new DataBindingAdapter(bindingLayout, bindingVariableId, recyclerView, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (data.size() != 0) {
//            return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//        } else {
//            return VIEW_TYPE_ITEM;
//        }
        return VIEW_TYPE_ITEM;

    }

    //	Lint test to verify variableId is a variableId, possibly that it exist in layoutId
//	public class EnumDetector extends Detector implements JavaScanner {
//		…
//	}

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


}
