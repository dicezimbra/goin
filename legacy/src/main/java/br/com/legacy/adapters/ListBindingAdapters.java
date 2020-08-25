package br.com.legacy.adapters;

import androidx.databinding.BindingAdapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.legacy.utils.TypefaceUtil;

public class ListBindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, Integer url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadImage(ImageView view, String url, Drawable resource) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadImage(ImageView view, Integer url, Drawable resource) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"setFont"})
    public static void setFont(TextView view, String param) {
        TypefaceUtil.mediumFont(view);
    }
}
