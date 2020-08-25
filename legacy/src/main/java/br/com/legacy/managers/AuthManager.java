package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;

import br.com.BuildConfig;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.QueryResponse;

/**
 * Created by kalunga on 22/08/2017.
 */

public class AuthManager extends BaseManager {

    private static final String TAG = AuthManager.class.getSimpleName();

    ProgressDialog progressDialog;

    public interface UpdateHandler {
        void forceUpdate();

        void optionalUpdate();

        void updated();

        void onUpdateError(String error);
    }

    public AuthManager(Activity activity) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }


}
