package videoplayer.ngo.me.com.videoplayer.util;


import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ajay on 31-10-2018.
 */
public class UIUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
