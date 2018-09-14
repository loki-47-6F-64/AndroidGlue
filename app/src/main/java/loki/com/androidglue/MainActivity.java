package loki.com.androidglue;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loki.superglue.djinni.bluecast.Bluetooth;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "loki.com.androidglue.MainActivity";

    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = new Bluetooth(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        bluetooth.getPermImpl().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
