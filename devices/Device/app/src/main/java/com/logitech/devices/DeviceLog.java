package com.logitech.devices;

import android.util.Log;

/**
 * Created by ayshwarya on 11/02/16.
 */
public class DeviceLog {

    public static void d(String msg) {

        Log.d(DeviceConstants.TAG, msg);

    }

    public static void e(Throwable t, String msg) {

        Log.e(DeviceConstants.TAG, msg, t);

    }

    public static void e(String msg) {

        Log.e(DeviceConstants.TAG, msg);

    }
}

