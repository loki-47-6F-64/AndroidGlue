package loki.com.androidglue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.loki.superglue.djinni.bluecast.SuperGlueBlueCast;
import com.loki.superglue.djinni.jni.BlueCallback;
import com.loki.superglue.djinni.jni.BlueDevice;
import com.loki.superglue.djinni.jni.BlueViewDisplayController;

public class DisplayActivity extends AppCompatActivity {
    public static final String DEVICE_ADDRESS_EXTRA = "androidglue.DEVICE_ADDRESS_EXTRA";
    public static final String DEVICE_NAME_EXTRA = "androidglue.DEVICE_NAME_EXTRA";

    private BlueCallback blueCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        String name    = intent.getStringExtra(DEVICE_NAME_EXTRA);
        String address = intent.getStringExtra(DEVICE_ADDRESS_EXTRA);


        blueCallback = SuperGlueBlueCast.getBluetooth().getBluetoothCallback();
        blueCallback.onCreateDisplay(
                new BlueDevice(name, address),
                new BlueViewDisplayController() {
                    @Override
                    public void display(BlueDevice device, String info) {
                        runOnUiThread(() -> {
                            TextView viewInfo = findViewById(R.id.displayInfo);
                            TextView viewName = findViewById(R.id.displayName);
                            TextView viewAddress = findViewById(R.id.displayRemoteBDAddr);

                            viewAddress.setText(device.getAddress());
                            viewName.setText(device.getName() != null ? device.getName() : "unknown");
                            viewInfo.setText(info);
                        });
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        blueCallback.onDestroyDisplay();

        super.onDestroy();
    }
}
