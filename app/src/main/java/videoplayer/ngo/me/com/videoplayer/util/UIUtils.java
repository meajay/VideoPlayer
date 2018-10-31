package videoplayer.ngo.me.com.videoplayer.util;


import android.content.Context;
import android.widget.Toast;

import videoplayer.ngo.me.com.videoplayer.R;

/**
 * Created by Ajay on 31-10-2018.
 */
public class UIUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message , Toast.LENGTH_LONG).show();
    }
//    private static ProgressBar progressBar{
////     public static void showProgreeBar(Context context,boolean show) {
////    }
}
