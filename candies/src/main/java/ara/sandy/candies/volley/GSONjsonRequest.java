package ara.sandy.candies.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Santhosh on 07/01/2019
 */
public class GSONjsonRequest<T> extends Request<T> {
    private Object dataIn;

    private Gson mGson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */


    /**
     * Make a POST request and return a parsed object from JSON.
     *  @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param listener
     */

    public GSONjsonRequest(int method, String url, Object dataIn, Class<T> clazz, Map<String, String> headers,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.dataIn = dataIn;
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        mGson = new Gson();

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public String getStringParams(){
        Gson gson = new Gson();
        return gson.toJson(dataIn);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Log.d("the json", mGson.toJson(dataIn));
        return mGson.toJson(dataIn).getBytes();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        Log.d("response", response + "");
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            Log.d("response", json);

            return Response.success(mGson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}