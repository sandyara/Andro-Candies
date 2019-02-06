package ara.sandy.candies.background;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ara.sandy.candies.R;
import ara.sandy.candies.volley.AraRequestQueue;

public class NetworkReceiver extends BroadcastReceiver {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    boolean isCancelable = true;
    int themeResId = 0;

    Dialog dialog;

    public NetworkReceiver() {
        super();
    }

    public NetworkReceiver(boolean isCancelable, @StyleRes int themeResId) {
        super();
        this.isCancelable = isCancelable;
        this.themeResId = themeResId;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = getConnectivityStatusString(context);

        if (status.equalsIgnoreCase("Not connected to Internet")){
            dialog = themeResId == 0 ? new Dialog((Activity) context) : new Dialog((Activity) context,themeResId);
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_network,null);
            dialog.setContentView(view);
            Button btnClose = view.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            if (isCancelable) btnClose.setVisibility(View.VISIBLE);

            dialog.setCancelable(false);
            dialog.show();
        } else {
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }

            AraRequestQueue.saveOffline((Activity) context);
        }
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static boolean isNetworkAvailable(Context context){
        if (getConnectivityStatus(context) == TYPE_NOT_CONNECTED)
            return false;

        return true;
    }

}
