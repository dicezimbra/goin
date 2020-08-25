package br.com.legacy.customViews;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.databinding.ItemMessageInputBinding;

public class OSMessageInput extends LinearLayout {

    private Context context;
    private View divider;
    private LinearLayout container;
    private LinearLayout attachmentButtonContainer;
    private ImageView attachmentImageView;
    private EditText editText;
    private LinearLayout sendButtonContainer;
    private ImageView sendImageView;
    private float sendButtonDisabledAlpha = 0.2f;

    private ItemMessageInputBinding binding;

    public OSMessageInput(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OSMessageInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = ItemMessageInputBinding.inflate(inflater, this, true);
        findViews();

    }

    void findViews(){
        divider = binding.messageDivider;
        container = binding.messageContainer;
        attachmentButtonContainer = binding.messageAttachmentButton;
        attachmentImageView = binding.messageAttachmentImageView;
        editText = binding.messageEditText;
        sendButtonContainer = binding.messageSendButton;
        sendImageView = binding.messageSendImageView;
    }

    public View getDivider() {
        return divider;
    }

    public LinearLayout getContainer() {
        return container;
    }

    public LinearLayout getAttachmentButtonContainer() {
        return attachmentButtonContainer;
    }

    public ImageView getAttachmentImageView() {
        return attachmentImageView;
    }

    public EditText getEditText() {
        return editText;
    }

    public LinearLayout getSendButtonContainer() {
        return sendButtonContainer;
    }

    public ImageView getSendImageView() {
        return sendImageView;
    }

    public ItemMessageInputBinding getBinding() {
        return binding;
    }

    public void setSendButtonDisabledAlpha(float sendButtonDisabledAlpha) {
        this.sendButtonDisabledAlpha = sendButtonDisabledAlpha;
    }

    public void setSendButtonEnabled(boolean enabled){
        if(enabled){
            sendButtonContainer.setAlpha(1);
        } else {
            sendButtonContainer.setAlpha(sendButtonDisabledAlpha);
        }
        sendButtonContainer.setEnabled(enabled);
    }
}
