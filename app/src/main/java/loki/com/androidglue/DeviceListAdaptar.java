package loki.com.androidglue;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loki.superglue.djinni.jni.BlueBeacon;

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

    private List<BlueBeacon> dataset;
    private View.OnClickListener clickListener;

    public DeviceListAdaptar(@NonNull List<BlueBeacon> dataset, @NonNull View.OnClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    private int indexOf(BlueBeacon beacon) {
        for(int x = 0; x < dataset.size(); ++x) {
            if(beacon.getUuid().equals(dataset.get(x).getUuid())) {
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

    public void deviceListRemove(@NonNull BlueBeacon beacon) {
        int pos = indexOf(beacon);

        if(pos != -1) {
            dataset.remove(pos);
            notifyItemRemoved(pos);
        }
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

        setBeacon(beaconViewHolder, dataset.get(position));
    }

    private static void setBeacon(BeaconViewHolder beaconViewHolder, BlueBeacon beacon) {
        TextView uuid          = (TextView) beaconViewHolder.layout.getViewById(R.id.uuid);
        TextView major_minor   = (TextView) beaconViewHolder.layout.getViewById(R.id.major_minor);
        TextView distance      = (TextView) beaconViewHolder.layout.getViewById(R.id.distance);

        uuid.setText(beacon.getUuid());
        major_minor.setText(String.valueOf(beacon.getMajor()).concat("_").concat(String.valueOf(beacon.getMinor())));
        distance.setText(new DecimalFormat("#.00 meters").format(beacon.getDistance()));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
