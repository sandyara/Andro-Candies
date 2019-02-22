package ara.sandy.candies.volley;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ara.sandy.candies.utils.Result;
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

    /**
     * if (noInternet) Request will store in Offline
     * else if (Internet) Request add to mRequestQueue
     *
     * @param context
     * @param req
     * @param <T>
     */
    public static <T> void addInBackground(Context context, Request<T> req){

        if (req.getMethod() != POST){
            Utility.showMessage((Activity) context,"Invalid Method for Offline Storage\n(Use POST method)");
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


    /**
     * Execute Request and Save Offline to Cloud
     * @param context
     */
    public static void saveOffline(final Context context){
        ArrayList<AraRequestModal> offlineRequest = getOfflineRequest(context);

        if (offlineRequest == null){
            return;
        } else if (!offlineRequest.isEmpty()){
            Toast.makeText(context, "Storing offline data...", Toast.LENGTH_SHORT).show();
        }
        Gson gson = new Gson();

        for(final AraRequestModal req: offlineRequest){
            Map<String,String> params = new HashMap<>();
            params = (Map<String, String>) gson.fromJson(req.getParams(),params.getClass());

            GSONjsonRequest gsoNjsonRequest = new GSONjsonRequest(POST,
                    req.getUrl(),
                    params,
                    Result.class,
                    req.getHeader(),
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            getOfflineRequest(context).remove(req);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            getRequestQueue(context).add(gsoNjsonRequest);
        }

        SharedPreferences sharedpreferences = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("REQUEST", gson.toJson(offlineRequest));
        editor.apply();
    }

    /**
     * Save Request in Offline
     *
     * @param context
     * @param request
     * @param <T>
     */
    public static <T> void setOfflineRequest(Context context,Request<T> request){
        ArrayList<AraRequestModal> offlineRequest = getOfflineRequest(context);

        Gson gson = new Gson();

        SharedPreferences sharedpreferences = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (offlineRequest == null) offlineRequest = new ArrayList<>();

        try {
            AraRequestModal requestModal = new AraRequestModal();

            if (request instanceof GSONjsonRequest){
                GSONjsonRequest gsonRequest = (GSONjsonRequest) request;

                requestModal.setHeader(gsonRequest.getHeaders());
                requestModal.setUrl(gsonRequest.getUrl());
                requestModal.setParams(gsonRequest.getStringParams());

            } else if (request instanceof GSONMultiPartRequest){
                GSONMultiPartRequest gsonRequest = (GSONMultiPartRequest) request;

                requestModal.setHeader(gsonRequest.getHeaders());
                requestModal.setUrl(gsonRequest.getUrl());
                requestModal.setParams(gsonRequest.getStringParams());

            } else if (request instanceof GSONDateRequest){
                GSONDateRequest gsonRequest = (GSONDateRequest) request;

                requestModal.setHeader(gsonRequest.getHeaders());
                requestModal.setUrl(gsonRequest.getUrl());
                requestModal.setParams(gsonRequest.getStringParams());

            } else if (request instanceof GSONRequest){
                GSONRequest gsonRequest = (GSONRequest) request;

                requestModal.setHeader(gsonRequest.getHeaders());
                requestModal.setUrl(gsonRequest.getUrl());
                requestModal.setParams(gsonRequest.getStringParams());
            }

            offlineRequest.add(requestModal);
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        editor.putString("REQUEST", gson.toJson(offlineRequest));

        editor.apply();
    }

    /**
     * Get Request Stored on Offline
     *
     * @param context
     * @return
     */
    public static ArrayList<AraRequestModal> getOfflineRequest(Context context){
        SharedPreferences pref = context.getSharedPreferences("OFFLINE_DATA", Context.MODE_PRIVATE);
        String json = pref.getString("REQUEST", "");
        Type type = new TypeToken<ArrayList<AraRequestModal>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

}
