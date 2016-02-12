package com.logitech.devices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ayshwarya on 12/02/16.
 */
public class DeviceParser {

    /**
     * Parses the json response and returns to list of devices.
     *
     * @param inputStream response from server.
     * @return list of devices
     */
    protected ArrayList<Device> parse(InputStream inputStream){
        ArrayList<Device> deviceList = new ArrayList<Device>();
        String jsonResponse = convertStreamToString(inputStream);
        try {
            JSONObject resopnseObj = new JSONObject(jsonResponse);
            JSONArray devicesArray = resopnseObj.getJSONArray(DeviceConstants.RESPONSE_KEY_DEVICES);
            for (int count = 0; count < devicesArray.length(); count++) {
                JSONObject deviceObj = devicesArray.getJSONObject(count);
                Device device = new Device();
                device.setName(deviceObj.getString(DeviceConstants.RESPONSE_KEY_NAME));
                device.setModel(deviceObj.getString(DeviceConstants.RESPONSE_KEY_MODEL));
                device.setDeviceType(deviceObj.getString(DeviceConstants.RESPONSE_KEY_DEVICE_TYPE));
                deviceList.add(device);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceList;
    }

    /**
     * Converts InputStream to String
     * @param is  response from server
     * @return String
     */
    private String convertStreamToString(java.io.InputStream is) {
        try {
            InputStreamReader inReader = new InputStreamReader(is);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(inReader);
            String read;
            try {
                read = br.readLine();
                while (read != null) {
                    sb.append(read);
                    read = br.readLine();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        } catch (java.util.NoSuchElementException e) {
            DeviceLog.e(e," ==================convertStreamToString ======= " + is);
            return "";
        }
    }
}
