package loki.com.androidglue;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loki.superglue.djinni.jni.BlueBeacon;
import com.loki.superglue.djinni.jni.BlueDevice;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DeviceListAdaptar extends RecyclerView.Adapter<DeviceListAdaptar.DeviceViewHolder> {
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout layout;

        public DeviceViewHolder(@NonNull ConstraintLayout v) {
            super(v);

            layout = v;
        }
    }

    private List<BlueBeacon> dataset;
    private View.OnClickListener clickListener;

    public DeviceListAdaptar(@NonNull List<BlueBeacon> dataset, @NonNull View.OnClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    private int indexOf(BlueBeacon beacon) {
        return indexOf(beacon.getDevice());
    }

    private int indexOf(BlueDevice beacon) {
        for(int x = 0; x < dataset.size(); ++x) {
            if(beacon.getAddress().equals(dataset.get(x).getDevice().getAddress())) {
                return x;
            }
        }

        return -1;
    }

    public void deviceListUpdate(@NonNull BlueBeacon beacon) {
        int pos = indexOf(beacon);

        if(pos == -1) {
            dataset.add(beacon);

            pos = dataset.size() -1;
        }
        else {
            dataset.set(pos, beacon);
        }

        notifyItemChanged(pos);
    }

    public void deviceListRemove(@NonNull BlueDevice beacon) {
        int pos = indexOf(beacon);

        if(pos != -1) {
            dataset.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TextView textView = new TextView(parent.getContext());
        ConstraintLayout v = (ConstraintLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list, parent, false);

        return new DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int position) {
        deviceViewHolder.layout.setOnClickListener(clickListener);

        setDevice(deviceViewHolder, dataset.get(position));
    }

    private static void setDevice(DeviceViewHolder deviceViewHolder, BlueBeacon beacon) {
        BlueDevice device = beacon.getDevice();

        TextView bdaddr = (TextView)deviceViewHolder.layout.getViewById(R.id.bdaddr);
        TextView name   = (TextView)deviceViewHolder.layout.getViewById(R.id.name);

        TextView uuid          = (TextView)deviceViewHolder.layout.getViewById(R.id.uuid);
        TextView major_minor   = (TextView)deviceViewHolder.layout.getViewById(R.id.major_minor);
        TextView distance      = (TextView)deviceViewHolder.layout.getViewById(R.id.distance);

        bdaddr.setText(device.getAddress());
        name.setText(device.getName() == null ? "unknown" : device.getName());

        uuid.setText(beacon.getUuid());
        major_minor.setText(String.valueOf(beacon.getMajor()).concat("_").concat(String.valueOf(beacon.getMinor())));
        distance.setText(new DecimalFormat("#.00 meters").format(beacon.getDistance()));
    }

    public static BlueDevice getDevice(View v) {
        ConstraintLayout layout = (ConstraintLayout)v;

        TextView bdaddr = (TextView)layout.getViewById(R.id.bdaddr);
        TextView name   = (TextView)layout.getViewById(R.id.name);

        return new BlueDevice(name.getText().toString(), bdaddr.getText().toString());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
