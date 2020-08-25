package br.com.legacy.viewControllers.activitites;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.ToolbarConfigurator;
import br.com.legacy.viewControllers.fragments.ScreenSlidePageFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by teruya on 03/10/2017.
 */

public class GalleryDetailActivity extends FragmentActivity{
    private static int NUM_PHOTOS;
    @BindView(br.com.R2.id.pager)
    ViewPager viewPager;
    @BindView(br.com.R2.id.gallery_detail_toolbar)
    View toolbar;

    private PagerAdapter pagerAdapter;
    private boolean isClubGallery = false;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this,"black");
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.black));

//      get the array list of the photoUrl`s
        Bundle b = getIntent().getExtras();
        ArrayList<String> photoUrls = new ArrayList<>();
        if(b != null) {
            photoUrls = b.getStringArrayList(Constants.PHOTO_URLS);
            position = b.getInt(Constants.POSITION);
            isClubGallery = b.getBoolean(Constants.IS_CLUB_GALLERY);
        }
        if (photoUrls != null){
            NUM_PHOTOS = photoUrls.size();
        }

        if(isClubGallery){
            ToolbarConfigurator.INSTANCE.configToolbar(toolbar,getResources().getString(R.string.photo_gallery),R.drawable.ic_arrow_back,0);
        } else {
            ToolbarConfigurator.INSTANCE.configToolbar(toolbar,getResources().getString(R.string.photo_detail),R.drawable.ic_arrow_back,0);
        }

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),photoUrls);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }

    @OnClick({br.com.R2.id.toolbar_left_icon})
    public void onBackButtonPressed(){
        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> photosList = new ArrayList<>();
        public ScreenSlidePagerAdapter(FragmentManager fm,ArrayList<String> photoUrls) {
            super(fm);
            this.photosList = photoUrls;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("photoUrl", photosList.get(position));
            ScreenSlidePageFragment frag = new ScreenSlidePageFragment();
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return NUM_PHOTOS;
        }
    }
}
