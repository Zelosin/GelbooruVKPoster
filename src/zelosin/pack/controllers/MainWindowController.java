package zelosin.pack.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import zelosin.pack.connections.RequestConnection;
import zelosin.pack.data.PreviewData;
import zelosin.pack.services.GelbooruService;
import zelosin.pack.data.PostData;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.control.Label;
import zelosin.pack.services.VKService;

import javax.imageio.ImageIO;


public class MainWindowController {
    @FXML
    private JFXTextArea dToken, dAPIVer, dIDGroup, dIDApp, dOwnID, dTagArea;
    @FXML
    private JFXComboBox<String> dServicePicker;
    @FXML
    private Label dStatusLabel;
    @FXML
    private VBox dContentScroller;
    @FXML
    private ScrollPane dScroller;
    @FXML
    private Label dCurPageLabel;
    @FXML
    private ImageView dPrevButton;
    @FXML
    private ImageView dFullImage;
    @FXML
    private Label dAlertLabel;
    @FXML
    private WebView dWebImageView;

    private HBox tempBox;
    private String tempString;
    private Runnable mRequestTask;
    private Runnable mOpenFullTask;
    private Thread mOpenFullThread= new Thread();
    private Thread mRequestThread;
    private int mSelectedImg;
    private WebEngine mWebEngine;

    public void initialize(){

        dToken.setText(PostData.getmUnicToken());
        dAPIVer.setText(String.valueOf(PostData.getmAPIVersion()));
        dIDGroup.setText(String.valueOf(PostData.getmGroupId()));
        dIDApp.setText(String.valueOf(PostData.getmAppId()));
        dOwnID.setText(String.valueOf(PostData.getmOwnAppId()));

       /* dServicePicker.getItems().addAll("Gelbooru");
        dServicePicker.getSelectionModel().selectFirst();*/
        VKService.setmStatusLabel(dStatusLabel);
        GelbooruService.setmTagArea(dTagArea);

        RequestConnection.setmAlertLabel(dAlertLabel);

        initializeTasks();

        clearPictures();
        startRequest();
        mWebEngine = dWebImageView.getEngine();
        mWebEngine.getHistory().setMaxSize(1);
        java.net.CookieHandler.setDefault(null);

        dCurPageLabel.setText("1");
        if(GelbooruService.getmPID() == 0){
            dPrevButton.setVisible(false);
        }
    }

    private void initializeTasks(){
        mRequestTask = new Runnable() {
            public void run() {
                try {
                    PreviewData.clearCollection();
                    GelbooruService.getXMLPage();
                    updatePictures();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mOpenFullTask = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mWebEngine.load(null);
                        mWebEngine = null;
                        mWebEngine = dWebImageView.getEngine();
                        mWebEngine.load(PreviewData.getmPreviewCollection().get(mSelectedImg).getFull());
                        System.gc();
                        Runtime.getRuntime().gc();

                    }
                });
                System.gc();
            }
        };
    }

    private void printAlert(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dAlertLabel.setText(text);
            }
        });
    }
    private void clearAlert(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dAlertLabel.setText("");
            }
        });
    }

    public void getLinkAction(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("http://oauth.vk.com/authorize?client_id="+PostData.getmAppId()+"&response_type=token&scope=photos%2Cwall"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updatePictures() throws IOException {
            for (int i = 0; i < 14; i++) {
                tempBox = (HBox) dContentScroller.getChildren().get(i);
                for (int j = 0; j < 3; j++)
                    ((ImageView) tempBox.getChildren().get(j)).setImage
                            (SwingFXUtils.toFXImage(ImageIO.read(new URL(PreviewData.getmPreviewCollection().get(i * 3 + j).getHref())), null));
            }
    }
    private void clearPictures(){
        for(int i = 0;i<14;i++){
            tempBox = (HBox)dContentScroller.getChildren().get(i);
            for(int j = 0; j< 3; j++)
                ((ImageView)tempBox.getChildren().get(j)).setImage(null);
        }
    }

    private void startRequest(){
        mRequestThread = new Thread(mRequestTask);
        mRequestThread.start();
    }

    public void onActionPost(ActionEvent actionEvent){
        PostData.setmUnicToken(dToken.getText());
        PostData.setmAPIVersion(Double.valueOf(dAPIVer.getText()));
        PostData.setmAppId(Integer.valueOf(dIDApp.getText()));
        PostData.setmGroupId(Integer.valueOf(dIDGroup.getText()));
        PostData.setmOwnAppId(Integer.valueOf(dOwnID.getText()));
        startRequest();
        dScroller.setVvalue(0.0);
    }

    private void stopAndClear(){
        mRequestThread.stop();
        mRequestThread = null;
        System.gc();
        clearPictures();
    }

    public void onActionScrollPrev(MouseEvent mouseEvent) {
        stopAndClear();
        GelbooruService.prevPage();
        startRequest();
        dScroller.setVvalue(0.0);
        if(GelbooruService.getmPID() == 0){
            dPrevButton.setVisible(false);
        }
        dCurPageLabel.setText(String.valueOf(GelbooruService.getmPID()+1));
    }

    public void onActionScrollNext(MouseEvent mouseEvent) {
        stopAndClear();
        GelbooruService.nextPage();
        if(GelbooruService.getmPID() == 1){
            dPrevButton.setVisible(true);
        }
        startRequest();
        dScroller.setVvalue(0.0);
        dCurPageLabel.setText(String.valueOf(GelbooruService.getmPID()+1));
    }

    private void openFullImage(){
        /*mOpenFullThread.stop();
        mOpenFullThread = null;
        System.gc();
        mOpenFullThread = new Thread(mOpenFullTask);
        mOpenFullThread.start();*/
        mWebEngine.load("about:blank");
        mWebEngine.load(PreviewData.getmPreviewCollection().get(mSelectedImg).getFull());
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
        dWebImageView = null;
        System.gc();
        Runtime.getRuntime().gc();
    }

    public void onActionSelectPicture(MouseEvent mouseEvent) {
        tempString = ((ImageView)mouseEvent.getSource()).getId();
        mSelectedImg = Integer.valueOf(tempString.substring(3, tempString.length()));
        openFullImage();
    }


    public void onActionOpenPost(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(PreviewData.getmPreviewCollection().get(mSelectedImg).getFull()));
    }
    public void onActionOpenOrg(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(PreviewData.getmPreviewCollection().get(mSelectedImg).getSrc()));
    }
}


























