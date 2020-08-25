package br.com.legacy.adapters.viewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.R;

public class CategoriesListViewHolder extends RecyclerView.ViewHolder{

    public ImageView imgKindOfCategories;
    public TextView txtKindOfCategories;

    public CategoriesListViewHolder(View itemView) {
        super(itemView);

        imgKindOfCategories = itemView.findViewById(R.id.img_card_kind_of);
        txtKindOfCategories = itemView.findViewById(R.id.txt_card_kind_of);
    }
}

