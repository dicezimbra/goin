package br.com.legacy.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.library.OSEditText;
import br.com.R;

/**
 * Created by ruderos & ivo on 8/17/17.
 */

public class CustomEditText extends LinearLayout {

    private EditText editText;
    ImageView icon;
    LinearLayout outsideContainer;
    RelativeLayout insideContainer;

    Drawable iconSrc;
    String customHint;
    String customText;
    Boolean isEditable;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomEditText,0,0);
        try {
            iconSrc = a.getDrawable(R.styleable.CustomEditText_icon);
            customHint = a.getString(R.styleable.CustomEditText_hint_text);
            customText = a.getString(R.styleable.CustomEditText_text);
            isEditable = a.getBoolean(R.styleable.CustomEditText_editable,true);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_edit_text, this);
        this.editText = findViewById(R.id.edit_text);
        this.outsideContainer = findViewById(R.id.custom_et_container);
        this.insideContainer = findViewById(R.id.custom_info_container);
        this.editText.setSingleLine();
        this.editText.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        this.icon = findViewById(R.id.icon);

        if (iconSrc != null) {
            this.icon.setImageDrawable(iconSrc);
        }
        if (customHint != null) {
            this.editText.setHint(customHint);
        }
        if (customText != null) {
            this.editText.setText(customText);
        }
        if (!isEditable) {
            this.editText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            this.editText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            this.icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            this.editText.setFocusable(isEditable);
        }
    }


    public void setStyle(int inputType, int textColor, int underlineColor, float sizeInSp) {
        this.setInputType(inputType);
        this.setColor(textColor, underlineColor);
        this.setTextSize(sizeInSp);
    }

    public void setStyle(int textColor, int underlineColor, float sizeInSp){
        this.setColor(textColor, underlineColor);
        this.setTextSize(sizeInSp);
    }

    public void setStyle(int inputType, int textColor, int hintTextColor, int underlineColor, float sizeInSp) {
        setStyle(inputType,textColor,underlineColor,sizeInSp);
        this.editText.setHintTextColor(ContextCompat.getColor(getContext(), hintTextColor));
    }

    public void setTextSize(float sizeInSP) {
        this.editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeInSP);
    }

    public  void setColor(int color) {
        this.editText.setTextColor(ContextCompat.getColor(getContext(), color));
        this.icon.setColorFilter(ContextCompat.getColor(getContext(), color));
        this.editText.setHintTextColor(ContextCompat.getColor(getContext(), color));
    }

    public  void setColor(int color, int underlineColor) {
        setColor(color);
        View underline = findViewById(R.id.underline);
        underline.setBackgroundColor(getResources().getColor(underlineColor));
    }


    public String getText() {
        return this.editText.getText().toString();
    }

    public void setText(CharSequence text) { this.editText.setText(text); }

    public void setError(){
        this.editText.setError("As senhas não são compatíveis");
    }

    public void setIsPassword() {
        this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setInputType(int type) {
        editText.setInputType(type);
    }

    public EditText getEditText(){
        return editText;
    }

    public void setOnChangeListener(TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }


    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }

    }

    @BindingAdapter("android:setMatchParent")
    public static void setMargin(View view, boolean shouldMatchParent) {
        if(!shouldMatchParent) return;
        if(view instanceof CustomEditText){
            LinearLayout outsideContainer = ((CustomEditText) view).outsideContainer;
            outsideContainer.setPadding(0,outsideContainer.getPaddingTop(),0,outsideContainer.getPaddingBottom());
            RelativeLayout insideContainer = ((CustomEditText) view).insideContainer;
        }
    }

    @BindingAdapter("android:numericalKeyboard")
    public static void setNumericalKeyboard(View view, boolean enabled) {
        if(!enabled) return;
        if(view instanceof CustomEditText){
            ((CustomEditText) view).getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    @BindingAdapter("android:uppercaseKeyboard")
    public static void setUppercaseKeyboard(View view, boolean enabled) {
        if(!enabled) return;
        if(view instanceof CustomEditText){
            ((CustomEditText) view).getEditText().setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }
    }

    @BindingAdapter("android:maxLength")
    public static void setMaxLength(View view, int maxLength) {
        if(view instanceof CustomEditText){
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            ((CustomEditText) view).getEditText().setFilters(fArray);
        }
    }

    @BindingAdapter("android:phone_mask")
    public static void setPhoneMask(View view, Activity activity){
        if(view instanceof CustomEditText){
            OSEditText phoneValid = new OSEditText.Builder(activity, ((CustomEditText)view).editText)
                                    .isPhone(true)
                                    .alert(true)
                                    .setMask("(##)#####-####")
                                    .build();
        }
    }

    @BindingAdapter("android:postal_code_mask")
    public static void setPostalCodeMask(View view, Activity activity){
        if(view instanceof CustomEditText){
            OSEditText postalCodeValid = new OSEditText.Builder(activity, ((CustomEditText)view).editText)
                                        .alert(true)
                                        .setMask("#####-###")
                                        .build();
        }
    }

    @BindingAdapter("android:cpf_mask")
    public static void setCPFmask(View view, Activity activity){
        if(view instanceof CustomEditText){
            OSEditText validCPF = new OSEditText.Builder(activity, ((CustomEditText)view).editText)
                    .isCpf(true)
                    .alert(true)
                    .setMask("###.###.###-##")
                    .build();
        }
    }

    @BindingAdapter("android:credit_card_mask")
    public static void setCreditCardMask(View view, Activity activity){
        if(view instanceof CustomEditText){
            OSEditText validCreditCard = new OSEditText.Builder(activity, ((CustomEditText)view).editText)
                    .alert(true)
                    .setMask("####-####-####-####")
                    .build();
        }
    }

    @BindingAdapter("android:email_validate")
    public static void validateMail(View view, Activity activity){
        if(view instanceof CustomEditText){
            ((CustomEditText)view).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
    }

    @BindingAdapter("android:birthday_mask")
    public static void setBirthdaymask(View view, Activity activity){
        if(view instanceof CustomEditText){
            OSEditText validCPF = new OSEditText.Builder(activity, ((CustomEditText)view).editText)
                    .isCpf(true)
                    .alert(true)
                    .setMask("##/##/####")
                    .build();
        }
    }
}
