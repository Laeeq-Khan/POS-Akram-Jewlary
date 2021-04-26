package POS.Home;

import Utils.DB_Connection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.smattme.MysqlExportService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import org.zeroturnaround.zip.commons.FileUtils;

 
public class Login_Controller implements Initializable {
 
    
    @FXML TextField username_Field;
    @FXML PasswordField  password_Field;
    @FXML Button login_Button;
    static String username,password,userId;
    @FXML Button exitButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Events();   
    }    
    
    public void Events(){
        
        exitButton.setOnAction(evt->{
            System.exit(0);
        });
        
        login_Button.setOnAction(event->{
             Login1();
        });
        login_Button.setOnKeyReleased(event->{
        if(event.getCode() == KeyCode.ENTER){
               Login1();
        }
        });
        
        password_Field.setOnKeyReleased(evt->{
        if(evt.getCode() == KeyCode.ENTER){
             Login1();
        }
        });
        
    }//end of function
    
    
    private void Login1(){
     //   updatingdatabaes();
        String username = username_Field.getText();
        String pass= password_Field.getText();
         if(validation(username, pass)==false)return;
        
        try {
            
            String query = "select * from users where username=? AND user_Pass=?";
            PreparedStatement st =  DB_Connection.getConnection().prepareStatement(query);
            st.setString(1, username);
            st.setString(2, pass);
            ResultSet rs  = st.executeQuery();
            if(rs.next()){
                this.username= rs.getString("username");
                password  = rs.getString("user_Pass");
                userId = rs.getString("user_id");
                VBox p =  FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Main.fxml"));
                Stage stag = new Stage();
                stage = stag;
               // stag.initStyle(StageStyle.UNDECORATED);
                stag.setScene(new Scene(p));
                stag.setTitle("Total Tuck shop");
                stag.getIcons().add(new Image("/Resource/softlogo.png"));
               // stag.setFullScreen(true);
               
                stag.setHeight(800);
                stag.setWidth(1550);
                stag.show();
                username_Field.setText("");
                password_Field.setText("");
                DriverClass.getStage().close();
                
            }else{
                Alert a = new Alert(Alert.AlertType.WARNING);
                 a.setTitle("Message");
                a.setContentText("Wrong username or password");
                a.showAndWait(); 
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        
    }//end of login
    
    private static Stage stage;
    public static Stage getStage(){
        return stage;
    }
    
    private boolean validation(String a1 , String b1){
        if(a1.length()==0){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Empty");
            a.setContentText("Username Field is Empty");
            a.showAndWait();
            return false;
        }
        if(b1.length()==0){
             Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Empty");
            a.setContentText("Username Field is Empty");
            a.showAndWait();
            return false;
        }
        return true;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUserId() {
        return userId;
    }
    
    
     
   
}
