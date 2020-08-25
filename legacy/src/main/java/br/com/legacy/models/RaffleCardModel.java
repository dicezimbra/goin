package br.com.legacy.models;

import android.app.Activity;

import androidx.databinding.Bindable;

import br.com.BR;
import br.com.R;
import br.com.goin.base.utils.Utils;
import br.com.goin.component.ui.kit.dialog.DialogUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by luciana on 13/12/17.
 */

public class RaffleCardModel extends FeedCardModel {

    public interface RaffleItemHandler {
        void onClickParticipateButton(String id, Boolean state, RaffleItemParticipateFeedback feedback);
    }

    public interface RaffleItemParticipateFeedback {
        void onSuccess();
        void onError();
    }

    public Activity activity;

    public String id;
    public String name;
    public String time;
    public String imageUrl;
    public String description;
    @Bindable
    public Boolean isParticipating;
    public Calendar endTime;
    public Integer maxPeople;

    public RaffleItemHandler handler;

    public RaffleCardModel(Activity activity) {
        this.activity = activity;
    }

    public String getDate() {
        DateFormat date = new SimpleDateFormat(Utils.INSTANCE.getDateFormat());
        return activity.getResources().getString(R.string.raffle_at) + " " + date.format(endTime.getTime());
    }

    public String getMaximum() {
        String max = activity.getResources().getQuantityString(R.plurals.maximum_of_participant,maxPeople, maxPeople);
        return max;
    }

    public void onClickJoinButton() {
        if (this.isParticipating) {
            DialogUtils.INSTANCE.showDecisionDialog(activity, activity.getString(R.string.confirm_unfollow_raffle),
                    activity.getString(R.string.yes), activity.getString(R.string.no), new DialogUtils.DecisionListener() {
                        @Override
                        public void onAccept() {
                            participate(false);
                        }

                        @Override
                        public void onReject() {

                        }
                    });
        } else {
            participate(true);
        }
    }

    public void participate(final Boolean isParticipating) {
        this.isParticipating = isParticipating;
        notifyPropertyChanged(BR.isParticipating);

        handler.onClickParticipateButton(id, isParticipating, new RaffleItemParticipateFeedback() {
            @Override
            public void onSuccess() {}
            @Override
            public void onError() {
                RaffleCardModel.this.isParticipating = !isParticipating;
                notifyPropertyChanged(BR.isParticipating);
                DialogUtils.INSTANCE.handleError(activity);
            }
        });
    }
}
