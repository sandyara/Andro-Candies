package ara.sandy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import ara.sandy.candies.background.NetworkReceiver;
import ara.sandy.candies.volley.AraRequestQueue;
import ara.sandy.candies.volley.GSONjsonRequest;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {

    Activity mContext = this;
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.imgPin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) img.getDrawable();
                avd.registerAnimationCallback(new Animatable2.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        avd.start();
                    }
                });
            avd.start();
        }

        NetworkReceiver br = new NetworkReceiver(true,R.style.AppTheme);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(br, intentFilter);


        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {

        Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();

        Map<String,String> params = new HashMap<>();
        params.put("fname","Test");
        params.put("name","testing");
        params.put("description","testing");

        String url = "http://growthmoon.com/api/service_main.php";

        Map<String,String> header = new HashMap<String,String>();
        header.put("Accept","application/json");

        GSONjsonRequest gsoNjsonRequest = new GSONjsonRequest
                (POST, url,params,
                        Result.class, header,
                        submitSuccessListener(),
                        submitErrorListener());

        AraRequestQueue.addInBackground(mContext,gsoNjsonRequest);

    }

    private Response.Listener submitSuccessListener() {
        return new Response.Listener<Result>() {
            @Override
            public void onResponse(final Result response) {
                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Response.ErrorListener submitErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

}

