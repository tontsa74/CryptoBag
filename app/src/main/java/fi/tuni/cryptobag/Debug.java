package fi.tuni.cryptobag;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Debug {
    private static Context HOST;
    private static int DEBUG_LEVEL;
    private static boolean DEBUG_CONSOLE;
    private static boolean DEBUG_TOAST;

    static void loadDebug(Context host) {
        HOST = host;
        DEBUG_LEVEL = host.getResources().getInteger(R.integer.debug_level);
        DEBUG_CONSOLE = host.getResources().getBoolean(R.bool.debug_console);
        DEBUG_TOAST = host.getResources().getBoolean(R.bool.debug_toast);
    }

    static void print(String tag, String methodName, String msg, int level) {
        if (BuildConfig.DEBUG) {
            if (level <= DEBUG_LEVEL) {
                if (DEBUG_CONSOLE) {
                    Log.d(tag + ": " + methodName, msg);
                }
                if (DEBUG_TOAST) {
                    Context context = HOST.getApplicationContext();
                    CharSequence text = tag + ": " + methodName + ": " + msg;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }
        }
    }
}
