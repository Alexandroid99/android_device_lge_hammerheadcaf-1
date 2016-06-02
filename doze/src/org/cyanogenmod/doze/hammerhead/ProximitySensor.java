/*
 * Copyright (c) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.doze.hammerhead;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.util.Log;

public class ProximitySensor extends HammerheadSensor {

    private static final boolean DEBUG = true;
    private static final String TAG = "ProximitySensor";

    private static final int MIN_PULSE_INTERVAL_MS = 2500;

    private boolean mNear = false;
    private long mEntryTimestamp;

    public ProximitySensor(Context context) {
        super(context, Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void enable() {
        if (DEBUG) Log.d(TAG, "Enabling");
        super.enable();
        mEntryTimestamp = SystemClock.elapsedRealtime();
    }

    @Override
    public void disable() {
        if (DEBUG) Log.d(TAG, "Disabling");
        super.disable();
    }

    @Override
    protected void onSensorEvent(SensorEvent event) {
        boolean isNear = event.values[0] < mSensor.getMaximumRange();
        String result = isNear ? "near" : "far";
        if (DEBUG) Log.d(TAG, "Got sensor event: " + result);

        //long delta = SystemClock.elapsedRealtime() - mEntryTimestamp;
        //if (delta < MIN_PULSE_INTERVAL_MS) {
        //    return;
        // else {
            if (mNear && !isNear) {
                launchDozePulse();
            } else {
                mEntryTimestamp = SystemClock.elapsedRealtime();
            }
        //}
        mNear = isNear;
    }
}
