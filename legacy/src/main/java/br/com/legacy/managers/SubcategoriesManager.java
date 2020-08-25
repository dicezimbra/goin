package br.com.legacy.managers;

import android.app.Activity;

import br.com.goin.component.model.category.SubcategoriesModel;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.SubcategoriesList;

import java.util.List;

public class SubcategoriesManager extends BaseManager{

    public SubcategoriesRequestHandler subcategoriesRequestHandler;

    public interface  SubcategoriesRequestHandler {
        void onReceiveSubcategories(List<SubcategoriesModel> subcategories);
        void onFailureToReceiveSubcategories(String message);
    }

    public SubcategoriesManager(Activity activity) {
        super(activity);
        if (activity instanceof SubcategoriesRequestHandler)
            this.subcategoriesRequestHandler = (SubcategoriesRequestHandler) activity;
    }

    String lastClickedCategoryId = "";
    public void requestSubcategoriesList(final String categoryId) {
        lastClickedCategoryId = categoryId;
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                cacheServiceManager.getSubcategories(categoryId, new RequestResponseHandler<SubcategoriesList>() {
                    @Override
                    public void onResponse(SubcategoriesList response) {
                        if(response != null && subcategoriesRequestHandler != null)
                             subcategoriesRequestHandler.onReceiveSubcategories(response.subcategories);
                        onlineCall(categoryId);
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        onlineCall(categoryId);
                    }
                }, sharedPreferencesControl);
            }
        });


    }

    private void onlineCall(String categoryId) {
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getSubcategories(categoryId, SubcategoriesManager.this, new RequestResponseHandler<SubcategoriesList>() {
                    @Override
                    public void onResponse(SubcategoriesList response) {

                        if(response != null && response.subcategories != null && response.subcategories.size() > 0){
                            if(lastClickedCategoryId.equals(response.subcategories.get(0).getCategoryId())){
                                subcategoriesRequestHandler.onReceiveSubcategories(response.subcategories);
                            }
                        }
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        subcategoriesRequestHandler.onFailureToReceiveSubcategories(error.message);
                    }
                });
            }
        });
    }
}
