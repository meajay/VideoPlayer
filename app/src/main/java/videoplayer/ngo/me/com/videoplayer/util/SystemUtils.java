package videoplayer.ngo.me.com.videoplayer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ajay on 31-10-2018.
 */
public class SystemUtils {

    private static ConnectivityManager connectivityManager = null;

    public static boolean isNetworkAvailable(Context context) {
        connectivityManager = connectivityManager != null ? connectivityManager :
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean status = activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
        return status;
    }
}
