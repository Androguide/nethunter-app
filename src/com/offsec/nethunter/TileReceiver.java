
/**
 * Copyright (c) 2015, The CyanogenMod Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.offsec.nethunter;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

public class TileReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (AppNavHomeActivity.ACTION_TOGGLE_STATE.equals(intent.getAction())) {
            Intent newIntent = new Intent();
            newIntent.setAction(AppNavHomeActivity.ACTION_TOGGLE_STATE);
            String label = "Start Kali Shell";

            int state = getCurrentState(intent);

            switch (state) {
                case States.STATE_OFF:
                    newIntent.putExtra(AppNavHomeActivity.STATE, States.STATE_ON);
                    label = "Stop Kali Shell";
                    Intent termStart =
                            new Intent("jackpal.androidterm.RUN_SCRIPT");
                    termStart.addCategory(Intent.CATEGORY_DEFAULT);
                    termStart.putExtra("jackpal.androidterm.iInitialCommand", "su -c bootkali");
                    termStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(termStart);
                    break;
                case States.STATE_ON:
                    newIntent.putExtra(AppNavHomeActivity.STATE, States.STATE_OFF);
                    label = "Start Kali Shell";
                    Intent termStop =
                            new Intent("jackpal.androidterm.RUN_SCRIPT");
                    termStop.addCategory(Intent.CATEGORY_DEFAULT);
                    termStop.putExtra("jackpal.androidterm.iInitialCommand", "su -c killkali");
                    termStop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(termStop);
                    break;
            }

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, 0,
                            newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTile customTile = new CustomTile.Builder(context)
                    .setOnClickIntent(pendingIntent)
                    .setContentDescription("Start & Stop Kali Shell")
                    .setLabel(label)
                    .setIcon(R.drawable.ic_tile)
                    .build();

            CMStatusBarManager.getInstance(context)
                    .publishTile(AppNavHomeActivity.CUSTOM_TILE_ID, customTile);
        }
    }

    private int getCurrentState(Intent intent) {
        return intent.getIntExtra(AppNavHomeActivity.STATE, 0);
    }
}