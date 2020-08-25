package br.com.legacy.adapters.viewHolders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.R;

public class SubCategoriesViewHolder extends RecyclerView.ViewHolder {

    public TextView txtCategorySubCategory;
    public TextView txtEstablishmentRatingCategory;
    public TextView txtEstablishmentNameCategory;
    public TextView txtAddressEstablishmentCategory;
    public TextView txtDistanceEstablishmentCategory;
    public TextView txtPriceEstablishmentCategory;
    public TextView txtSaleEstablishmentCategory;
    public TextView ratingBarCategories;
    public ImageView starBarCategories;
    public ImageView imgEstablishmentCategory;
    public ConstraintLayout constraintEstablishmentInfo;
    public ImageView star;
    public SubCategoriesViewHolder(View itemView) {
        super(itemView);

        txtCategorySubCategory = itemView.findViewById(R.id.establishment_category_subcategory_txt);
        txtEstablishmentNameCategory = itemView.findViewById(R.id.establishment_name_txt);
      //  txtEstablishmentRatingCategory = itemView.findViewById(R.id.txt_rating_establishment_category);
      //  txtAddressEstablishmentCategory = itemView.findViewById(R.id.txt_establishment_address_category);
     //   txtDistanceEstablishmentCategory = itemView.findViewById(R.id.txt_distance_establishment_category);
     //   txtPriceEstablishmentCategory = itemView.findViewById(R.id.txt_price_establishment_category);
     //   txtSaleEstablishmentCategory = itemView.findViewById(R.id.txt_sale_establishment_category);
        ratingBarCategories = itemView.findViewById(R.id.ratingBarCategories);
        starBarCategories = itemView.findViewById(R.id.starCategories);
        imgEstablishmentCategory = itemView.findViewById(R.id.img_category_establishment);
     //   star = itemView.findViewById(R.id.star);
        constraintEstablishmentInfo = itemView.findViewById(R.id.constraint_establishment_info);
    }

}
