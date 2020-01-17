package zelosin.pack.services;

import com.jfoenix.controls.JFXTextArea;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import zelosin.pack.connections.RequestConnection;
import zelosin.pack.data.PreviewData;

import java.io.IOException;

public class GelbooruService {
    private static final String GEL_REQ = "https://gelbooru.com//index.php?page=dapi&s=post&q=index";
    private static final String SAVE_DIST = "src/zelosin/pack/images/SPTempImage.jpg";

    private static JFXTextArea mTagArea;

    private static String mTags;
    private static String mSrc;
    private static int mPID = 0;
    private static int mLimit = 42;
    private static JSONObject json;
    private static JSONObject mJsonObject;
    private static JSONArray mJsonArray;

    public static void setmTagArea(JFXTextArea mTagArea) {
        GelbooruService.mTagArea = mTagArea;
    }
    public static void nextPage(){
        mPID++;
    }
    public static void prevPage(){
        if(mPID>0)
            mPID--;
    }

    public static void setmPID(int mPID) {
        GelbooruService.mPID = mPID;
    }
    public static int getmPID() {
        return mPID;
    }

    public static void getXML() throws IOException {
        if(!mTagArea.getText().isEmpty())
            json = XML.toJSONObject(RequestConnection.GET(GEL_REQ + "&pid=" + mPID + "&limit=" + mLimit+"&tags="+mTagArea.getText()));
        else
            json = XML.toJSONObject(RequestConnection.GET(GEL_REQ + "&pid=" + mPID + "&limit=" + mLimit));
        mJsonArray =((JSONObject)json.get("posts")).getJSONArray("post");
        for(Object obj: mJsonArray) {
            json = (JSONObject) obj;
            RequestConnection.saveImage(json.get("file_url").toString(),"src/zelosin/pack/images/SPTempImage.jpg");
            mTags = json.get("tags").toString().replace(" ", "#");
            mSrc = json.get("source").toString();
            VKService.postImage(SAVE_DIST, "src: "+mSrc+"\n"+"tags: "+mTags);
        }
    }
    public static void getXMLPage() throws IOException {
        /*if(!mTagArea.getText().isEmpty())
            json = XML.toJSONObject(RequestConnection.GET(GEL_REQ + "&pid=" + mPID + "&limit=" + mLimit+"&tags="+mTagArea.getText()));
        else*/
            json = XML.toJSONObject(RequestConnection.GET(GEL_REQ + "&pid=" + mPID + "&limit=" + 42));
        mJsonArray =((JSONObject)json.get("posts")).getJSONArray("post");
        for(Object obj: mJsonArray) {
            json = (JSONObject) obj;
            new PreviewData(json.get("preview_url").toString(),
                    json.get("source").toString(),
                    json.get("tags").toString().replace(" ", "#"),
                    json.get("file_url").toString(),
                    Integer.valueOf(json.get("width").toString()),
                    Integer.valueOf(json.get("height").toString())
                    );
        }
    }
}









