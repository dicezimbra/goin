package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.Notification;
import br.com.legacy.models.NotificationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by teruy on 23/10/2017.
 */

public class NotificationsManager extends BaseManager {

    private ProgressDialog progressDialog;

    public NotificationsRequestHandler notificationsRequestHandler;

    public interface NotificationsRequestHandler {
        void onReceiveNotification(List<NotificationModel> notifications);
        void onError(String error);
    }

    public NotificationsManager(Activity activity) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }

    public void requestNotifications(final String lastId) {

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<Notification>>>() {
            @Override
            public void onResponse(HashMap<String, List<Notification>> response) {
                if (notificationsRequestHandler != null && response!= null) {
                    notificationsRequestHandler.onReceiveNotification(mapToModelList(response.get("notifications")));
                }

                onlineCall(lastId);
            }

            @Override
            public void onFailure(ErrorResponse error) {
               onlineCall(lastId);
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                cacheServiceManager.getNotifications( handler, lastId, sharedPreferencesControl);
            }
        });

        onlineCall(lastId);
    }

    private void onlineCall(String lastId) {
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<Notification>>>() {
            @Override
            public void onResponse(HashMap<String, List<Notification>> response) {
                if (notificationsRequestHandler != null && response != null) {
                    notificationsRequestHandler.onReceiveNotification(mapToModelList(response.get("notifications")));
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (notificationsRequestHandler != null) {
                    notificationsRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getNotifications(NotificationsManager.this, handler, lastId);
            }
        });
    }

    private List<NotificationModel> mapToModelList(List<Notification> notifications) {
        List<NotificationModel> models = new ArrayList<>();
        if (notifications != null) {
            for (Notification notification : notifications) {
                NotificationModel notificationModel = mapToModel(notification);
                if(notificationModel.type != null && ! notificationModel.type.equals(NotificationModel.NotificationType.undefined)){
                    models.add(mapToModel(notification));
                }
            }
        }
        return models;
    }

    private NotificationModel mapToModel(Notification notification) {
        NotificationModel model = new NotificationModel(activity);
        model.notificationId = notification.notificationId;
        model.userName = notification.firstName;
        model.secondName = notification.secondName;
        model.categoryType = notification.categoryType;
        model.time = notification.timeStamp;
        try {
            model.type = NotificationModel.NotificationType.valueOf(notification.type);
        } catch (IllegalArgumentException e) {
            model.type = NotificationModel.NotificationType.undefined;
        }
        model.creatorId = notification.creatorId;
        model.destinationId = notification.destinationId;
        if(notification.followedByMe != null){
            model.followedByMe = notification.followedByMe;
        } else {
            model.followedByMe = false;
        }
        model.avatar = notification.avatar;
        return model;
    }
}
