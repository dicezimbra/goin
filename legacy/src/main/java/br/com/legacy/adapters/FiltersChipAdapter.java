package br.com.legacy.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by appsimples on 13/09/17.
 */

public class FiltersChipAdapter extends RecyclerView.Adapter<FiltersChipAdapter.ViewHolder> {

    private final int ITEM_TYPE_DEFAULT = 0;
    private final int ITEM_TYPE_INCREASED = 1;

    public List<String> items;
    public List<String> idItems;
    private OnRemoveListener onRemoveListener;

    private Context context;
    private int chipColor;
    private Boolean isClosable;

    public FiltersChipAdapter(OnRemoveListener onRemoveListener, int color) {
        this.onRemoveListener = onRemoveListener;
        this.chipColor = color;
    }

    public FiltersChipAdapter(List<String> items, OnRemoveListener onRemoveListener, int color) {
        this.items = items;
        this.onRemoveListener = onRemoveListener;
        this.chipColor = color;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_filter_chip, parent, false);
        if(isClosable != null && !isClosable){
            TextView chipClose = itemView.findViewById(R.id.chip_close);
            if (chipClose != null){
                chipClose.setVisibility(View.GONE);
            }
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        String item = items.get(position);
        if (item.startsWith("!")) {
            return ITEM_TYPE_INCREASED;
        }

        return ITEM_TYPE_DEFAULT;
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public void setClosable(Boolean closable) {
        isClosable = closable;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(br.com.R2.id.chip_text)
        TextView tvText;

        @BindView(br.com.R2.id.chip_close_view)
        LinearLayout ibClose;

        @BindView(br.com.R2.id.chip_close)
        TextView tvClose;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            GradientDrawable chipDrawable = (GradientDrawable) ((LinearLayout) tvText.getParent()).getBackground();
            chipDrawable.setStroke(2, chipColor);
            tvText.setTextColor(chipColor);
            tvClose.setTextColor(chipColor);

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(">>FILTERSCHIPADAPTER", "ibClose onClick");
                    int position = getAdapterPosition();
                    if (position != -1) {
                        Log.d(">>ITEMS_antes", items.toString());
                        if(onRemoveListener != null)
                        onRemoveListener.onItemRemoved(position);
                        Log.d(">>ITEMS_depois", items.toString());
                    }
                }
            });
        }

        void bindItem(String text) {
            tvText.setText(text);
        }
    }

    public interface OnRemoveListener {
        void onItemRemoved(int position);
    }
}
