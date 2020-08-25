package br.com.legacy.components;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import android.widget.ImageView;

import br.com.BR;

public class ToolbarComponent extends BaseObservable{
    @Bindable
    public Integer leftIconId;
    @Bindable
    public Integer rightIconId;
    @Bindable
    public Integer centerIconId;
    @Bindable
    public String title;
    @Bindable
    public Boolean isHidden;

    public ObservableField<String> font = new ObservableField<>();

    public ToolbarComponent() {
        super();
    }



    public static ToolbarComponent build(){
        return new ToolbarComponent();
    }

    public ToolbarComponent addLeftIcon(Integer leftIconId){
        this.leftIconId = leftIconId;
        notifyPropertyChanged(BR.leftIconId);
        return this;
    }

    public ToolbarComponent addCenterIcon(Integer centerIconId){
        this.centerIconId = centerIconId;
        notifyPropertyChanged(BR.centerIconId);
        return this;
    }

    public ToolbarComponent setTitle(String text){
        String titleCap = text;
        if (!text.isEmpty() || isStringFormattable(text)) {
            titleCap = text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        this.title = titleCap;
        notifyPropertyChanged(BR.title);
        return this;
    }

    public ToolbarComponent addRightIcon(Integer rightIconId){
        this.rightIconId = rightIconId;
        notifyPropertyChanged(BR.rightIconId);
        return this;
    }

    public void hide() {
        this.isHidden = true;
        notifyPropertyChanged(BR.isHidden);
    }

    public void configToolbar(Integer leftIconId, Integer rightIconId, String title){
        this.leftIconId = leftIconId;
        this.rightIconId = rightIconId;
        if (title != null){
            this.title = title.toUpperCase();
        }
        this.isHidden = false;
        notifyChange();
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);

    }

    public static boolean isStringFormattable(String s) {
        // s = MFDSMFPDSM -> return true
        // s = mkdfmsklfmsdklfsm -> return true
        // s = Amdnsdaiojdsaiodas -> return false
        if (!s.isEmpty()) {
            boolean isLowerCase = Character.isLowerCase(s.charAt(0));
            for (int i = 1; i < s.length(); i++) {
                if (isLowerCase != Character.isLowerCase(s.charAt(i)) && !(s.charAt(i) == ' ')) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
