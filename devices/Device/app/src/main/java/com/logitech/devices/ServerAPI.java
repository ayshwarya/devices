package com.logitech.devices;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by ayshwarya on 11/02/16.
 */
public class ServerAPI {

    private ServerAPIInterface serverAPIInterface = null;
    private DeviceParser deviceParser = null;

    public ServerAPI(ServerAPIInterface serverAPIInterface) {
        this.serverAPIInterface = serverAPIInterface;
        deviceParser = new DeviceParser();
    }

    /**
     * Requests server for the list of devices. On Success, parses the json response and returns the list to the listener
     * and on failure notifies the listener.
     */
    protected void executeRequest() {
        JobRunner.instance.submit(new Runnable() {
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    java.net.URL requestUrl = new java.net.URL(DeviceConstants.DEVICE_URL);
                    urlConnection = (HttpURLConnection) requestUrl
                            .openConnection();
                    urlConnection.connect();
                    urlConnection.setReadTimeout(DeviceConstants.REQUEST_TIMEOUT);
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection
                                .getInputStream());
                        processResponse(in);
                    } else {
                        DeviceLog.e("Server error: unexpected server status of %d" +
                                responseCode);
                        serverAPIInterface.onFailure();
                    }

                } catch (IOException e) {
                    DeviceLog.e(e, "Error loading %s" + DeviceConstants.DEVICE_URL);
                    DeviceLog.e(e, "AdServer error");
                    serverAPIInterface.onFailure();
                }
            }
        });
    }

    /**
     * Parses the response and notifies the listener.
     *
     * @param inputStream to be parsed.
     */
    private void processResponse(InputStream inputStream) {
        ArrayList<Device> deviceList = deviceParser.parse(inputStream);
        serverAPIInterface.onSuccess(deviceList);
    }
}
