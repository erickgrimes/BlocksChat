package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.sample.chat.R;

public class SplashActivity extends Activity implements QBCallback {

    private static final String APP_ID = "14818";
    private static final String AUTH_KEY = "6zTZ8BF4RXVhpNK";
    private static final String AUTH_SECRET = "SK4GwLAffgDSTd3";

    private ProgressBar progressBar;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    int extra=0;
    QBUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Activity activity = this;
        prefs = activity.getSharedPreferences("imentor",activity.MODE_PRIVATE);
        prefsEditor=prefs.edit();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        QBAuth.createSession(this);
        if(prefs.getBoolean("isSaved",false)){
            String login = prefs.getString("username","");
            String password = prefs.getString("password","");

            user = new QBUser(login, password);
            QBUsers.signIn(user, this);
        }
    }

    @Override
    public void onComplete(Result result) {
        progressBar.setVisibility(View.GONE);

        if (result.isSuccess()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("pairID",extra);
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Error(s) occurred. Look into DDMS log for details, " +
                    "please. Errors: " + result.getErrors()).create().show();
        }
    }

    @Override
    public void onComplete(Result result, Object context) {
    }
}