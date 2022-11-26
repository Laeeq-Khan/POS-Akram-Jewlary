/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Home;

import POS.Invoices.CreateInvoiceController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class MainController implements Initializable {

    @FXML    private MenuItem productAccount_MenuItem;
    @FXML    private MenuItem customerAccount_MenuItem;
    @FXML    private MenuItem addStock_MenuItem;
    @FXML    private MenuItem createInvoice_MenuItem;
    @FXML    private MenuItem editInvoices_MenuItem;
    @FXML    private MenuItem invoiceHistory_Menu;
    @FXML    private MenuItem purchaseReturn_MenuItem;
    @FXML    private MenuItem purchaseReport_MenuItem;
    @FXML    private MenuItem receivableMenu,saleReportMenus;
    @FXML    private MenuItem returnsReport_MenuItem;
    @FXML    private MenuItem customerPurchaseReport_MenuItem;
    @FXML    private MenuItem changePassword_MenuItem,close , home;
    @FXML    private AnchorPane main_Container;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        events();
        
        try{
             AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Home.fxml"));
             main_Container.getChildren().clear();
             main_Container.getChildren().add(p);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    private void events(){
        
        saleReportMenus.setOnAction(evt->{
            try{
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Sale_Report.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
           }catch(Exception e){
               e.printStackTrace();
           }
        });
        
        receivableMenu.setOnAction(evt->{
                    try{
                    AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Receivable.fxml"));
                    main_Container.getChildren().clear();
                    main_Container.getChildren().add(p);
               }catch(Exception e){
                   e.printStackTrace();
               }
        });
        
        invoiceHistory_Menu.setOnAction(evt->{
            try{
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Invoice_History.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
                }catch(Exception e){
                    e.printStackTrace();
                }
        });
        createInvoice_MenuItem.setOnAction(evt->{
            try{
                FXMLLoader  p =  new FXMLLoader(getClass().getClassLoader().getResource("Layouts/CreateInvoice.fxml"));
                BorderPane root = (BorderPane) p.load();
                CreateInvoiceController controller = (CreateInvoiceController)p.getController();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Invoice");
                controller.setStage(stage);
                stage.getIcons().add(new Image("/Resource/softlogo.png"));
                stage.setMaximized(true);
                stage.show();
             }catch(Exception e){
                 e.printStackTrace();
             }
        });
        
        home.setOnAction(evt->{
                try{
                   AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/Home.fxml"));
                   main_Container.getChildren().clear();
                   main_Container.getChildren().add(p);
              }catch(Exception e){
                  e.printStackTrace();
              }
        });
        productAccount_MenuItem.setOnAction(evt->{
        try { 
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/CreateProduct.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
                System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        customerAccount_MenuItem.setOnAction(evt->{
        try { 
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/CreateCustomer.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
                System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        
        editInvoices_MenuItem.setOnAction(evt->{
        try { 
                AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/EditInvoice.fxml"));
                main_Container.getChildren().clear();
                main_Container.getChildren().add(p);
                System.out.println("click on home");
             } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Error in mainWindow.class "+e);
            }
        });
        
        close.setOnAction(evt->{
            Login_Controller.getStage().close();
            System.exit(0);
        });
        
        changePassword_MenuItem.setOnAction(evt->{
         try{
             AnchorPane p =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("Layouts/changePassword.fxml"));
             main_Container.getChildren().clear();
             main_Container.getChildren().add(p);
        }catch(Exception e){
            e.printStackTrace();
        }
        });
    }
}
