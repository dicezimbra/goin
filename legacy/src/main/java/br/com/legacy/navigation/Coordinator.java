package br.com.legacy.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import br.com.R;
import br.com.goin.component.model.category.Category;
import br.com.goin.component.navigation.NavigationController;
import br.com.legacy.managers.SessionManager;
import br.com.legacy.models.PurchaseModel;
import br.com.legacy.models.TicketModel;
import br.com.legacy.utils.Constants;
import br.com.legacy.viewControllers.activitites.GalleryDetailActivity;
import br.com.legacy.viewControllers.activitites.InfoPaymentActivity;
import br.com.legacy.viewControllers.activitites.PaymentMethodActivity;
import br.com.legacy.viewControllers.activitites.PhotoGalleryActivity;
import br.com.legacy.viewControllers.activitites.PlayerActivity;
import br.com.legacy.viewControllers.activitites.PostLikersActivity;
import br.com.legacy.viewControllers.activitites.PurchaseDetailActivity;
import br.com.legacy.viewControllers.activitites.SelectFriendsActivity;
import br.com.legacy.viewControllers.activitites.SinglePostActivity;
import br.com.legacy.viewControllers.activitites.TicketPurchaseActivity;
import br.com.legacy.viewControllers.activitites.UploadProfilePictureActivity;
import br.com.legacy.viewControllers.activitites.VipBoxPaymentActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class Coordinator {

    private static NavigationController navigationController = NavigationController.Companion.getInstance();

    public static void goToChooseProfilePicture(Context activity) {
        activity.startActivity(new Intent(activity, UploadProfilePictureActivity.class));
    }

    public static void goToStart(Context activity) {
        navigationController.loginModule().goToLogin(activity, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                return null;
            }
        });
    }

    public static void goToTabsMain(Activity activity) {
        navigationController.homeModule().goToHome(activity, "");
    }

    public static void goToSinglePost(Activity activity, String postId) {
        Intent intent = new Intent(activity, SinglePostActivity.class);
        intent.putExtra(Constants.POST_ID, postId);
        activity.startActivity(intent);
    }

    public static void goToSearchClubResultsFinish(Activity activity, Bundle args, boolean shouldShowFilterButton, ArrayList<String> filtersList, String categoryId, Category.CategoryType categoryType) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ENABLE_BUTTON, shouldShowFilterButton);
        intent.putStringArrayListExtra(Constants.SEARCH_FILTERS, filtersList);
        intent.putExtra(Constants.CATEGORY_ID, categoryId);
        intent.putExtra(Constants.DATA, args);
        intent.putExtra(Constants.TYPE, categoryType);
        intent.putExtra(Constants.CAME_FROM_CLUB, Constants.CAME_FROM_CLUB);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static void goToGalleryDetail(Activity activity, int position, ArrayList<String> photoUrls) {
        Intent intent = new Intent(activity, GalleryDetailActivity.class);
        Bundle b = new Bundle();
        ArrayList<String> photosList = photoUrls;
        b.putStringArrayList(Constants.PHOTO_URLS, photosList);
        b.putInt(Constants.POSITION, position);
        if (activity instanceof PhotoGalleryActivity) {
            b.putBoolean(Constants.IS_CLUB_GALLERY, true);
        } else {
            b.putBoolean(Constants.IS_CLUB_GALLERY, false);
        }
        intent.putExtras(b); //Put your id to your next Intent
        activity.startActivity(intent);
    }

    public static void goToChoosePaymentMethods(Activity activity, PurchaseModel purchaseModel, String originType) {
        Intent intent = new Intent(activity, PaymentMethodActivity.class);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        intent.putExtra(Constants.ORIGIN_TYPE, originType);
        activity.startActivity(intent);
    }

    public static void goToInfoPayment(Activity activity, PurchaseModel purchaseModel,
                                       PaymentMethodActivity.PaymentMethod paymentMethod, String originType) {
        Intent intent = new Intent(activity, InfoPaymentActivity.class);
        intent.putExtra(Constants.USER_NAME, SessionManager.getInstance().getCurrentUser(activity).getName());
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        intent.putExtra(Constants.ORIGIN_TYPE, originType);
        intent.putExtra(Constants.TYPE, paymentMethod);
        activity.startActivity(intent);
    }

    public static void setStatusBarColor(Activity activity, String color) {

        if (Build.VERSION.SDK_INT >= 21) {
            if (color != null) activity.getWindow().setStatusBarColor(Color.parseColor(color));
            else
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
    }

    public static void setStatusBarColor(Activity activity, String color, View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (color != null) {
                int flags = view.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
                activity.getWindow().setStatusBarColor(Color.parseColor(color));
            } else {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            }
        }
    }

    public static void setStatusBarColorDark(Activity activity, String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (color != null) {

                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.black));
            } else {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            }
        }
    }

    public static void goToPostLikers(Activity activity, String postid) {
        Intent intent = new Intent(activity, PostLikersActivity.class);
        intent.putExtra(Constants.POST_ID, postid);
        activity.startActivity(intent);
    }

    public static void goToPostLikersFromNotification(Activity activity, String postId) {
        Intent intent = new Intent(activity, SinglePostActivity.class);
        intent.putExtra(Constants.POST_ID, postId);
        activity.startActivity(intent);
        intent = new Intent(activity, PostLikersActivity.class);
        intent.putExtra(Constants.POST_ID, postId);
        activity.startActivity(intent);
    }

    public static void goToPlayer(Activity activity, String url) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        if (url != null)
            intent.putExtra(Constants.URL, url);

        activity.startActivity(intent);
    }

    public static void goToSelectTicketOwners(Activity activity, TicketModel ticketModel) {
        Intent intent = new Intent(activity, SelectFriendsActivity.class);
        intent.putExtra(Constants.TICKET, (new Gson()).toJson(ticketModel));
        intent.putExtra(Constants.TYPE, SelectFriendsActivity.SelectFriendsType.BuyTickets);
        activity.startActivityForResult(intent, Constants.REQUEST_SELECTED_FRIENDS);
    }

    public static void goToTicketPurchase(Activity activity, TicketModel ticketModel, String originType) {
        Intent intent = new Intent(activity, TicketPurchaseActivity.class);
        intent.putExtra(Constants.TICKET, (new Gson()).toJson(ticketModel));
        intent.putExtra(Constants.ORIGIN_TYPE, originType);
        activity.startActivity(intent);
    }

    public static void goToPurchaseConfirmation(Activity activity, PurchaseModel purchaseModel,
                                                String paymentToken, String creditCardMask, String originType) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.PurchaseConfirmation);
        intent.putExtra(Constants.TOKEN, paymentToken);
        intent.putExtra(Constants.DISPLAY_NUMBER, creditCardMask);
        intent.putExtra(Constants.ORIGIN_TYPE, originType);
        activity.startActivity(intent);
    }

    public static void goToPurchaseConfirmation(Activity activity, PurchaseModel purchaseModel) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.PurchaseConfirmation);
        activity.startActivity(intent);
    }

    public static void goToPurchaseDetails(Activity activity, String ticketId, boolean isPastEvent, PurchaseModel purchaseModel, boolean finishPastActivities) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        if (finishPastActivities) {
            activity.finishAffinity();
        }
        intent.putExtra(Constants.TICKET_ID, ticketId);
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.ReadOperation);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        intent.putExtra(Constants.IS_PAST_EVENT, isPastEvent);
        activity.startActivity(intent);
    }

    public static void redirectToPurchaseDetails(Activity activity, String ticketId) {
        Intent purchaseDetailsIntent = new Intent(activity, PurchaseDetailActivity.class);
        purchaseDetailsIntent.putExtra(Constants.TICKET_ID, ticketId);
        purchaseDetailsIntent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.ReadOperation);
        activity.startActivity(purchaseDetailsIntent);
    }

    public static void goToTransferTicket(Context activity, PurchaseModel purchaseModel, PurchaseModel.TicketsInfo info, String avatarUrl) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.TransferTicket);
        if (purchaseModel.ticketsInfo.size() == 2) {
            purchaseModel.ticketsInfo.remove(1);
        }
        purchaseModel.ticketsInfo.add(info);
        intent.putExtra(Constants.URL, avatarUrl);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        activity.startActivity(intent);
    }

    public static void goToTransferConfirmation(Activity activity, PurchaseModel purchaseModel) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.ConfirmTransfer);
        intent.putExtra(Constants.PURCHASE_MODEL, (new Gson()).toJson(purchaseModel));
        activity.startActivity(intent);
    }

    public static void goToVipBoxPayment(Activity activity, String ticketId) {
        Intent intent = new Intent(activity, VipBoxPaymentActivity.class);
        intent.putExtra(Constants.TICKET_ID, ticketId);
        activity.startActivity(intent);
    }

    public static void goToWebsite(Context activity, String siteUrl) {
        if (siteUrl != null) {
            if (!siteUrl.startsWith("http://") && !siteUrl.startsWith("https://")) {
                siteUrl = "http://" + siteUrl;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(siteUrl));
            activity.startActivity(intent);
        }
    }

    public static void goToForgotPassword(@NotNull Context context, boolean b) {
        navigationController.loginModule().goToForgotPassword(context, b);
    }
}
