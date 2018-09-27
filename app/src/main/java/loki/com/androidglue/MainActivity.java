package loki.com.androidglue;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.loki.superglue.djinni.bluecast.SuperGlueBlueCast;
import com.loki.superglue.djinni.common.activity.CommonActivity;
import com.loki.superglue.djinni.jni.BlueCallback;
import com.loki.superglue.djinni.jni.BlueDevice;
import com.loki.superglue.djinni.jni.BlueViewMainController;

import java.util.ArrayList;

public class MainActivity extends CommonActivity {
    private BlueCallback blueCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        blueCallback = SuperGlueBlueCast.getBluetooth().getBluetoothCallback();

        Switch leScan = findViewById(R.id.LeScan);
        leScan.setOnCheckedChangeListener((CompoundButton compoundButton, boolean isChecked) -> {
            blueCallback.onBeaconScanEnable(isChecked);
        });

        RecyclerView recyclerView = findViewById(R.id.BeaconList);
        recyclerView.setAdapter(new DeviceListAdaptar(new ArrayList<>(), (View view) -> {
            blueCallback.onSelectDevice(DeviceListAdaptar.getDevice(view));
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();

        blueCallback.onStartMain(new BlueViewMainController() {
            @Override
            public void blueEnable(boolean enable) {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }

            @Override
            public void setDeviceList(ArrayList<BlueDevice> devices) {
                RecyclerView recyclerView = findViewById(R.id.BeaconList);

                DeviceListAdaptar deviceListAdaptar = (DeviceListAdaptar)recyclerView.getAdapter();
                runOnUiThread(() -> deviceListAdaptar.setDataset(devices));
            }

            @Override
            public void launchViewDisplay(BlueDevice device) {
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);

                intent.putExtra(DisplayActivity.DEVICE_ADDRESS_EXTRA, device.getAddress());
                intent.putExtra(DisplayActivity.DEVICE_NAME_EXTRA, device.getName());

                runOnUiThread(() -> startActivity(intent));
            }
        }, getPermissionInterface());
    }

    @Override
    protected void onStop() {
        blueCallback.onStopMain();

        super.onStop();
    }
}
