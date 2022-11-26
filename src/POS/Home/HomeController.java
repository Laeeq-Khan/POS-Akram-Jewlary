/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Home;

import POS.Invoices.CreateInvoiceController;
import com.smattme.MysqlExportService;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Laeeq-Khan
 */
public class HomeController implements Initializable{

    @FXML    private AnchorPane main_Container;
    @FXML    private Label dateLabel;
    @FXML    private Button createInvoice;
    @FXML    private Button saleReport;
    @FXML    private Button printOld;
    @FXML    private Label timeLabel;
    @FXML    private Button backup;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            events();
            time();
    }
 
    
    private void events(){
        createInvoice.setOnAction(evt->{
              try{
                FXMLLoader  p =  new FXMLLoader(getClass().getClassLoader().getResource("Layouts/CreateInvoice.fxml"));
                BorderPane root = (BorderPane) p.load();
                CreateInvoiceController controller = (CreateInvoiceController)p.getController();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Invoice");
                controller.setStage(stage);
                stage.setMaximized(true);
                stage.getIcons().add(new Image("/Resource/softlogo.png"));
                stage.show();
                
                
           }catch(Exception e){
               e.printStackTrace();
               System.exit(0);
           }
        });
        
        backup.setOnAction(evt->{
            mysqlBackup();
        });
        
        saleReport.setOnAction(evt->{
              try{
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Sale_Report.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
           }catch(Exception e){
               e.printStackTrace();
           }
        });
        
        printOld.setOnAction(evt->{
              try{
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Invoice_History.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
           }catch(Exception e){
               e.printStackTrace();
           }
        });
    }
    
      
    private void time(){
         Thread timerThread = new Thread(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-YYYY");
        while (true) {
            
            final String time = simpleDateFormat.format(new Date());
            final String date = simpleDateFormat2.format(new Date());
            Platform.runLater(() -> {
                timeLabel.setText(time);
                dateLabel.setText(date);
            });
            try {
                Thread.sleep(1000); //1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });   timerThread.start();//start the thread and its ok
    }
    
     public void mysqlBackup(){
        try {
             Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, "pos");
        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "");
       
        //set the outputs temp dir
        File f = new File("D:\\");
        properties.setProperty(MysqlExportService.TEMP_DIR, f.getPath());
         
        MysqlExportService mysqlExportService = new MysqlExportService(properties);
        mysqlExportService.export();
        
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Backup Taken Successfully. Please Save your backup file ");
            a.show();
            
        } catch (Exception e) {
            System.out.println(e);
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Failed");
            a.setContentText("Backup Failed");
            a.show();
        }
    }
    
}
