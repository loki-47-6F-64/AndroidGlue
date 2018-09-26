package loki.com.androidglue;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loki.superglue.djinni.jni.BlueDevice;

import java.text.DecimalFormat;
import java.util.List;

public class DeviceListAdaptar extends RecyclerView.Adapter<DeviceListAdaptar.BeaconViewHolder> {
    public static class BeaconViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout layout;

        public BeaconViewHolder(@NonNull ConstraintLayout v) {
            super(v);

            layout = v;
        }
    }

    private List<BlueDevice> dataset;
    private View.OnClickListener clickListener;

    public DeviceListAdaptar(@NonNull List<BlueDevice> dataset, @NonNull View.OnClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    public void setDataset(@NonNull List<BlueDevice> dataset) {
        this.dataset = dataset;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeaconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TextView textView = new TextView(parent.getContext());
        ConstraintLayout v = (ConstraintLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list, parent, false);

        return new BeaconViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BeaconViewHolder beaconViewHolder, int position) {
        beaconViewHolder.layout.setOnClickListener(clickListener);

        setDevice(beaconViewHolder, dataset.get(position));
    }

    public static BlueDevice getDevice(@NonNull View view) {
        ConstraintLayout layout = (ConstraintLayout)view;

        TextView address = (TextView) layout.getViewById(R.id.device_address);
        TextView name    = (TextView) layout.getViewById(R.id.device_name);

        return new BlueDevice(name.getText().toString(), address.getText().toString());
    }

    private static void setDevice(BeaconViewHolder beaconViewHolder, BlueDevice device) {
        TextView address = (TextView) beaconViewHolder.layout.getViewById(R.id.device_address);
        TextView name    = (TextView) beaconViewHolder.layout.getViewById(R.id.device_name);

        address.setText(device.getAddress());
        name.setText(device.getName());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
