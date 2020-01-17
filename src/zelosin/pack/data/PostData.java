package zelosin.pack.data;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ini4j.Ini;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PostData {
    private static Ini mReader;
    private static Ini mWriter;

    private static final String INI_PATH = "src/zelosin/pack/inifiles/setting.ini";

    private static final String mSpFoo = "#SPPoster";
    private static String mUnicToken = "";
    private static double mAPIVersion;
    private static int mGroupId;
    private static int mAppId;
    private static int mOwnAppId;

    private static boolean UseProxy;

    private static List<NameValuePair> mParamsForSave = new ArrayList<NameValuePair>();
    private static List<NameValuePair> mParamsForPost = new ArrayList<NameValuePair>();

    private static void initializeIni(){
        mReader = new Ini();
        mWriter = new Ini();
    }
    static{
        initializeIni();
        if(new File(INI_PATH).exists())
            loader.loadData();
        else
            loader.initializeData();

        mParamsForSave.add(new BasicNameValuePair("access_token", mUnicToken));
        mParamsForSave.add(new BasicNameValuePair("server", ""));
        mParamsForSave.add(new BasicNameValuePair("photo", ""));
        mParamsForSave.add(new BasicNameValuePair("hash", ""));
        mParamsForSave.add(new BasicNameValuePair("group_id", String.valueOf(mGroupId)));
        mParamsForSave.add(new BasicNameValuePair("v", String.valueOf(mAPIVersion)));

        mParamsForPost.add(new BasicNameValuePair("access_token", mUnicToken));
        mParamsForPost.add(new BasicNameValuePair("owner_id", String.valueOf(-mGroupId)));
        mParamsForPost.add(new BasicNameValuePair("from_group", "1"));
        mParamsForPost.add(new BasicNameValuePair("message", mSpFoo));
        mParamsForPost.add(new BasicNameValuePair("attachments", ""));
        mParamsForPost.add(new BasicNameValuePair("signed", "0"));
        mParamsForPost.add(new BasicNameValuePair("v", String.valueOf(mAPIVersion)));

        UseProxy = new Boolean(false);
    }

    public static boolean isUseProxy() {
        return UseProxy;
    }

    public static String getmSpFoo() {
        return mSpFoo;
    }

    public static List<NameValuePair> getmParamsForSave() {
        return mParamsForSave;
    }

    public static List<NameValuePair> getmParamsForPost() {
        return mParamsForPost;
    }

    public static String getmUnicToken() {
        return mUnicToken;
    }

    public static void setmUnicToken(String mUnicToken) {
        PostData.mUnicToken = mUnicToken;
    }

    public static double getmAPIVersion() {
        return mAPIVersion;
    }

    public static void setmAPIVersion(double mAPIVersion) {
        PostData.mAPIVersion = mAPIVersion;
    }

    public static int getmGroupId() {
        return mGroupId;
    }

    public static void setmGroupId(int mGroupId) {
        PostData.mGroupId = mGroupId;
    }

    public static int getmAppId() {
        return mAppId;
    }

    public static void setmAppId(int mAppId) {
        PostData.mAppId = mAppId;
    }

    public static int getmOwnAppId() {
        return mOwnAppId;
    }

    public static void setmOwnAppId(int mOwnAppId) {
        PostData.mOwnAppId = mOwnAppId;
    }

    public static class loader {

        private static void loadData() {
            try {
                mReader.load(new FileReader(INI_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mUnicToken = mReader.get("vk_app", "mToken", String.class);
            mAPIVersion = mReader.get("vk_app", "mAPIVersion", double.class);
            mGroupId = mReader.get("vk_app", "mGroupId", int.class);
            mAppId = mReader.get("vk_app", "mAppId", int.class);
            mOwnAppId = mReader.get("vk_app", "mOwnAppId", int.class);
        }

        public static void saveData() {
            mWriter.put("vk_app", "mToken", mUnicToken);
            mWriter.put("vk_app", "mAPIVersion", mAPIVersion);
            mWriter.put("vk_app", "mGroupId", mGroupId);
            mWriter.put("vk_app", "mAppId", mAppId);
            mWriter.put("vk_app", "mOwnAppId", mOwnAppId);
            try {
                mWriter.store(new FileOutputStream(INI_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private static void initializeData(){
            mWriter.put("vk_app", "mToken", 0);
            mWriter.put("vk_app", "mAPIVersion", 0);
            mWriter.put("vk_app", "mGroupId", 0);
            mWriter.put("vk_app", "mAppId", 0);
            mWriter.put("vk_app", "mOwnAppId", 0);
            try {
                mWriter.store(new FileOutputStream(INI_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
