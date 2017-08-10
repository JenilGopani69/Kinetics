package proj.kinetics;

import android.app.Application;
import android.view.ViewGroup;

import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;

/**
 * Created by sai on 2/8/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener,ViewGroup container) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
