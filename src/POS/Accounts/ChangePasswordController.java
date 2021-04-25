/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Accounts;

import POS.Home.Login_Controller;
import Utils.DB_Connection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


 
public class ChangePasswordController implements Initializable {

    @FXML    private PasswordField oldPasswrod;
    @FXML    private PasswordField newPassword;
    @FXML    private TextField oldUsername;
    @FXML    private TextField newusername;
    @FXML    private Button changeUsername;
    @FXML    private Button changePassword;
    @FXML    private PasswordField cnfmPass;
    @FXML    private TextField cnfrmusername;

     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        events();
    }    
    
    private void events(){
        changePassword.setOnAction((ActionEvent evt)->{
            String newPass =  newPassword.getText();
            String cnfrm = cnfmPass.getText();
            String oldPass = oldPasswrod.getText();
            System.out.println(newPass+"  "+cnfrm);
            if(!newPass.equals(cnfrm)){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Message");
                    a.setContentText("Your New and Confirm password is not maching..");
                    a.showAndWait();
                return;
            }
            
            if(oldPass.equals(Login_Controller.getPassword())){
                String query = "update users set user_Pass='"+newPass+"' where user_id='"+Login_Controller.getUserId()+"'";
                try {
                    Statement st =  DB_Connection.getConnection().createStatement();
                    st.executeUpdate(query);
                    oldPasswrod.clear();
                    newPassword.clear();
                    cnfmPass.clear();
                     Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Message");
                    a.setContentText("Password Succfully Changed");
                    a.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Message");
                    a.setContentText("Old password is wrong");
                    a.showAndWait();
            }
            
        });
        
        
          changeUsername.setOnAction(evt->{
            String newPass =  newusername.getText();
            String cnfrm = cnfrmusername.getText();
            String oldPass = oldUsername.getText();
            System.out.println(newPass+"  "+cnfrm);
            if(!newPass.equals(cnfrm)){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Message");
                    a.setContentText("Your New and Confirm username is not maching..");
                    a.showAndWait();
                return;
            }
            
            if(oldPass.equals(Login_Controller.getUsername())){
                String query = "update users set username='"+newusername.getText()+"' where user_id='"+Login_Controller.getUserId()+"'";
                try {
                    Statement st =  DB_Connection.getConnection().createStatement();
                    st.executeUpdate(query);
                    oldUsername.clear();
                    newusername.clear();
                    cnfrmusername.clear();
                     Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Message");
                    a.setContentText("Username Succfully Changed Succfully Changed");
                    a.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Message");
                    a.setContentText("Old Username is wrong");
                    a.showAndWait();
            }
            
        });
    }
    
}
