package zelosin.pack.data;

import java.util.ArrayList;

public class PreviewData {
    private String href;
    private String src;
    private String tags;
    private String full;
    private int sample_width;
    private int sample_height;
    private static ArrayList<PreviewData> mPreviewCollection = new ArrayList<>();

    public PreviewData(String href, String src, String tags, String full, int sample_width, int sample_height) {
        this.href = href;
        this.src = src;
        this.tags = tags;
        this.full = full;
        this.sample_width = sample_width;
        this.sample_height = sample_height;
        mPreviewCollection.add(this);
    }

    public String getTags() {
        return tags;
    }

    public int getSample_width() {
        return sample_width;
    }

    public int getSample_height() {
        return sample_height;
    }

    public String getFull() {
        return full;
    }

    public static void clearCollection(){
        mPreviewCollection.forEach(previewData -> previewData=null);
        mPreviewCollection.clear();
        System.gc();
    }

    public static ArrayList<PreviewData> getmPreviewCollection() {
        return mPreviewCollection;
    }

    public String getHref() {
        return href;
    }

    public String getSrc() {
        return src;
    }
}
