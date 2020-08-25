package br.com.legacy.components;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

public class SelectableCircleComponentFIlter extends BaseObservable {

    public String image ;
    public String imageWhite;

    public ObservableField<String> selectedImage = new ObservableField<>();

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> font = new ObservableField<>();
    public String id;
    public Boolean isEvent;
    public SelectableCircleHandler handler;
    public boolean isSelected = false;


    public interface SelectableCircleHandler {
        void onSelectCircle(SelectableCircleComponentFIlter component);
    }


    public SelectableCircleComponentFIlter(String image, String name, String id, String imageWhite, boolean isSelected) {
        this.image = image;
        this.name.set(name);
        this.id = id;
        this.imageWhite = imageWhite;
        if(!isSelected)
        this.selectedImage.set(imageWhite);
        else   this.selectedImage.set(image);

    }

    public void onClickComponentContainer(){

        if (handler != null){
            handler.onSelectCircle(this);
        }
    }


    public void updateImage(boolean isSelected){
        this.isSelected = isSelected;
        if(isSelected){
            selectedImage.set(image);
        }else{
            selectedImage.set(imageWhite);
        }

    }



}
