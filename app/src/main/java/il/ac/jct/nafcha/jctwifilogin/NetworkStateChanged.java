package il.ac.jct.nafcha.jctwifilogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.net.wifi.WifiManager;

public class NetworkStateChanged extends BroadcastReceiver {

    static final String TAG = "NetworkStateChanged";
    static final String[] SSID = {"JCT - Lev", "JCT - Tal"};

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        // Check network connected
        NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        Log.d(TAG, "onReceive: netInfo = " + netInfo);
        if (!netInfo.isConnected()) {
            return;
        }

        // Check SSID
        String ssid;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            ssid = wifi.getConnectionInfo().getSSID();
        }
        catch (NullPointerException e) {
            // So many things can be null here when network is not connected
            Log.d(TAG, "Exception getting SSID", e);
            ssid = null;
        }
        Log.d(TAG, "ssid = " + ssid);
        boolean validSsid = false;
        for (String checkSsid : SSID) {
            if (checkSsid.equalsIgnoreCase(ssid) || ("\"" + checkSsid + "\"").equalsIgnoreCase(ssid)) {
                // JB 4.2 puts quote around SSID
                validSsid = true;
                break;
            }
        }
        if (!validSsid) {
            Log.d(TAG, "Invalid SSID");
            return;
        }

        Log.v(TAG, "Connected to the correct network");

        Intent i = new Intent(context, JctWifiLogin.class);
        context.startService(i);
    }

}