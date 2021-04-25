/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Invoices;

import POS.Accounts.CreateCustomer_Controller;
import Utils.DB_Connection;
import Utils.Validation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class ReceivableController implements Initializable {

    @FXML    private TextField search_Field;
    @FXML    private TableView<CustomerTableClass> customerTable;
    @FXML    private TableColumn<CustomerTableClass, String> customerId_Column;
    @FXML    private TableColumn<CustomerTableClass, String> customerName_Column;
    @FXML    private TableColumn<CustomerTableClass, String> customerContact_Column,customerAddress_Column;
    @FXML    private TextField paidAmount;
    @FXML    private Button saveButton;
    @FXML    private Label customerId;
    @FXML    private Label customerName;
    @FXML    private Label customerBalance;
    @FXML    private Label customerAddress;
    @FXML    private Label oldBalance;
    @FXML    private Label balanceLeft;
    private ObservableList<CustomerTableClass> list;
    private Connection con;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = DB_Connection.getConnection();
        initTable();
        events();
    }  
    
    private void events(){
        search_Field.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.ENTER){
                populateTable();
            }
        });
        
        customerTable.setOnKeyReleased(evt->{
            data_to_Fields();
        });
        customerTable.setOnMouseClicked(evt->{
            data_to_Fields();
        });
        
        paidAmount.setOnKeyReleased(evt->{
            calculateLeft();
        });
        saveButton.setOnAction(evt->{
            saveAmount();
        });
    }
    
    private void saveAmount(){
        if(!Validation.product_Price_color(paidAmount, 999999))return;
        if(customerId.getText().length() ==0)return;
        String left  =  balanceLeft.getText();
        try {
            PreparedStatement stm = con.prepareStatement("update customer set balance=? where customerId=?");
            stm.setString(1, left);
            stm.setString(2, customerId.getText());
            stm.executeUpdate();
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Balanced Updated");
            a.show();
            
            paidAmount.setText("");
            customerId.setText("");
            customerName.setText("");
            oldBalance.setText("");
            customerAddress.setText("");
            customerBalance.setText("");
            balanceLeft.setText("0");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void calculateLeft(){
        String paid = paidAmount.getText();
        if(!Validation.product_Price_color(paidAmount, 999999))return;
        
        float oldB = Float.parseFloat(oldBalance.getText());
        float paidNow  = Float.parseFloat(paid);
        
        float left = oldB - paidNow;
        balanceLeft.setText(String.valueOf(left));
        
    }
    private void data_to_Fields(){
        if(customerTable.getSelectionModel().getSelectedItem() == null)return ;
         CustomerTableClass row = customerTable.getSelectionModel().getSelectedItem();
         String customerIdD  = row.getId();
         
         try {
            PreparedStatement stm = con.prepareStatement("select * from customer where customerId = ?");
            stm.setString(1, customerIdD);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                String name  = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                String balance = rs.getString("balance");
                
                customerId.setText(customerIdD);
                customerName.setText(name);
                customerBalance.setText(contact);
                customerAddress.setText(address);
                oldBalance.setText(balance);
                
                
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    private void initTable(){
        customerId_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("id"));
        customerName_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("name"));
        customerContact_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("contact"));
        customerAddress_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("address"));
        
    }
     private void populateTable(){
        
        list = FXCollections.observableArrayList();
        try {
            if(search_Field.getText() == null || search_Field.getText().length()==0)return;
            PreparedStatement stm = con.prepareStatement("select * from customer where customerId like '%"+search_Field.getText()+"%' or  "
                    + " name like '%"+search_Field.getText()+"%' or contact like '%"+search_Field.getText()+"%'  ");
          
            ResultSet rs = stm.executeQuery();
            int counter  =0;
            while(rs.next()){
                String code = rs.getString("customerId");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                list.add(new CustomerTableClass(code, name, contact, address));
                counter++;
            }
            customerTable.setItems(list);
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
     public  class CustomerTableClass{
       String id, name, contact, address;

        public CustomerTableClass(String id, String name, String contact, String address) {
            this.id = id;
            this.name = name;
            this.contact = contact;
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
       
    }
}
