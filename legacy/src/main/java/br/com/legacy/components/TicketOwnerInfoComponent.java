package br.com.legacy.components;

import android.content.res.Resources;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import br.com.legacy.customViews.CustomEditText;
import br.com.R;

public class TicketOwnerInfoComponent extends BaseObservable {

    private ObservableInt index = new ObservableInt();
    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> font = new ObservableField<>();
    public String rg;
    public String halfEntranceId;
    public String id;
    private EditText rgEditText;
    private EditText halfEntranceEditText;
    public CloseComponentHandler closeComponentHandler;
    public TransferTicketHandler transferTicketHandler;
    public boolean isMyTicketComponent = false;
    public boolean hasHalfEntrance = false;
    public boolean shouldShowTransferButton = false;

    Resources res;

    TicketOwnerInfoComponent() {

    }

    public interface CloseComponentHandler {
        void removeComponent(int index);
    }

    public interface TransferTicketHandler {
        void transferTicket(TicketOwnerInfoComponent ticketOwnerInfoComponent);
    }


    public TicketOwnerInfoComponent(CloseComponentHandler handler) {
        this.closeComponentHandler = handler;
    }

    public TicketOwnerInfoComponent(Resources res, TransferTicketHandler handler) {
        this.res = res;
        this.transferTicketHandler = handler;
    }

    public String getTicketLabelText() {
        return "Ticket " + index.get();
    }

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public EditText getRgEditText() {
        return rgEditText;
    }

    public void setRgEditText(EditText rgEditText) {
        this.rgEditText = rgEditText;
    }

    public EditText getHalfEntranceEditText() {
        return halfEntranceEditText;
    }

    public void setHalfEntranceEditText(EditText halfEntranceEditText) {
        this.halfEntranceEditText = halfEntranceEditText;
    }

    public void onClickCloseComponent() {
        if (closeComponentHandler != null && !isMyTicketComponent) {
            closeComponentHandler.removeComponent(this.index.get() - 1);
        }
    }

    public void onClickTransferButton(View v) {
        if (transferTicketHandler != null && isMyTicketComponent) {
            transferTicketHandler.transferTicket(this);
        }
    }

    @BindingAdapter("android:setCustomStyle")
    public static void setPaddingLeft(View view, boolean customStyle) {
        ((CustomEditText) view).setStyle(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, R.color.gray, R.color.lightGray, 12);
    }

    @BindingAdapter("android:rg")
    public static void setRgEditText(View view, final TicketOwnerInfoComponent ticketOwnerInfoComponent) {
        if (view instanceof EditText) {
            ticketOwnerInfoComponent.setRgEditText((EditText) view);
            if (ticketOwnerInfoComponent.rg != null && !ticketOwnerInfoComponent.rg.isEmpty()) {
                ticketOwnerInfoComponent.getRgEditText().setText(ticketOwnerInfoComponent.rg);
            } else {
                ticketOwnerInfoComponent.getRgEditText().setText("");
            }
        }
    }

    @BindingAdapter("android:halfEntrance")
    public static void setHalfEntranceEditText(View view, TicketOwnerInfoComponent ticketOwnerInfoComponent) {
        if (view instanceof EditText) {
            ticketOwnerInfoComponent.setHalfEntranceEditText((EditText) view);
            if (ticketOwnerInfoComponent.halfEntranceId != null && !ticketOwnerInfoComponent.halfEntranceId.isEmpty()) {
                ticketOwnerInfoComponent.getHalfEntranceEditText().setText(ticketOwnerInfoComponent.halfEntranceId);
            } else {
                ticketOwnerInfoComponent.getHalfEntranceEditText().setText("");
            }
        }
    }

    public String getIndexLabel() {
        return index.get() + "-";
    }

    public String getRgInfo() {
        return res.getString(R.string.user_rg_info) + rg;
    }

    public String getHalfInfo() {
        return res.getString(R.string.user_half_info) + halfEntranceId;
    }

    public int getButtonVisibility() {
        if (isMyTicketComponent && shouldShowTransferButton) {
            return View.VISIBLE;
        }
        return View.GONE;
    }

}
