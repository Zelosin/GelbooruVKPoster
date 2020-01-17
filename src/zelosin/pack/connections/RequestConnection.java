package zelosin.pack.connections;


import javafx.application.Platform;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.List;
import javafx.scene.control.Label;


public class RequestConnection {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static HttpClient mClient;
    private static HttpGet GETRequest;
    private static HttpPost POSTRequest;
    private static HttpResponse mResponse;
    private static MultipartEntity mEntityImage;
    private static Label mAlertLabel;

    public static void setmAlertLabel(Label mAlertLabel) {
        RequestConnection.mAlertLabel = mAlertLabel;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    protected static void buildGetClient(String URL){
        mClient = HttpClientBuilder.create().build();
        GETRequest = new HttpGet(URL);
        GETRequest.addHeader("User-Agent", USER_AGENT);
    }
    protected static void buildPostClient(String URL){
        mClient = HttpClientBuilder.create().build();
        POSTRequest = new HttpPost(URL);
        POSTRequest.addHeader("User-Agent", USER_AGENT);
    }

    public static String GET(String URL) throws IOException {
        buildGetClient(URL);
        mResponse = mClient.execute(GETRequest);
        return (readAll(new BufferedReader(new InputStreamReader(mResponse.getEntity().getContent()))));
    }
    public static JSONObject GETJson(String URL) throws IOException {
        buildGetClient(URL);
        mResponse = mClient.execute(GETRequest);
        return new JSONObject(readAll(new BufferedReader(new InputStreamReader(mResponse.getEntity().getContent()))));
    }

    public static JSONObject POST(String URL, List<NameValuePair> pParams) throws IOException {
        buildPostClient(URL);
        POSTRequest.setEntity(new UrlEncodedFormEntity(pParams));
        mResponse = mClient.execute(POSTRequest);
        return new JSONObject(readAll(new BufferedReader(new InputStreamReader(mResponse.getEntity().getContent()))));
    }

    public static JSONObject POSTImage(String URL, String pImageSrc) throws IOException {
        buildPostClient(URL);
        mEntityImage = new MultipartEntity();
        mEntityImage.addPart("file", new FileBody(new File(pImageSrc)));
        POSTRequest.setEntity(mEntityImage);
        mResponse = mClient.execute(POSTRequest);
        return (new JSONObject(readAll(new BufferedReader(new InputStreamReader(mResponse.getEntity().getContent())))));
    }
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }
}




















