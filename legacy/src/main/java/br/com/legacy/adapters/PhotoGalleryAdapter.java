package br.com.legacy.adapters;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import br.com.legacy.models.PhotoGalleryModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.databinding.ItemPhotoGalleryBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kalunga on 03/10/2017.
 */

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoGalleryViewHolder> {

    List<PhotoGalleryModel> gallery;
    Activity activity;
    ArrayList<String> photoUrls;

    public PhotoGalleryAdapter(List<PhotoGalleryModel> list, Activity activity) {
        this.gallery = list;
        this.activity = activity;
        photoUrls = new ArrayList<>();
        for (PhotoGalleryModel model : gallery){
            photoUrls.add(model.photoUrl);
        }
    }

    @Override
    public PhotoGalleryAdapter.PhotoGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPhotoGalleryBinding binding = ItemPhotoGalleryBinding.inflate(inflater, parent, false);
        return new PhotoGalleryAdapter.PhotoGalleryViewHolder(binding, activity);
    }

    @Override
    public void onBindViewHolder(final PhotoGalleryAdapter.PhotoGalleryViewHolder holder, final int position) {
        final PhotoGalleryModel galleryModel = gallery.get(position);


        holder.itemContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Coordinator.goToGalleryDetail(activity,position,photoUrls);
            }
        });
        holder.bind(galleryModel);

    }

    @Override
    public int getItemCount() {
        return this.gallery.size();
    }

    public static class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {

        private final ItemPhotoGalleryBinding binding;
        public RelativeLayout itemContainer;

        Activity activity;

        public PhotoGalleryViewHolder (ItemPhotoGalleryBinding binding, Activity activity) {
            super (binding.getRoot());
            this.activity = activity;
            this.itemContainer = binding.getRoot().findViewById(R.id.item_photo_gallery_container);
            this.binding = binding;
        }

        public void bind(PhotoGalleryModel item) {
            ButterKnife.bind(this, binding.getRoot());
            binding.setPhotoGallery(item);
            binding.executePendingBindings();

            Glide.with(activity)
                    .load(item.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_profile_placeholder)) // TODO colocar um placeholder
                    .into(binding.itemPhotoGalleryImage);
        }
    }
}
