package br.com.legacy.utils;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import br.com.R2;
import butterknife.BindView;

class IncludedToolbar {

    @BindView(R2.id.toolbar_left_icon)
    ImageView leftIcon;
    @BindView(R2.id.toolbar_title)
    TextView toolbarTitle;

    @Nullable
    @BindView(R2.id.toolbar_right_icon)
    ImageView rightIcon;

    @Nullable @BindView(R2.id.toolbar_right_second_icon)
    ImageView rightSecondIcon;
}
