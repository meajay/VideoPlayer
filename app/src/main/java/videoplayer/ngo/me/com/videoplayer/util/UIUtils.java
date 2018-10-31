package videoplayer.ngo.me.com.videoplayer.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ajay on 31-10-2018.
 */
public class UIUtils {
    private static ProgressDialog dialog;
    public static void showProgreeBar(Context context,boolean show) {
        if (show) {
            dialog = ProgressDialog.show(context, "",
                    "Buffering...", true);
        } else {
            dialog.dismiss();
        }
    }
}
