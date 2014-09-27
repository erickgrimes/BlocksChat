package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.sample.chat.R;

public class SplashActivity extends Activity implements QBCallback {

    private static final String APP_ID = "14818";
    private static final String AUTH_KEY = "6zTZ8BF4RXVhpNK";
    private static final String AUTH_SECRET = "SK4GwLAffgDSTd3";

    private ProgressBar progressBar;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    int extra=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        QBAuth.createSession(this);
        if(prefs.getBoolean("isMatched",false)){
            extra=prefs.getInt("pairID",0);
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