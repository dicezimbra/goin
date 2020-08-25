package br.com.legacy.viewControllers.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import br.com.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by teruya on 06/10/2017.
 */

public class ScreenSlidePageFragment extends Fragment {
    @BindView(br.com.R2.id.gallery_detail_image)
    ImageView photo;

    PhotoViewAttacher photoViewAttacher ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);
        String photoUrl = getArguments().getString("photoUrl");
        Glide.with(this.getActivity())
                .load(photoUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        photo.setImageDrawable(resource);
                        photoViewAttacher = new PhotoViewAttacher(photo);
                        photoViewAttacher.update();
                    }
                });

        return rootView;
    }

}
