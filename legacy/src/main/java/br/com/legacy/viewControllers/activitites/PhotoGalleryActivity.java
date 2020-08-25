package br.com.legacy.viewControllers.activitites;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import br.com.legacy.adapters.PhotoGalleryAdapter;
import br.com.legacy.managers.PhotoGalleryManager;
import br.com.legacy.models.PhotoGalleryModel;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.ToolbarConfigurator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoGalleryActivity extends AppCompatActivity implements PhotoGalleryManager.PhotoGalleryRequestHandler {

    @BindView(br.com.R2.id.photo_gallery_recycler_view)
    RecyclerView photoRecycler;
    @BindView(br.com.R2.id.gallery_toolbar)
    View toolbar;
    String clubId;
    PhotoGalleryAdapter adapter;
    PhotoGalleryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.bind(this);

        Coordinator.setStatusBarColor(this,null);
        ToolbarConfigurator.INSTANCE.configToolbar(toolbar,getResources().getString(R.string.photo_gallery),R.drawable.ic_arrow_back,0);

        this.clubId = getIntent().getStringExtra(Constants.CLUB_ID);

        manager = new PhotoGalleryManager(this);
        manager.requestPhotoGallery(this.clubId);
        manager.photoGalleryRequestHandler = this;
    }

    @Override
    public void onReceiveGallery(List<PhotoGalleryModel> photoGalleryModel) {

        adapter = new PhotoGalleryAdapter(photoGalleryModel, this);

        photoRecycler.setLayoutManager(new GridLayoutManager(PhotoGalleryActivity.this, 2));
        photoRecycler.setAdapter(adapter);
    }

    @Override
    public void onFailure(String error) {

    }

    @OnClick({br.com.R2.id.toolbar_left_icon})
    public void onBackButtonPressed(){
        finish();
    }

}
