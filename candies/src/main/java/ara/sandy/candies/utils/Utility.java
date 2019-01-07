package ara.sandy.candies.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ara.sandy.candies.R;


/**Utils
 * this class contains mostly using methods it can access from any files.
 */
public class Utility {

    // load image to ImageView using Picasso
    public static void loadImage(Context context, final ImageView img, final String imgPath){

        try {

            Picasso.with(context).load(imgPath).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    img.setTag(imgPath);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onError() {
                    img.setImageResource(R.drawable.ic_no_photos);
                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });

        }catch (Exception ex){
            Log.e("LOAD IMAGE ERROR",ex.getMessage());
        }

    }

    /**set date to TextView
     * @param cxt Context
     * @param format -- Custom Date Format
     * @param txtDate -- TextView to Set Date
      */
    public static void setDate(final TextView txtDate, final Activity cxt, final String format){

        DatePickerDialog datePickerDialog;
        try{

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(cxt,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
                    Date convdate=new Date();
                    Date date=null;

                    try {
                        date=dateFormat.parse((dayOfMonth)+"-"+(monthOfYear + 1)+"-"+year);
                        // time=new Date(aetime);
                        String newdate = new SimpleDateFormat(format).format(date);
                        txtDate.setText(newdate);

                    } catch (ParseException e) {
                        Toast.makeText(cxt,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }catch (Exception ex){
            Log.d("DATE_PICKER",ex.getMessage());
        }
    }

    public static void setDate(final TextView txtDate, final Activity cxt, final String format, String minDate){

        DatePickerDialog datePickerDialog;
        try{

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(cxt,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
                    Date convdate = new Date();
                    Date date=null;

                    try {
                        date=dateFormat.parse((dayOfMonth)+"-"+(monthOfYear + 1)+"-"+year);
                        // time=new Date(aetime);
                        String newdate = new SimpleDateFormat(format).format(date);
                        txtDate.setText(newdate);

                    } catch (ParseException e) {
                        Toast.makeText(cxt,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }, mYear, mMonth, mDay);

            Calendar minCalender = Calendar.getInstance();

            int minMonth = Integer.valueOf(changeDateFormat(minDate,"dd.MM.yyyy","MM")) - 1;
            int minYear = Integer.valueOf(changeDateFormat(minDate,"dd.MM.yyyy","yyyy"));
            int minDay = Integer.valueOf(changeDateFormat(minDate,"dd.MM.yyyy","dd"));

            if ((mDay - minDay) >= 7){
                minDay = mDay - 7;
            }

            minCalender.set(Calendar.MONTH,minMonth);
            minCalender.set(Calendar.YEAR,minYear);
            minCalender.set(Calendar.DAY_OF_MONTH,minDay);

            datePickerDialog.getDatePicker().setMinDate(minCalender.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            datePickerDialog.show();
        }catch (Exception ex){
            Log.d("DATE_PICKER",ex.getMessage());
        }

    }

    /**load spinner
     * @param context Context
     * @param spn -- Spinner object to load data
     * @param spnList -- list of data to load
     */
    public static void loadSpinner(Context context, Spinner spn,List<SpinnerObject> spnList){

        try{

            ArrayAdapter dataAdapter = new ArrayAdapter<SpinnerObject>(context, R.layout.simple_spinner_item, spnList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn.setAdapter(dataAdapter);
            spn.setSelection(0);

        }catch(Exception ex){
            Log.d("SPINNER ERROR",ex.getMessage());
        }

    }

    /**load spinner
     * @param context Context
     * @param spn -- Spinner object to load data
     * @param spnList -- list of data to load
     * @param position -- set selection
     */
    public static void loadSpinner(Context context, Spinner spn,List<SpinnerObject> spnList,int position){

        try{
            ArrayAdapter dataAdapter = new ArrayAdapter<SpinnerObject>(context, R.layout.simple_spinner_item, spnList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn.setAdapter(dataAdapter);
            spn.setSelection(position);

        }catch(Exception ex){
            Log.d("SPINNER ERROR",ex.getMessage());
        }

    }

    /**Show Alert Dialog Box
     *
     * @param context
     * @param message
     */
    public static void showMessage(Context context, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Show Alert Dialog Box
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showMessage(Context context, String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String getCurrentDateTime(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(c);
    }

    public static String getCurrentDateTime(String format){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c);
    }

    /**Change String to String Date Format
     *
     * @param text
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public static String changeDateFormat(String text,String inputFormat,String outputFormat){
        try {
            SimpleDateFormat input = new SimpleDateFormat(inputFormat);
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);

            return  dateFormat.format(input.parse(text));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void chooseFromGallery(Activity activity, int REQUEST_IMAGE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_IMAGE);
        }
    }

    public static void captureImage(Activity activity, int REQUEST_TAKE_PHOTO) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    /** Display Images on Dialog
     *
     * @param mContext
     * @param path
     */
    public static void showImage(Activity mContext,String path) {

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.lay_show_image);
        Bitmap bitmap = getBitmapFromPath(path);

        AppCompatImageView image = dialog.findViewById(R.id.image);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        image.setImageBitmap(bitmap);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Bitmap getBitmapFromPath(String path){

        if(path.contains("http")){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch (IOException e) {
                Log.e("Url Exception", e.getMessage());
                return null;
            }

        } else {
            File image = new File(path);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            return BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        }

    }

    public static byte[] getByteFromBitmap(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();

        return byteArray;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    /** Save Bitmap to Local Path
     *
     * @param context
     * @param bmp
     * @return FileName and Path
     */
    public static String saveBitmap(Context context, Bitmap bmp) {
        String _time = "";
        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        _time = "image_" + hourofday + "" + minute + "" + second + ""
                + millisecond + ".png";
        String file_path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + context.getResources().getString(R.string.app_name);
        try {
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, _time);
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.e("error in saving image", e.getMessage());
        }
        Log.d("FILE",file_path);

        return file_path + "/" + _time;
    }
}
