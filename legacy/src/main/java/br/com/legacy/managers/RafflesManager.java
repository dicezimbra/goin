package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.Raffle;
import br.com.legacy.models.RaffleCardModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luciana on 13/12/17.
 */

public class RafflesManager extends BaseManager {

    public RafflesRequestHandler rafflesRequestHandler;
    public RafflesJoiningRequestHandler rafflesJoiningRequestHandler;
    ProgressDialog progressDialog;

    public RafflesManager(Activity activity){
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }

    public interface RafflesRequestHandler {
        void onReceiveRaffles(List<RaffleCardModel> raffles);
        void onFailureToReceiveRaffles(String error);
    }

    public interface RafflesJoiningRequestHandler {
        void onJoinRaffle();
        void onLeaveRaffle();
        void onFailure(String error);
    }

    public void requestRafflesList(final Integer count) {

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<Raffle>>>() {
            @Override
            public void onResponse(HashMap<String, List<Raffle>> response) {

                List<RaffleCardModel> raffles = mapDtoListToModelList(response.get("getMyRaffles"), activity);
                for (RaffleCardModel r : raffles)
                    r.isParticipating = true;
                if (rafflesRequestHandler != null) {
                    rafflesRequestHandler.onReceiveRaffles(raffles);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (rafflesRequestHandler != null) {
                    rafflesRequestHandler.onFailureToReceiveRaffles(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getMyRaffles(RafflesManager.this, handler, count);
            }
        });
    }

    public void requestRafflesList(final String userId, final Integer count) {

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<Raffle>>>() {
            @Override
            public void onResponse(HashMap<String, List<Raffle>> response) {

                List<RaffleCardModel> raffles = mapDtoListToModelList(response.get("getMyRaffles"), activity);
                for (RaffleCardModel r : raffles)
                    r.isParticipating = true;
                if (rafflesRequestHandler != null) {
                    rafflesRequestHandler.onReceiveRaffles(raffles);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (rafflesRequestHandler != null) {
                    rafflesRequestHandler.onFailureToReceiveRaffles(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getUserRaffles(RafflesManager.this, handler, userId, count);
            }
        });
    }

    public void joinRaffle(final String id, final RaffleCardModel.RaffleItemParticipateFeedback feedback) {
        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {
                if (rafflesJoiningRequestHandler != null){
                    rafflesJoiningRequestHandler.onJoinRaffle();
                    feedback.onSuccess();
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (rafflesJoiningRequestHandler != null){
                    feedback.onError();
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.joinRaffle(RafflesManager.this, handler, id);
            }
        });
    }

    public void leaveRaffle(final String id, final RaffleCardModel.RaffleItemParticipateFeedback feedback) {
        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {
                if (rafflesJoiningRequestHandler != null){
                    feedback.onSuccess();
                    rafflesJoiningRequestHandler.onLeaveRaffle();
                }

            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (rafflesJoiningRequestHandler != null){
                    rafflesJoiningRequestHandler.onFailure(error.message);
                    feedback.onError();
                }

            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.leaveRaffle(RafflesManager.this, handler, id);
            }
        });
    }

    private List<RaffleCardModel> mapDtoListToModelList(List<Raffle> list, Activity activity) {
        List<RaffleCardModel> raffles = new ArrayList<>();
        if (list != null)
            for (Raffle item : list) {
                raffles.add(mapDtoToModel(item, activity));
            }
        return raffles;
    }

    public static RaffleCardModel mapDtoToModel(Raffle item, Activity activity) {
        RaffleCardModel model = new RaffleCardModel(activity);
        model.id = item.raffleId;
        model.idPost = item.raffleId;
        model.name = item.name;
        model.imageUrl = item.image;
        model.description = item.description == null? "" : item.description;
        model.isParticipating = item.joined == null? false : item.joined;
        if (item.endTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.endTime);
            model.endTime = calendar;
        }
        model.maxPeople = item.maxPeople == null? 0 : item.maxPeople;

        return model;
    }
}
