package br.com.legacy.viewModels;

import android.app.Application;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import br.com.databinding.ActivityNewFilterSearchBinding;
import br.com.goin.component.model.category.SubcategoriesModel;
import br.com.legacy.components.SubcategoryComponent;
import br.com.legacy.managers.SubcategoriesManager;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewModels.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchVM extends BaseViewModel {

    public String rangeBarMinValue;
    public String rangeBarMaxValue;
    public String categoryId;

    public SubcategoryComponent selectedSubcategory;
    public ObservableList<SubcategoryComponent> subcategoryComponents = new ObservableArrayList<>();

    public class ReturnFilters {
        public float priceRange;
        public String subcategoryId;
        public String searchText;
        public String categoryId;
    }

    public FilterSearchVM(Application application) {
        super(application);
    }

    public void init(HandlerError handlerError, ActivityNewFilterSearchBinding binding) {
        super.init(handlerError);
        getRangeBarFilter(binding);
    }

    public void getData(Intent intent, SubcategoriesManager subcategoriesManager) {
        this.categoryId = intent.getStringExtra(Constants.CATEGORY_ID);
       // subcategoriesManager.requestSubcategoriesList(categoryId);
    }

    public void setSubcategoryComponents(List<SubcategoriesModel> subcategoriesList, String subcategoryId) {
        subcategoryComponents.clear();
        for (SubcategoriesModel subcategoryModel : subcategoriesList) {
            SubcategoryComponent subcategoryComponent = new SubcategoryComponent(new SubcategoryComponent.SelectSubcategoryHandler() {
                @Override
                public void onSelectComponent(SubcategoryComponent component) {
                    if (selectedSubcategory != null) {
                        selectedSubcategory.isSelected.set(false);
                    }
                    if (component == selectedSubcategory) {
                        selectedSubcategory = null;
                    } else {
                        component.isSelected.set(true);
                        selectedSubcategory = component;
                    }
                }
            });

            subcategoryComponent.id = subcategoryModel.getSubcategoryId();
            subcategoryComponent.name = subcategoryModel.getName();
            subcategoryComponents.add(subcategoryComponent);

            if(subcategoryId != null && subcategoryId.equals(subcategoryModel.getSubcategoryId()) &&
                    selectedSubcategory == null){
                subcategoryComponent.isSelected.set(true);
                selectedSubcategory = subcategoryComponent;

            }else if(selectedSubcategory != null &&
                    subcategoryComponent.id.equals(selectedSubcategory.id)){
                subcategoryComponent.isSelected.set(true);
            }
        }
    }

    public void cleansubcategory(List<SubcategoriesModel> subcategoriesList){
        selectedSubcategory = null;
        setSubcategoryComponents(subcategoriesList, null);
    }


    public void getRangeBarFilter(ActivityNewFilterSearchBinding binding) {

    }

    public ReturnFilters getFilters(ActivityNewFilterSearchBinding binding) {
        try{
        final ReturnFilters filters = new ReturnFilters();

        filters.categoryId = categoryId;
        if (selectedSubcategory != null) {
            filters.subcategoryId = selectedSubcategory.id;
        }

//        if (binding.textViewValue.getText().toString().length() > 3) {
//
//            filters.priceRange = Float.valueOf(binding.textViewValue.getText().toString().substring(4));
//        } else {
//
//            filters.priceRange = 0.0f;
//        }

        return filters;}
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getFiltersList(ActivityNewFilterSearchBinding binding) {
        ArrayList<String> filtersList = new ArrayList<>();

        if (selectedSubcategory != null) {
            filtersList.add(selectedSubcategory.name);
        }

        return filtersList;
    }
}
