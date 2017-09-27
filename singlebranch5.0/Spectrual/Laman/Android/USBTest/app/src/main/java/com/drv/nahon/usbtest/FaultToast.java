package com.drv.nahon.usbtest;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Administrator on 2017/3/27.
 */

public class FaultToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object toasklock = new Object();
    private static ExecutorService threadpools = Executors.newCachedThreadPool();

    public static void makeText(final Context act, final String msg,
                                final int len) {
        threadpools.submit(new Runnable() {
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
