package loki.com.androidglue;

import android.app.PendingIntent;
import android.content.Intent;

import com.loki.superglue.djinni.bluecast.BlueApplication;
import com.loki.superglue.djinni.bluecast.SuperGlueBlueCast;

public class AndroidGlueApp extends BlueApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        SuperGlueBlueCast.getBluetooth()
                .setOnBeaconNotificationTap(PendingIntent.getActivity(this, 0, intent, 0));
    }
}
