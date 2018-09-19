package loki.com.androidglue;

import android.app.Application;

import com.loki.superglue.djinni.bluecast.Bluetooth;
import com.loki.superglue.djinni.bluecast.SuperGlueBlueCast;

public class AndroidGlueApp extends Application {
    private static String TAG = "loki.com.androidglue.AndroidGlueApp";

    @Override
    public void onCreate() {
        super.onCreate();

        SuperGlueBlueCast.init(this);
    }
}
