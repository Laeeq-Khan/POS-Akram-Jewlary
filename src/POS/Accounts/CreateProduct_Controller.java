/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Accounts;

import Utils.DB_Connection;
import Utils.Database_Returns;
import Utils.Validation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class CreateProduct_Controller implements Initializable {

    @FXML    private TextField productCpde_Field;
    @FXML    private TextField productName_Field;
    @FXML    private Button saveButton;
    @FXML    private Button clearAllButton;
    @FXML    private TextField search_Field;
    @FXML    private TableView<ProductTableClass> productTable;
    @FXML    private TableColumn<ProductTableClass, String> productCode_Column;
    @FXML    private TableColumn<ProductTableClass, String> productName_Column;
    @FXML    private Label totalProductStatus;
    @FXML    private MenuItem deleteButton,editButton;
    private ObservableList<ProductTableClass> list;
    private ObservableList<ProductTableClass> filterData;
    private Connection con;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productCpde_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("product" , "code", 100)));
        con = DB_Connection.getConnection();
        events();
        populateTable();
    }    
    
    private void events(){
        productName_Field.setOnKeyReleased(evt->{
             Validation.Only_Text_Color(productName_Field, 150);
        });
       
        
        saveButton.setOnAction(evt->{
            if(productSaveValidation()){
                saveProduct();
            }
        });
        
        clearAllButton.setOnAction(evt->{
            clearAll();
        });
        
        search_Field.setOnKeyReleased(evt->{
            filter_records();
        });
        
        editButton.setOnAction(evt->{
            dataMoveToFields();
        });
        productTable.setOnKeyReleased(evt->{
           if(evt.getCode()==KeyCode.ENTER){
              dataMoveToFields();
            }
        });
        
        deleteButton.setOnAction(evt->{
            deleteProduct();
        });
        
    }
    
    private void deleteProduct(){
             if(productTable.getSelectionModel().getSelectedItem() == null) return;
              ProductTableClass row = productTable.getSelectionModel().getSelectedItem();
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you Really want to delete "+row.getProductName()+" Product";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                try {
                    PreparedStatement stm = con.prepareStatement("update product set status=0 where code=?");
                    stm.setString(1, row.getCode());
                    stm.executeUpdate();
                    
                    stm = con.prepareStatement("delete from product where code=? and used=0");
                    stm.setString(1, row.getCode());
                    stm.executeUpdate();
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
             } 
            populateTable();
    }
    
    private void dataMoveToFields(){
         if(productTable.getSelectionModel().getSelectedItem() == null) return;
         ProductTableClass row = productTable.getSelectionModel().getSelectedItem();
         productCpde_Field.setText(row.getCode());
         productName_Field.setText(row.getProductName());
         saveButton.setText("Update");
    }
    
    private void populateTable(){
        productCode_Column.setCellValueFactory(new PropertyValueFactory<ProductTableClass , String>("code"));
        productName_Column.setCellValueFactory(new PropertyValueFactory<ProductTableClass , String>("productName"));
        
        list = FXCollections.observableArrayList();
        try {
            PreparedStatement stm = con.prepareStatement("select * from product where status=1");
            ResultSet rs = stm.executeQuery();
            int counter  =0;
            while(rs.next()){
                String code = rs.getString("code");
                String name = rs.getString("name");
                list.add(new ProductTableClass(code, name));
                counter++;
            }
            productTable.setItems(list);
            totalProductStatus.setText("Total Products "+counter);
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
    private void updateProduct(){
          try {
            PreparedStatement statement; 
            statement = con.prepareStatement("update product set name=? where code=?");
            statement.setString(1, productName_Field.getText());
            statement.setString(2, productCpde_Field.getText());
            statement.executeUpdate();
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Product Updated Succfully");
            a.show();
            clearAll();
            populateTable();

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    private void saveProduct(){
        
        if(saveButton.getText().equalsIgnoreCase("update")){
            updateProduct();
            return;
        }
        try {
            PreparedStatement statement; 
            statement = con.prepareStatement("insert into product(name,status,used) values(?,?,?)");
            statement.setString(1, productName_Field.getText());
            statement.setString(2, "1");
            statement.setString(3, "0");
            statement.executeUpdate();
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Product Saved Succfully");
            a.show();
            clearAll();
            populateTable();

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    private boolean productSaveValidation(){
        if(!Validation.Only_Text_Diaglog(productName_Field, 150, "Product Name")) return false;
        return true;
    }
    private void clearAll(){
        productCpde_Field.setText(null);
        productName_Field.setText(null);
        saveButton.setText("Save");
        productCpde_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("product" , "code", 100)));
    }
  
 public void filter_records() {
     if(search_Field.getText().length()==0){
          productTable.setItems(list);
         return;
     }
     ObservableList<ProductTableClass> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<ProductTableClass,?>> cols=productTable.getColumns();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(list.get(i)).toString().toLowerCase();
                String text = new String (search_Field.getText().toString().toLowerCase());
                if (value.contains(text))
                {
                  list2.add(list.get(i));
                
                } else {
                }
            }         
                }
         productTable.setItems(list2);
    
}//end of filter method
  public  class ProductTableClass{
       private String code;
       private String productName;

       public ProductTableClass(String code, String productName) {
          this.code = code;
          this.productName = productName;
        }
 
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

     
        
    }
}


