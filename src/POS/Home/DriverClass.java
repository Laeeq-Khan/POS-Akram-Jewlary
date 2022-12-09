/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Home;

import POS.Invoices.CreateInvoiceController;
import Utils.DB_Connection;
import Utils.Validation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Laeeq-Khan
 */
public class DriverClass extends Application{

    
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        DB_Connection.connect_with_db();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Resource/softlogo.png"));
        stage.show();
//          try{
//                DB_Connection.connect_with_db();
//                FXMLLoader  p =  new FXMLLoader(getClass().getClassLoader().getResource("Layouts/CreateInvoice.fxml"));
//                BorderPane root = (BorderPane) p.load();
//                CreateInvoiceController controller = (CreateInvoiceController)p.getController();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.setTitle("Invoice");
//                controller.setStage(stage);
//                stage.setMaximized(true);
//                stage.getIcons().add(new Image("/Resource/softlogo.png"));
//                stage.show();
//                
//                
//           }catch(Exception e){
//               e.printStackTrace();
//               System.exit(0);
//           }
        
        this.stage  = stage;
    }
    
    public static void main(String ar[]){
         launch(ar);
       }
 
    
     public static Stage getStage(){
        return stage;
    }
    
}
