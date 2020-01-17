package zelosin.pack.services;

import javafx.application.Platform;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import zelosin.pack.connections.RequestConnection;
import zelosin.pack.data.PostData;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class VKService {
    private static JSONObject mJsonResponse;
    private static JSONObject mJsonObject;
    private static List<NameValuePair> mParams;

    private static Label mStatusLabel;


    private static final String VKSERVER_API = "https://api.vk.com/method/photos.getWallUploadServer";
    private static final String VKPOST_API = "https://api.vk.com/method/wall.post";
    private static final String VKSAVE_API = "https://api.vk.com/method/photos.saveWallPhoto";

    public static void setmStatusLabel(Label mStatusLabel) {
        VKService.mStatusLabel = mStatusLabel;
    }

    private static void updateStatus(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mStatusLabel.setText(text);
            }
        });
    }

    public static String getServerForUpload(){
        mJsonResponse = null;
        updateStatus("Getting server for upload");
        try {
            mJsonResponse = RequestConnection.GETJson(VKSERVER_API + "?group_id="+ PostData.getmGroupId()
                    +"&access_token="+PostData.getmUnicToken()
                    +"&v="+PostData.getmAPIVersion());
            return ((JSONObject)mJsonResponse.get("response")).get("upload_url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void wallTextPost(String pMessageText) throws IOException {
        mParams = PostData.getmParamsForPost();
        mParams.set(3, new BasicNameValuePair("message", PostData.getmSpFoo()+"\n"+pMessageText));
        RequestConnection.POST(VKPOST_API, mParams);
    }


    public static void postImage(String pImageAddres, String pMessageText) throws IOException {
        mJsonResponse = null;

        mParams = PostData.getmParamsForSave();
        mJsonResponse = RequestConnection.POSTImage(getServerForUpload(), pImageAddres);
        mParams.set(1, new BasicNameValuePair("server", mJsonResponse.get("server").toString()));
        mParams.set(2, new BasicNameValuePair("photo", mJsonResponse.get("photo").toString()));
        mParams.set(3, new BasicNameValuePair("hash", mJsonResponse.get("hash").toString()));
        mJsonResponse = RequestConnection.POST(VKSAVE_API, mParams);
        updateStatus("Image uploaded to server");
        mParams = PostData.getmParamsForPost();
        mJsonObject = (((JSONObject)mJsonResponse.getJSONArray("response").get(0)));
        mParams.set(4, new BasicNameValuePair("attachment", "photo"+
                mJsonObject.get("owner_id")+"_"+mJsonObject.get("id")));
        mParams.set(3, new BasicNameValuePair("message", PostData.getmSpFoo()+"\n"+pMessageText));
        RequestConnection.POST(VKPOST_API, mParams);
        updateStatus("Image posted");
    }
}
















