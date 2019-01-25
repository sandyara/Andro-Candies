package ara.sandy.candies.volley;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ara.sandy.candies.utils.Utility;

import static ara.sandy.candies.background.NetworkReceiver.isNetworkAvailable;
import static com.android.volley.Request.Method.POST;

public class AraRequestQueue {

    private static RequestQueue mRequestQueue;

    /**
     * Create Request Queue if null
     *
     * @param context
     * @return mRequestQueue
     */
    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    /**
     * Adding Request to mRequestQueue
     *
     * @param context
     * @param req
     * @param tag
     * @param <T>
     */
    public static <T> void add(Context context, Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? ((Activity) context).getClass().getSimpleName() : tag);
        getRequestQueue(context).add(req);
    }

    public static <T> void addInBackground(Context context, Request<T> req, int method){

        if (method != POST){
            Utility.showMessage((Activity) context,"Invalid Method");
            return;
        }

        if (isNetworkAvailable(context)){
            req.setTag((Activity) context).getClass().getSimpleName();
            getRequestQueue(context).add(req);
        } else {
            setOfflineRequest(context,req);
            Toast.makeText(context, "No Internet connection!!! Data stored in offline", Toast.LENGTH_SHORT).show();
        }
    }


    public static void saveOffline(Context context){
        Toast.makeText(context, "Storing offline data...", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        String json = pref.getString("REQUEST", "");
        Type type = new TypeToken<ArrayList<Request>>(){}.getType();

        Gson gson = new Gson();
        ArrayList<Request> offlineRequest = gson.fromJson(json, type);

        for(Request req: offlineRequest){
            getRequestQueue(context).add(req);
            offlineRequest.remove(req);
        }

        SharedPreferences sharedpreferences = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("REQUEST", gson.toJson(offlineRequest));
        editor.apply();
    }


    public static <T> void setOfflineRequest(Context context,Request<T> request){
        SharedPreferences pref = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        String json = pref.getString("REQUEST", "");
        Type type = new TypeToken<ArrayList<Request>>(){}.getType();

        Gson gson = new Gson();
        ArrayList<Request> offlineRequest = gson.fromJson(json, type);

        SharedPreferences sharedpreferences = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        offlineRequest.add(request);
        editor.putString("REQUEST", gson.toJson(offlineRequest));

        editor.apply();
    }

}
