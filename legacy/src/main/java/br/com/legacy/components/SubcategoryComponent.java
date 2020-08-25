package br.com.legacy.components;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public class SubcategoryComponent extends BaseObservable {
    public String name;
    public String id;
    public SelectSubcategoryHandler selectComponentHandler;
    public ObservableBoolean isSelected = new ObservableBoolean(false);


    public ObservableField<String> font = new ObservableField<>();

    public interface SelectSubcategoryHandler {
        void onSelectComponent(SubcategoryComponent component);
    }

    public SubcategoryComponent(SelectSubcategoryHandler handler) {
        this.selectComponentHandler = handler;
    }

    public void onClickSubcategoryComponent() {
        if (selectComponentHandler != null) {
            selectComponentHandler.onSelectComponent(this);
        }
    }

}
