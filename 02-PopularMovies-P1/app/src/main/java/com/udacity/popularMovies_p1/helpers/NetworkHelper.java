package com.udacity.popularMovies_p1.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    public static boolean checkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo  = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
        }
        return isConnected;
    }
}

