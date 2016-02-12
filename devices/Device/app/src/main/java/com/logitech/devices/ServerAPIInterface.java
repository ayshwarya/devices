package com.logitech.devices;

import java.util.ArrayList;

/**
 * Created by ayshwarya on 11/02/16.
 */

/**
 * Notifies the listener when request is success/failure.
 */
public interface ServerAPIInterface {

    public void onSuccess(ArrayList<Device> deviceList);
    public void onFailure();

}
