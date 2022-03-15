/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author Laeeq-Khan
 */
public class DB_Connection {
    static Connection con;
    public static void connect_with_db(){
          try {
               Class.forName("com.mysql.jdbc.Driver");  
               //dddcon =  DriverManager.getConnection("jdbc:mysql://localhost:3306/pos","root","");
               con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/pos","root","");
               System.out.println("Connected");
          } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database");
            a.showAndWait();
            e.printStackTrace();
            
         }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database: Error Code : "+e);
            a.showAndWait();
            e.printStackTrace();
          }
        
     }//end of function
    
    
   
    public static Connection getConnection(){
        try {
           if(con.isClosed() || con == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Database connection error");
            a.setContentText("Your are not connected with database");
            a.showAndWait();
            connect_with_db();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
