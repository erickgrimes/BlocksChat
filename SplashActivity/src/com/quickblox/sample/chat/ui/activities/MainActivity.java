package com.quickblox.sample.chat.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.sample.chat.App;
import com.quickblox.sample.chat.R;
import com.quickblox.sample.chat.core.SingleChat;
import com.quickblox.sample.chat.ui.fragments.RoomsFragment;
import com.quickblox.sample.chat.ui.fragments.UsersFragment;

import org.jivesoftware.smack.ConnectionListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static final int AUTHENTICATION_REQUEST = 1;
    private static final int POSITION_USER = 0;
    private static final int POSITION_ROOM = 1;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private Action lastAction;
    private ConnectionListener connectionListener;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("imentor",this.MODE_PRIVATE);
        boolean pairID=prefs.getBoolean("isPartner",false);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if(pairID){
            startChat();
        }



        List<Fragment> tabs = new ArrayList<Fragment>();
        tabs.add(UsersFragment.getInstance());
        tabs.add(RoomsFragment.getInstance());

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(sectionsPagerAdapter.getPageTitle(i)).setTabListener(
                    this));
        }
        showUsersFragment();
        showAuthenticateDialog();
    }

    public void startChat(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatActivity.EXTRA_MODE, ChatActivity.Mode.SINGLE);
        bundle.putInt(SingleChat.EXTRA_USER_ID, prefs.getInt("partner",0));
        ChatActivity.start(this, bundle);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int position = tab.getPosition();
        QBUser qbUser = ((App) getApplication()).getQbUser();
        if (qbUser != null) {
            viewPager.setCurrentItem(position);
        } else if (position == POSITION_ROOM) {
            lastAction = Action.ROOM_LIST;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ((UsersFragment) sectionsPagerAdapter.getItem(POSITION_USER)).startChat();
            connectionListener = new ChatConnectionListener();
            QBChatService.getInstance().addConnectionListener(connectionListener);
            ((RoomsFragment) sectionsPagerAdapter.getItem(POSITION_ROOM)).loadRooms();
        } else {

        }
    }

    private void showUsersFragment() {
        getSupportActionBar().selectTab(getSupportActionBar().getTabAt(POSITION_USER));
        viewPager.setCurrentItem(POSITION_USER);
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    public void showAuthenticateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Authorize first");
        builder.setItems(new String[]{"Login", "Register"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, AUTHENTICATION_REQUEST);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, RegistrationActivity.class);
                        startActivityForResult(intent, AUTHENTICATION_REQUEST);
                        break;
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showUsersFragment();
            }
        });
        builder.show();
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static enum Action {CHAT, ROOM_LIST}

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POSITION_USER:
                    return "Users";
                case POSITION_ROOM:
                    return "Rooms";
            }
            return null;
        }
    }

    private class ChatConnectionListener implements ConnectionListener {

        @Override
        public void connectionClosed() {
            showToast("connectionClosed");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            showToast("connectionClosed on error" + e.getLocalizedMessage());
        }

        @Override
        public void reconnectingIn(int i) {

        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectionFailed(Exception e) {

        }
    }
}
