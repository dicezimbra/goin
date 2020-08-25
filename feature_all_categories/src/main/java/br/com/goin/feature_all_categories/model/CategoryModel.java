package br.com.goin.feature_all_categories.model;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import java.io.Serializable;

/**
 * Created by rudieros on 8/21/17.
 */

public class CategoryModel implements Serializable {

    public Drawable image;
    public String name;
    public String id;
    public String photoUrl;
    public String categoryType;
    public CategoryDetails.Type type;
    public String imageWhite;

    public static String TYPE_BALADA = "Baladas e Festas";
    public static String TYPE_SHOWS = "Shows";
    public static String TYPE_TEATRO = "Teatro";

    public enum CategoryType {
        event, place
    }
    public Boolean isSelected = false;

    ColorMatrixColorFilter filter;

    public void setImage(Drawable newImage) {
        this.image = newImage;
        if (isSelected) {
            image.clearColorFilter();
        } else {
            setBW();
        }
    }

    public void selectCategory() {
        isSelected = !isSelected;
        if (isSelected && image != null) {
            image.clearColorFilter();
        } else if (image != null) {
            setBW();
        }
    }

    void setBW() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        image.setColorFilter(filter);
    }

    public boolean isEvent(){
        return !(type != null && type == CategoryDetails.Type.place);
    }
}
