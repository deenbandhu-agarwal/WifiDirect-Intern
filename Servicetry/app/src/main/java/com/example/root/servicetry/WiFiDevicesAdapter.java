package com.example.root.servicetry;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 7/2/15.
 */
public class WiFiDevicesAdapter extends ArrayAdapter<WifiP2pDevice> {

    List<WifiP2pDevice> items;
    private final Context context;
    public WiFiDevicesAdapter(Context context,int textViewResourceID, List<WifiP2pDevice> objects)
    {
        super(context, textViewResourceID, objects);
        this.context = context;
        items = objects;

    }
    @Override
    public View getView(int Position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_devices, null);
        }
        WifiP2pDevice device = items.get(Position);
        if (device != null) {
            TextView top = (TextView) v.findViewById(R.id.device_name);
            TextView bottom = (TextView) v.findViewById(R.id.device_details);
            if (top != null) {
                top.setText(device.deviceName);
            }
            if (bottom != null) {
                bottom.setText(MainActivity.getDeviceStatus(device.status));
            }
        }

        return v;

    }
}