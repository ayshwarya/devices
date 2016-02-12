package com.logitech.devices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ServerAPIInterface {

    private ServerAPI serverAPI = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ListAdapter listAdapter = null;
    private ArrayList<Device> deviceList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        deviceList = new ArrayList<Device>();
        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //Refresh animation does not work on onCreate. To make it work, we are using post runnable.
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        requestDeviceList();
                                    }
                                }
        );
    }

    /**
     * Requests server for the list of devices.
     */
    private void requestDeviceList() {
        swipeRefreshLayout.setRefreshing(true);
        serverAPI = new ServerAPI(this);
        serverAPI.executeRequest();
    }

    private void setListAdapter() {
        if (listAdapter == null) {
            listAdapter = new ListAdapter(this, deviceList);
            listView.setAdapter(listAdapter);
            swipeRefreshLayout.setOnRefreshListener(this);
        } else {
            listAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Adapter to display list data.
     */
    public class ListAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private List<Device> deviceList;


        public ListAdapter(Activity activity, List<Device> deviceList) {
            this.activity = activity;
            this.deviceList = deviceList;

        }

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null) {
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
            }

            TextView deviceNameText = (TextView) convertView.findViewById(R.id.deviceName);
            Device device = deviceList.get(position);
            deviceNameText.setText(device.getName());
            return convertView;
        }
    }

    /**
     * Notifies the adapter of the data change.
     */
    private void notifyListAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Notifies when server returns a valid response.
     *
     * @param deviceList - Arraylist containing list of devices to be loaded into listview.
     */
    @Override
    public void onSuccess(ArrayList<Device> deviceList) {
        this.deviceList.clear();
        this.deviceList.addAll(deviceList);

        DeviceLog.d(this.deviceList.toString());
        notifyListAdapter();
    }

    /**
     * Notifies when server fails to return a valid response.
     */
    @Override
    public void onFailure() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Requests server for device list when user refreshes the list.
     */
    @Override
    public void onRefresh() {
        requestDeviceList();
    }
}
