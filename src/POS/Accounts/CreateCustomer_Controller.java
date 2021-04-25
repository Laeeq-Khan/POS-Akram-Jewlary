
package POS.Accounts;

import Utils.DB_Connection;
import Utils.Database_Returns;
import Utils.Validation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class CreateCustomer_Controller implements Initializable {

    @FXML    private TextField customerCode_Field;
    @FXML    private TextField customerName_Field,custormerContact_Field,custormerAddress_Field;
    @FXML    private Button saveButton;
    @FXML    private Button clearAllButton;
    @FXML    private TextField search_Field;
    @FXML    private TableView<CustomerTableClass> customerTable;
    @FXML    private TableColumn<CustomerTableClass, String> customerId_Column;
    @FXML    private TableColumn<CustomerTableClass, String> customerName_Column;
    @FXML    private TableColumn<CustomerTableClass, String> customerContact_Column,customerAddress_Column;
    @FXML    private Label totalCustomers;
    @FXML    private MenuItem editButton;
    private ObservableList<CustomerTableClass> list;
    private Connection con;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerCode_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
        con = DB_Connection.getConnection();
        events();
        populateTable();
    }    
    
    private void events(){
        customerName_Field.setOnKeyReleased(evt->{
             Validation.Only_Text_Color(customerName_Field, 150);
        });
       
        
        saveButton.setOnAction(evt->{
            if(customerSaveValidation()){
                saveCustomer();
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
        customerTable.setOnKeyReleased(evt->{
           if(evt.getCode()==KeyCode.ENTER){
              dataMoveToFields();
            }
        });
        
      
        
    }
   
    
    private void dataMoveToFields(){
         if(customerTable.getSelectionModel().getSelectedItem() == null) return;
         CustomerTableClass row = customerTable.getSelectionModel().getSelectedItem();
         customerCode_Field.setText(row.getId());
         customerName_Field.setText(row.getName());
         custormerAddress_Field.setText(row.getAddress());
         custormerContact_Field.setText(row.getContact());
         saveButton.setText("Update");
    }
    
    private void populateTable(){
        customerId_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("id"));
        customerName_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("name"));
        customerContact_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("contact"));
        customerAddress_Column.setCellValueFactory(new PropertyValueFactory<CustomerTableClass , String>("address"));
        
        list = FXCollections.observableArrayList();
        try {
            PreparedStatement stm = con.prepareStatement("select * from customer");
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
            totalCustomers.setText("Total Customers "+counter);
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
    private void updateCustomer(){
          try {
            PreparedStatement statement; 
            statement = con.prepareStatement("update customer set name=?,contact=?,address=? where customerId =?");
            statement.setString(1, customerName_Field.getText());
            statement.setString(2, custormerContact_Field.getText());
            statement.setString(3, custormerAddress_Field.getText());
            statement.setString(4, customerCode_Field.getText());
           statement.executeUpdate();
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Customer Updated Succfully");
            a.show();
            clearAll();
            populateTable();

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    private void saveCustomer(){
        
        if(saveButton.getText().equalsIgnoreCase("update")){
            updateCustomer();
            return;
        }
        try {
            PreparedStatement statement; 
            statement = con.prepareStatement("insert into customer(name,contact,address) values(?,?,?)");
            statement.setString(1, customerName_Field.getText());
            statement.setString(2, custormerContact_Field.getText());
            statement.setString(3, custormerAddress_Field.getText());
            statement.executeUpdate();
            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Customer Saved Succfully");
            a.show();
            clearAll();
            populateTable();

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }
    private boolean customerSaveValidation(){
        if(!Validation.Name_Diaglog(customerName_Field, 150, "Customer Name")) return false;
        return true;
    }
    private void clearAll(){
        customerCode_Field.setText(null);
        customerName_Field.setText(null);
        custormerAddress_Field.setText(null);
        custormerContact_Field.setText(null);
        saveButton.setText("Save");
        customerCode_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
    }
  
 public void filter_records() {
     if(search_Field.getText().length()==0){
          customerTable.setItems(list);
         return;
     }
     ObservableList<CustomerTableClass> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<CustomerTableClass,?>> cols=customerTable.getColumns();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(list.get(i)).toString().toLowerCase();
                String text = new String (search_Field.getText().toString().toLowerCase());
                if (value.contains(text)){
                  list2.add(list.get(i));
                
                } else {
                }
            }         
        }
         customerTable.setItems(list2);
    
}//end of filter method
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


