package loki.com.androidglue;

import android.app.Application;

import com.loki.superglue.djinni.jit.SuperGlueJIT;

public class AndroidGlueApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SuperGlueJIT.init(this);
    }
}
