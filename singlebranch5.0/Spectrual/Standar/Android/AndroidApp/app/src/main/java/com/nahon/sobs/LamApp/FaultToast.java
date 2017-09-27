package com.nahon.sobs.LamApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import sps.system.SPDevSystem;

/**
 * Created by Administrator on 2017/3/27.
 */

public class FaultToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object toasklock = new Object();

    public static void makeText(final Context act, final String msg,
                                final int len) {
        SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (toasklock) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(act, msg, len);

                            toast.show();
                        }
                    }
                });

            }
        });
    }
}
