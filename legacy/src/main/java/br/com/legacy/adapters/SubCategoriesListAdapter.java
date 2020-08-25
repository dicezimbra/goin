package br.com.legacy.adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.legacy.adapters.viewHolders.CategoriesListViewHolder;
import br.com.legacy.models.SubcategoryModel;
import br.com.R;
import br.com.legacy.utils.TypefaceUtil;

public class SubCategoriesListAdapter extends RecyclerView.Adapter<CategoriesListViewHolder> {

    public OnClickListenerCard listenerCard;

    private static final String POP = "https://i.ytimg.com/vi/Jia_8RXlCDk/maxresdefault.jpg";
    private static final String ROCK = "https://i.ytimg.com/vi/ECiP1Z4u06k/maxresdefault.jpg";
    private static final String BALADA = "https://132771-383016-raikfcquaxqncofqfm.stackpathdns.com/wp-content/uploads/2017/05/villa-mix-balada-sertaneja.jpg";
    private static final String PAGODE_SAMBA = "https://www.obaoba.com.br/contentFiles/system/pictures/2011/9/240981/original/87bc49a1fd7a7f8f.jpg";

    private String pics[] = {POP, ROCK, BALADA, PAGODE_SAMBA};

    Activity activity;
    private List<SubcategoryModel> subcategoryModelList;

    public SubCategoriesListAdapter(List<SubcategoryModel> subcategoryModelList, Activity activity, OnClickListenerCard onClickListenerCard) {
        this.subcategoryModelList = subcategoryModelList;
        this.listenerCard = onClickListenerCard;
        this.activity = activity;
    }


    @NonNull
    @Override
    public CategoriesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategories_card, parent, false);
        return new CategoriesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesListViewHolder holder, final int position) {
        final SubcategoryModel model = subcategoryModelList.get(position);

        holder.txtKindOfCategories.setText(model.name);
        Glide.with(activity)
                .load(model.image)
                .apply(RequestOptions.errorOf(R.drawable.profile_placerholder))
                .apply(RequestOptions.overrideOf(120, 70))
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imgKindOfCategories);


        for (int i = 0; i < pics.length; i++) {
            model.image = pics[i];
            System.out.println("Pics: " + model.image);
        }

        holder.imgKindOfCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerCard.onClickImgCard(model.subcategoryId);
            }
        });

        //Applying font on adapters components
        TypefaceUtil.initilize(activity);
        TypefaceUtil.boldFont(holder.txtKindOfCategories);
    }


    @Override
    public int getItemCount() {
        return subcategoryModelList.size();
    }


    public interface OnClickListenerCard {
        void onClickImgCard(String subCategoryId);
    }

}
