package loki.com.androidglue;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.loki.superglue.djinni.bluecast.Bluetooth;
import com.loki.superglue.djinni.bluecast.SuperGlueBlueCast;
import com.loki.superglue.djinni.common.activity.CommonActivity;
import com.loki.superglue.djinni.jni.BlueBeacon;
import com.loki.superglue.djinni.jni.BlueDevice;
import com.loki.superglue.djinni.jni.BlueViewCallback;
import com.loki.superglue.djinni.jni.BlueViewController;

import java.util.ArrayList;

public class MainActivity extends CommonActivity {
    private BroadcastReceiver receiver;
    private BlueViewCallback blueView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Switch leScan = findViewById(R.id.LeScan);
        leScan.setOnCheckedChangeListener((CompoundButton compoundButton, boolean isChecked) -> {
            blueView.onToggleScan(isChecked);
        });

        RecyclerView recyclerView = findViewById(R.id.BeaconList);
        recyclerView.setAdapter(new DeviceListAdaptar(new ArrayList<>(), (View view) -> {
            BlueDevice blueDevice = DeviceListAdaptar.getDevice(view);

            blueView.onSelectDevice(blueDevice);
        }));

        blueView = SuperGlueBlueCast.getBluetooth().getBluetoothCallback().onCreate(new BlueViewController() {
            @Override
            public void blueEnable(boolean enable) {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }

            @Override
            public void beaconListUpdate(BlueBeacon beacon) {
                RecyclerView recyclerView = findViewById(R.id.BeaconList);

                DeviceListAdaptar deviceListAdaptar = (DeviceListAdaptar)recyclerView.getAdapter();
                runOnUiThread(() -> deviceListAdaptar.deviceListUpdate(beacon));
            }

            @Override
            public void beaconListRemove(BlueDevice beacon) {
                RecyclerView recyclerView = findViewById(R.id.BeaconList);

                DeviceListAdaptar deviceListAdaptar = (DeviceListAdaptar)recyclerView.getAdapter();
                runOnUiThread(() -> deviceListAdaptar.deviceListRemove(beacon));
            }
        }, getPermissionInterface());

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                blueView.onPowerStateChange(Bluetooth.fromPowerState(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)));
            }
        };

        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

//        DeviceListAdaptar deviceListAdaptar = (DeviceListAdaptar)recyclerView.getAdapter();

//        deviceListAdaptar.deviceListUpdate(new BlueBeacon(new BlueDevice("hello", "world"), "uniqueId", 1, 2, 1.0d));
//        deviceListAdaptar.deviceListUpdate(new BlueBeacon(new BlueDevice("dlrow", "olleh"), "dIeuqinu", 2, 1, 1.0d));
    }

    @Override
    protected void onDestroy() {
        SuperGlueBlueCast.getBluetooth().getBluetoothCallback().onDestroy();

        unregisterReceiver(receiver);

        super.onDestroy();
    }
}
