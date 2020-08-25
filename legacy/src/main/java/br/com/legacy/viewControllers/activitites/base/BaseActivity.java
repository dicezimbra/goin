package br.com.legacy.viewControllers.activitites.base;


import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.components.ToolbarComponent;
import br.com.goin.component.analytics.adjust.TagManager;
import br.com.goin.component.analytics.adjust.TagManagerImpl;
import br.com.legacy.navigation.Coordinator;
import br.com.legacy.viewModels.base.BaseViewModel;

public class BaseActivity extends AppCompatActivity implements BaseViewModel.HandlerError {

    ViewDataBinding binding;
    protected BaseViewModel viewModel;
    public ToolbarComponent toolbar;
    public static final String PREF_NAME = "BasePref";
    protected TagManager tagManager = new TagManagerImpl();

    public BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Coordinator.setStatusBarColor(this, null);
    }

    protected ViewDataBinding setLayoutId(@LayoutRes int layoutId, int variableId) {
        try {
            this.binding = DataBindingUtil.setContentView(this, layoutId);
            this.binding.setVariable(variableId, this);
            return this.binding;
        }catch (Exception e){
            e.printStackTrace();
            return DataBindingUtil.setContentView(this, layoutId);
        }
    }

    protected BaseViewModel linkViewModel(Class vmClass) {
        viewModel = (BaseViewModel) ViewModelProviders.of(this).get(vmClass);
        return this.viewModel;
    }

    public void onClickToolbarLeftIcon() {
    }

    public void onClickToolbarRightIcon() {
    }

    public void onClickToolbarCenterIcon() {
    }

    @Override
    public void onError(String message) {
        DialogUtils.INSTANCE.show(this, message, new DialogUtils.DialogCallback() {
            @Override
            public void onClickOk() {

            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadImage(ImageView imageView, String url, Drawable resource) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.placeholderOf(resource))
                .apply(RequestOptions.errorOf(resource))
                .into(imageView);
    }

    public void setEmailCredentials(Activity activity, String userEmail) {

        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", userEmail);
        editor.apply();

    }

    public String getEmailCredentials(Activity activity) {

        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, 0);
        String email = sharedPref.getString("email", "");

        return email;

    }


    public void setPassCredentials(Activity activity, String pass) {

        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pass", pass);
        editor.apply();

    }

    public String getPassCredentials(Activity activity) {

        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, 0);
        String pass = sharedPref.getString("pass", "");

        return pass;

    }






}