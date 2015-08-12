package com.offsec.nethunter;

import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

//import android.app.Fragment;
//import android.app.FragmentManager;

public class AppNavHomeActivity extends FragmentActivity
        implements SideMenu.NavigationDrawerCallbacks {

    public final static int NETHUNTER_FRAGMENT = 0;
    public final static int KALILAUNCHER_FRAGMENT = 1;
    public final static int KALISERVICES_FRAGMENT = 2;
    public final static int HIDE_FRAGMENT = 3;
    public final static int DUCKHUNTER_FRAGMENT = 4;
    public final static int BADUSB_FRAGMENT = 5;
    public final static int MANA_FRAGMENT = 6;
    public final static int DNSMASQ_FRAGMENT = 7;
    public final static int IPTABLES_FRAGMENT = 8;

    public static final int REQUEST_CODE = 0;
    public static final int CUSTOM_TILE_LIST_ID = 2;
    public static final int CUSTOM_TILE_GRID_ID = 3;
    public static final String ACTION_TOGGLE_STATE =
            "com.offsec.nethunter.ACTION_TOGGLE_STATE";
    public static final String STATE = "state";
    public static int CUSTOM_TILE_ID = 1337;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private SideMenu mNavigationDrawerFragment;
    private String[] activityNames;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.base_layout);
        //set kali wallpaper as background
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kali-nh/wallpaper/kali-nh-2183x1200.png";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        ImageView myImageView = (ImageView) findViewById(R.id.bgHome);
        myImageView.setImageBitmap(bitmap);


        if (Build.VERSION.SDK_INT >= 21) {
            // detail for android 5 devices
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkTitle));
        }

        mNavigationDrawerFragment = (SideMenu)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        String[][] activitiesInfo = mNavigationDrawerFragment.getMenuInfo();
        activityNames = activitiesInfo[0];
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        setupQuickSettingsTile();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String activity) {
        //Log.d("POSI", String.valueOf(position));
        // This is called from the sidemenu as callback when a item  is clickled

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case NETHUNTER_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, NetHunterFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case KALILAUNCHER_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, KaliLauncherFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case KALISERVICES_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, KaliServicesFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case HIDE_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, HidFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case DUCKHUNTER_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, DuckHunterFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case BADUSB_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, BadusbFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case MANA_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, ManaFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case DNSMASQ_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, DnsmasqFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            case IPTABLES_FRAGMENT:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, IptablesFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                // Start activity as usually // REMOVE THIS SOON no needed
                Intent target = new Intent();
                target.setClassName(getApplicationContext(), activity);
                startActivity(target);
                break;
        }
    }

    public void onSectionAttached(int position) {
        mTitle = activityNames[position];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        //Handle back button for fragments && menu
        //FragmentManager fragmentManage    r = getFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            finish();

            return;
        }
        super.onBackPressed();
    }

    public void showMessage(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


    public String readConfigFile(String configFilePath) {


        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, configFilePath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Log.e("Nethunter", "exception", e);
            Logger Logger = new Logger();
            Logger.appendLog(e.getMessage());
        }
        return text.toString();
    }

    public boolean updateConfigFile(String configFilePath, String source) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File myFile = new File(sdcard, configFilePath);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(source);
            myOutWriter.close();
            fOut.close();
            return true;
        } catch (Exception e) {
            showMessage(e.getMessage());
            Logger Logger = new Logger();
            Logger.appendLog(e.getMessage());
            return false;
        }
    }

    private PendingIntent buildTermIntent(String cmd, Context ctx) {
        Intent i = new Intent("jackpal.androidterm.RUN_SCRIPT");
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.putExtra("jackpal.androidterm.iInitialCommand", cmd);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(ctx, 0, i, 0);
    }

    private void setupQuickSettingsTile() {
        String[] commands = {
                "su -c bootkali",
                "su -c 'bootkali kalimenu'",
                "su -c 'bootkali wifite'",
                "su -c 'bootkali wifi-disable'",
                "su -c 'bootkali update'",
                "su -c killkali"
        };

        String[] labels = {
                getString(R.string.launch_kali_shell),
                getString(R.string.launch_kali_menu),
                getString(R.string.launch_wifite),
                getString(R.string.turn_off_external_wifi),
                getString(R.string.update_kali_chroot),
                getString(R.string.kill_kali)
        };

        ArrayList<CustomTile.ExpandedListItem> expandedListItems = new ArrayList<>();

        for (int i = 0; i < commands.length; i++) {
            CustomTile.ExpandedListItem expandedListItem = new CustomTile.ExpandedListItem();
            expandedListItem.setExpandedListItemDrawable(R.drawable.ic_action_code);
            expandedListItem.setExpandedListItemTitle(labels[i]);
            expandedListItem.setExpandedListItemOnClickIntent(buildTermIntent(commands[i], this));
            expandedListItems.add(expandedListItem);
        }

        CustomTile.ListExpandedStyle listExpandedStyle = new CustomTile.ListExpandedStyle();
        listExpandedStyle.setListItems(expandedListItems);

        CustomTile mCustomTile = new CustomTile.Builder(this)
                .setExpandedStyle(listExpandedStyle)
                .setContentDescription("Kali Linux NetHunter")
                .setLabel("Kali NetHunter")
                .setIcon(R.drawable.ic_tile)
                .setContentDescription("Kali Linux NetHunter")
                .setOnSettingsClickIntent(new Intent(this, AppNavHomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                .build();

        CMStatusBarManager.getInstance(this)
                .publishTile(CUSTOM_TILE_ID, mCustomTile);
    }
}