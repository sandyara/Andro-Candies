package ara.sandy.candies.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Santhosh on 07/01/2019.
 */

public class PreferencesUtil {

    public static void setUserDetails(Context context, long id ,String username, String mobile, String emailid,String accesstoken,String imgURL) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(Constant.USERID, id);
        editor.putString(Constant.USERNAME, username);
        editor.putString(Constant.MOBILE, mobile);
        editor.putString(Constant.EMAILID, emailid);
        editor.putString(Constant.ACCESSTOKEN, accesstoken);
        editor.putString(Constant.USERIMAGE, imgURL);

        editor.apply();
    }

    public static long getUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getLong(Constant.USERID, 0);
    }

    public static String getUsername(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getString(Constant.USERNAME, "");
    }

    public static String getMobile(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getString(Constant.MOBILE, "");
    }

    public static String getEmailId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getString(Constant.EMAILID, "");
    }

    public static String getAccessToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getString(Constant.ACCESSTOKEN, "");
    }

    public static String getUserImage(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        return pref.getString(Constant.USERIMAGE, "");
    }

    public static void clearUserDetails(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
    }

}

