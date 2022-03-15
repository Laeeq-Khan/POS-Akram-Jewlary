/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Invoices;

import Utils.DB_Connection;
import Utils.Database_Returns;
import Utils.Validation;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.time.LocalDate.now;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class CreateInvoiceController implements Initializable {

    Stage stage;
    @FXML   private TextField customerID_Field;
    @FXML    private TextField customerName_Field;
    @FXML    private TextField contact_Field,customerBalance;
    @FXML    private TextField address_Field;
    @FXML    private TableView<CustomerTable> customerTable;
    @FXML    private TableColumn<CustomerTable, String> customerID_Column;
    @FXML    private TableColumn<CustomerTable, String> customerName_Column,customerContact_Colum,customerAddress_Column;
    @FXML    private TableColumn<CustomerTable, Float> customerBalance_column;
    @FXML    private TextField productSearc_Field,customerPay_Field;
    @FXML    private TextField productName_Field;
    @FXML    private TextField unitPrice_Field,invoiceNumber;
    @FXML    private TextField totalUnits_Field;
    @FXML    private Label totalLable;
    @FXML    private Button clearInvoiceButton,addButton,printButton,clearCustomerButton;
    @FXML    private TableView<ProductTableClass> productTable;
    @FXML    private TableColumn<ProductTableClass, String> productColumn, productCodeColumn;
    @FXML    private TableView<InvoiceTable_Class> invoiceTable;
    @FXML    private TableColumn<InvoiceTable_Class, String> productCode_InvoiceColumn;
    @FXML    private TableColumn<InvoiceTable_Class, TextField> productNameInvoice;
    @FXML    private TableColumn<InvoiceTable_Class, TextField> unitPriceInvoice;
    @FXML    private TableColumn<InvoiceTable_Class, TextField> UnitsInvoice;
    @FXML    private TableColumn<InvoiceTable_Class, Label> totalInvoice;
    @FXML    private TableColumn<InvoiceTable_Class, Integer> invoiceSrColumn;
    @FXML    private MenuItem deleteRecord;
    @FXML    private Label grandTotalLabel , invoiceBalance_Field;
    @FXML    private TextField productCode_Field;
    boolean paidStatus = false;
    private Connection con;
    private ObservableList<InvoiceTable_Class> invoiceList;
    private ObservableList<CustomerTable> customerList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       customerID_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
       invoiceNumber.setText(String.valueOf(Database_Returns.getNextAutoIncrement("invoice" , "invoiceNumber", 1000)));

       con = DB_Connection.getConnection();
       initAllTable();
       populateProductTable();
       events();
       
        
    }
    
   
    private void clearInvoice(){
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you Really want to Clear all data of this invoice";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                clear();
             } 
    }
    
     public void setStage(Stage stage){
        this.stage  = stage;
    }
     
     
    private void clear(){
        invoiceNumber.setText(String.valueOf(Database_Returns.getNextAutoIncrement("invoice" , "invoiceNumber", 1000)));
        invoiceList.clear();
        productCode_Field.setText("");
        productName_Field.setText("");
        totalUnits_Field.setText("");
        unitPrice_Field.setText("");
        totalLable.setText("0.00");
        grandTotalLabel.setText("0");
        invoiceBalance_Field.setText("0");
        customerPay_Field.setText("0");
        customerID_Field.setText("");
        customerName_Field.setText("");
        customerBalance.setText("");
        address_Field.setText("");
        contact_Field.setText("");
        paidStatus = false; 
    }
    private void events(){
         
        
        invoiceTable.setOnKeyReleased(evt->{
             printCommand(evt);
        });
        
        customerPay_Field.setOnKeyReleased(evt->{
            calculateBalance();
            printCommand(evt);
            paidStatus = true;
        });
        
        clearInvoiceButton.setOnAction(evt->{
            clearInvoice();
         });
        
        clearCustomerButton.setOnAction(evt->{
          customerID_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
          customerName_Field.setText("");
          contact_Field.setText("");
          address_Field.setText("");
          customerName_Field.requestFocus();
          customerFieldEnable();
            
        });
        
        printButton.setOnAction(evt->{
           
            Invoice_Save_And_Print();
        });
        
        deleteRecord.setOnAction(evt->{
            deleteRecord();
        });
        
        productTable.setOnMouseClicked(evt->{
            productToFields();
        });
        
        productTable.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.ENTER){
               productToFields();
               totalUnits_Field.requestFocus();
               productTable.setItems(productList);
            }
            
            if(evt.getCode() == KeyCode.UP || evt.getCode() == KeyCode.DOWN){
                productToFields();
            }
             printCommand(evt);
        });
        
        unitPrice_Field.setOnKeyReleased(evt->{
            calculateTotal(); 
            if(evt.getCode() == KeyCode.ENTER){
                addItemToInvoice();
            }
             printCommand(evt);
        });
        
        totalUnits_Field.setOnKeyReleased(evt->{
            calculateTotal(); 
             if(evt.getCode() == KeyCode.ENTER && totalUnits_Field.getText().length()>0){
               unitPrice_Field.requestFocus();
            }
             printCommand(evt);
            
        });
        
        productSearc_Field.setOnKeyReleased(evt->{
            filterProductByName();
            if(evt.getCode() == KeyCode.ENTER){
                if(productList.size()>0){
                    productTable.requestFocus();
                    productTable.getSelectionModel().select(0);
                }
            }
             printCommand(evt);
        });
        
        customerTable.setOnMouseClicked(evt->{
            customerTableToField();
        });
        customerTable.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.UP || evt.getCode() == KeyCode.DOWN ){
                 customerTableToField();
            }
            if(evt.getCode() == KeyCode.ENTER){
                totalUnits_Field.requestFocus();
            }
             printCommand(evt);
        });
        
        contact_Field.setOnKeyReleased(evt->{
           
            if(evt.isControlDown() && evt.getCode() == KeyCode.ENTER){
                if(customerList.size()>0){
                    customerTable.requestFocus();
                    customerTable.getSelectionModel().select(0);
                    customerTableToField();
                }
            }
            
            if(!evt.isControlDown() && evt.getCode() == KeyCode.ENTER){
                populateCustomerTable(contact_Field.getText());
                if(contact_Field.getText().length()==0)return;
                address_Field.requestFocus();
            }
            
             printCommand(evt);
        });
        customerName_Field.setOnKeyReleased(evt->{
             if(evt.isControlDown() && evt.getCode() == KeyCode.ENTER){
                if(customerList.size()>0){
                    customerTable.requestFocus();
                    customerTable.getSelectionModel().select(0);
                    customerTableToField();
                   
                }
                 
            }
        
            
            if(!evt.isControlDown() && evt.getCode() == KeyCode.ENTER){
                populateCustomerTable(customerName_Field.getText());
                if(customerName_Field.getText().length()==0)return;
                contact_Field.requestFocus();
            }
            changeTitle(customerName_Field.getText());
            
             printCommand(evt);
        });
        
        address_Field.setOnKeyReleased(evt->{
             if(evt.getCode() == KeyCode.ENTER){
                totalUnits_Field.requestFocus();
            }
              printCommand(evt);
        });
        
    }
    private void calculateBalance(){
        if(Validation.product_Price_color(customerPay_Field, 999999999) && Validation.product_Price_color(grandTotalLabel.getText(), 999999999)){
                String gTotal = grandTotalLabel.getText();
                String userAmt = customerPay_Field.getText();
                
                float gtotal = Float.parseFloat(gTotal);
                float userAmount = Float.parseFloat(userAmt);
                
                float balance = gtotal - userAmount;
                invoiceBalance_Field.setText(String.valueOf(balance));
                printButton.setDisable(false);
            }else{
                printButton.setDisable(true);
            }
    }
    private void deleteRecord(){
           if(invoiceTable.getSelectionModel().getSelectedItem() == null) return;
           int index = invoiceTable.getSelectionModel().getSelectedIndex();
           invoiceList.remove(index);
           calculateGrandTotal();
    }
    private void initAllTable(){
        invoiceSrColumn.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , Integer>("sr"));
        productCode_InvoiceColumn.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , String>("code"));
        productNameInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , TextField>("name"));
        unitPriceInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , TextField>("unitPrice"));
        UnitsInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , TextField>("units"));
        totalInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_Class , Label>("total"));
        invoiceList = FXCollections.observableArrayList();
        
        
        customerID_Column.setCellValueFactory(new PropertyValueFactory<CustomerTable , String>("id"));
        customerName_Column.setCellValueFactory(new PropertyValueFactory<CustomerTable , String>("customerName"));
        customerContact_Colum.setCellValueFactory(new PropertyValueFactory<CustomerTable , String>("contact"));
        customerAddress_Column.setCellValueFactory(new PropertyValueFactory<CustomerTable , String>("address"));
        customerBalance_column.setCellValueFactory(new PropertyValueFactory<CustomerTable , Float>("balance"));
        
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<ProductTableClass , String>("code"));
        productColumn.setCellValueFactory(new PropertyValueFactory<ProductTableClass , String>("productName"));
        
    }
    private void addItemToInvoice(){
        calculateGrandTotal();
        if(!Validation.Only_Number_Color(totalUnits_Field, 8))return;
        if(!Validation.product_Price_color(unitPrice_Field, 10000000))return;
        if(totalUnits_Field.getText().equals("0") || unitPrice_Field.getText().equals("0"))return;
        String code = productCode_Field.getText();
        TextField price = new TextField(unitPrice_Field.getText());
        price.setAlignment(Pos.CENTER);
        TextField items = new TextField(totalUnits_Field.getText());
        TextField productName = new TextField(productName_Field.getText());
        
        items.setAlignment(Pos.CENTER);
        Label total = new Label();
        total.setAlignment(Pos.CENTER);
        total.setStyle("-fx-font-weight: bold;");
        
       
        
        price.setOnKeyReleased(evt->{
            calculate(price, items, total);
        });
        items.setOnKeyReleased(evt->{
            calculate(price, items, total);
        });

        calculate(price, items, total);
        invoiceList.add(new InvoiceTable_Class(code, productName, price, items, total,0));
        calculateGrandTotal();
        invoiceTable.setItems(invoiceList);
        
        unitPrice_Field.setText("");
        totalUnits_Field.setText("");
        productName_Field.setText("");
        productCode_Field.setText("");
        totalUnits_Field.requestFocus();
        totalLable.setText("0.0");
        productSearc_Field.setText("");
        
    }
    public void calculate(TextField price, TextField items, Label total){
        if(!Validation.Only_Number_Color(items, 8)){
            total.setText("Invalid");
            return;
        }
        if(!Validation.product_Price_color(price, 10000000)){
            total.setText("Invalid");
            return;
        }
        int units = Integer.parseInt(items.getText());
        float priceD = Float.parseFloat(price.getText());
        float t =(float) units * priceD;
        total.setText(String.valueOf(t));
        calculateGrandTotal();
       
    }

    private void changeTitle(String text) {
         stage.setTitle(text+ " Bill");
    }
    public class InvoiceTable_Class{
        String code;
        TextField name, unitPrice, units;
        Label total;
        int sr;

        public InvoiceTable_Class(String code, TextField name, TextField unitPrice, TextField units, Label total, int sr) {
            this.code = code;
            this.name = name;
            this.unitPrice = unitPrice;
            this.units = units;
            this.total = total;
            this.sr = sr;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }
        
       

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public TextField getName() {
            return name;
        }

        public void setName(TextField name) {
            this.name = name;
        }

        public TextField getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(TextField unitPrice) {
            this.unitPrice = unitPrice;
        }

        public TextField getUnits() {
            return units;
        }

        public void setUnits(TextField units) {
            this.units = units;
        }

        public Label getTotal() {
            return total;
        }

        public void setTotal(Label total) {
            this.total = total;
        }
        
    }
    private void calculateTotal(){
        String unitPrice = unitPrice_Field.getText();
        String totalUnit= totalUnits_Field.getText();
        
        if(Validation.product_Price_color(unitPrice_Field, 10000000)==false)return;
        if(Validation.Only_Number_Color(totalUnits_Field, 8)==false)return;
        
        float units = Float.parseFloat(unitPrice_Field.getText());
        int items = Integer.parseInt(totalUnits_Field.getText());
        
        float total = (float)units * items;
        totalLable.setText(String.valueOf(total));
        
    }
    private void productToFields(){
         if(productTable.getSelectionModel().getSelectedItem() == null) return;
         ProductTableClass row = productTable.getSelectionModel().getSelectedItem();
         productCode_Field.setText(row.getCode());
         productName_Field.setText(row.getProductName());
    }
    private void populateCustomerTable(String search){
       
        if(search.length() ==0 || search == null || search.equals(""))return;
        customerList =FXCollections.observableArrayList();
        try {
            PreparedStatement stm = con.prepareStatement("select * from  customer where name like '%"+search+"%' or  contact like '%"+search+"%'  ");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String id = rs.getString("customerId");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                float balance = (float)rs.getFloat("balance");
                customerList.add(new CustomerTable(id, name, contact, address , balance));
            }
            customerTable.setItems(customerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class CustomerTable{
        String id, customerName, contact, address;
        float balance;

        public CustomerTable(String id, String customerName, String contact, String address, float balance) {
            this.id = id;
            this.customerName = customerName;
            this.contact = contact;
            this.address = address;
            this.balance = balance;
        }

        

        public float getBalance() {
            return balance;
        }

        public void setBalance(float balance) {
            this.balance = balance;
        }
        
        

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
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
    private void customerTableToField(){
         if(customerTable.getSelectionModel().getSelectedItem() == null) return;
         CustomerTable row = customerTable.getSelectionModel().getSelectedItem();
         customerID_Field.setText(row.getId());
         customerName_Field.setText(row.getCustomerName());
         contact_Field.setText(row.getContact());
         address_Field.setText(row.getAddress());
         customerBalance.setText(String.valueOf(row.getBalance()));
         customerFieldDisable();
         changeTitle(customerName_Field.getText());
    }
    private void customerFieldDisable(){
        customerName_Field.setDisable(true);
        contact_Field.setDisable(true);
        address_Field.setDisable(true);
       changeTitle(customerName_Field.getText());
    }
    private void customerFieldEnable(){
        customerName_Field.setDisable(false);
        contact_Field.setDisable(false);
        address_Field.setDisable(false);
        System.out.println("Customer are "+customerList.size());
        customerTable.setItems(customerList);
        changeTitle(customerName_Field.getText());
    }
//    private void filterCustomersByName() {
//     if(customerName_Field.getText().length()==0){
//          customedrTable.setItems(customerList);
//         return;
//     }
//     ObservableList<CustomerTable> list2=FXCollections.observableArrayList();
//     ObservableList<TableColumn<CustomerTable,?>> cols=customerTable.getColumns();
//        for (int i = 0; i < customerList.size(); i++) {
//            for (int j = 0; j <cols.size(); j++) {
//                TableColumn ab=cols.get(j);
//                String value=ab.getCellData(customerList.get(i)).toString().toLowerCase();
//                String text = new String (customerName_Field.getText().toString().toLowerCase());
//                if (value.contains(text)){
//                  list2.add(customerList.get(i));
//                
//                } else {
//                }
//            }         
//        }
//         customerTable.setItems(list2);
//}//end of filter method
//    private void filterCustomersByContact() {
//     if(contact_Field.getText().length()==0){
//          customerTable.setItems(customerList);
//         return;
//     }
//     ObservableList<CustomerTable> list2=FXCollections.observableArrayList();
//     ObservableList<TableColumn<CustomerTable,?>> cols=customerTable.getColumns();
//        for (int i = 0; i < customerList.size(); i++) {
//            for (int j = 0; j <cols.size(); j++) {
//                TableColumn ab=cols.get(j);
//                String value=ab.getCellData(customerList.get(i)).toString().toLowerCase();
//                String text = new String (contact_Field.getText().toString().toLowerCase());
//                if (value.contains(text)){
//                  list2.add(customerList.get(i));
//                
//                } else {
//                }
//            }         
//        }
//         customerTable.setItems(list2);
//}//end of filter method
    ObservableList<ProductTableClass> productList;
    private void populateProductTable(){
        
        productList =  FXCollections.observableArrayList();
        
        try {
            PreparedStatement stm = con.prepareStatement("select * from product where status =1");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                String code = rs.getString("code");
                String name = rs.getString("name");
                
                productList.add(new ProductTableClass(code, name));
            }
            productTable.setItems(productList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
    }
    public class ProductTableClass{
        String code, productName;

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
    private void filterProductByName() {
     if(productSearc_Field.getText().length()==0){
          productTable.setItems(productList);
         return;
     }
     ObservableList<ProductTableClass> list2=FXCollections.observableArrayList();
     ObservableList<TableColumn<ProductTableClass,?>> cols=productTable.getColumns();
        for (int i = 0; i < productList.size(); i++) {
            for (int j = 0; j <cols.size(); j++) {
                TableColumn ab=cols.get(j);
                String value=ab.getCellData(productList.get(i)).toString().toLowerCase();
                String text = new String (productSearc_Field.getText().toString().toLowerCase());
                if (value.contains(text)){
                  list2.add(productList.get(i));
                
                } else {
                }
            }         
        }
         productTable.setItems(list2);
}//end of filter method
    private boolean calculateGrandTotal(){
        float gTotal =0;
        boolean stats= true;
        for (int i = 0; i < invoiceList.size(); i++) {
            
            invoiceList.get(i).setSr(i+1);
            if(Validation.Only_Number_Color(invoiceList.get(i).getUnits(), 8) && Validation.product_Price_color(invoiceList.get(i).getUnitPrice(), 10000000
            )){
                   String t = invoiceList.get(i).getTotal().getText();
                   float f = Float.parseFloat(t);
                   gTotal +=f;
            }else{
                stats =false;
            }
        }
        if(stats){
            grandTotalLabel.setText(String.valueOf(gTotal));
            invoiceBalance_Field.setText(String.valueOf(gTotal));
            if(paidStatus==false){
                 customerPay_Field.setText(String.valueOf(gTotal));
            }
           
           // printButton.setDisable(false);
        }else{
            grandTotalLabel.setText("Invalid Entry in Invoice Table");
            printButton.setDisable(true);
            return false;
        }
        calculateBalance();
     return true;   
    }
    private void Invoice_Save_And_Print(){
        
        if(!Validation.product_Price_color(customerPay_Field , 999999999)){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning! Can't print");
                a.setContentText("Paid Amount Value is Wrong");
                a.show();
            return;
            }
         if(calculateGrandTotal()==false){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning! Can't print");
                a.setContentText("Please Enter Valid value in Invoice Table, Before Print");
                a.show();
                return;
            }
            if(customerName_Field.getText().equals("null") || customerName_Field.getText() == null || customerName_Field.getText().length()<2){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning! Can't print");
                a.setContentText("Please Fill Customer Information, Before Print");
                a.show();
                customerName_Field.requestFocus();
                return;
            }
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you Want to Save and Print Invoice";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
            if(!(result.get() == ButtonType.OK)) {
               return;
             } 
            
        if(invoiceList.size()<=0){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Your Invice is empty. Please add some products");
            a.show();
            return;
        }
        customerID_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
        String customerId = getCustomerId();
        if(customerId.equalsIgnoreCase("null") || customerId == ""){
          Alert a = new Alert(Alert.AlertType.WARNING);
          a.setTitle("Customer is not selected");
          a.setContentText("Please select customer");
          a.show();
          return;
        }
        try {
            PreparedStatement stm  = con.prepareStatement("insert into invoice(date , displayDate , customerId, time) values (?,?,?,?)");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
            LocalDateTime now = LocalDateTime.now();  
            String datee  = String.valueOf(dtf.format(now));
            stm.setInt(1, Validation.date_to_int(datee));
            stm.setString(2, datee);
            stm.setString(3, customerId);
            stm.setString(4, String.valueOf(java.time.LocalTime.now()));
            stm.executeUpdate();
            
           String query = "select max(invoiceNumber) as maxnumber from invoice";
           PreparedStatement stm2 = con.prepareStatement(query);
           ResultSet rs = stm2.executeQuery();
           if(rs.next()){
               invoiceNumber.setText(rs.getString("maxnumber"));
               System.out.println("max number is "+invoiceNumber.getText());
           }
            
           
            
            for (int i = 0; i < invoiceList.size(); i++) {
                String invoice = invoiceNumber.getText();
                stm = con.prepareStatement("insert into invoicedetails(invoicenumber,serial,productName,units,price,total) values(?,?,?,?,?,?)");
                stm.setString(1, invoice);
                stm.setString(2, String.valueOf(invoiceList.get(i).getSr()));
                stm.setString(3, invoiceList.get(i).getName().getText());
                stm.setString(4, invoiceList.get(i).getUnits().getText());
                stm.setString(5, invoiceList.get(i).getUnitPrice().getText());
                stm.setString(6, invoiceList.get(i).getTotal().getText());
                stm.executeUpdate();
                
            }
            
            // Updateing customer balance
            try {
                float pay = Float.parseFloat(invoiceBalance_Field.getText());
                stm = con.prepareStatement("update customer set balance = balance + ? where customerId=?");
                stm.setFloat(1, pay);
                stm.setString(2, customerId);
                stm.executeUpdate();
                System.out.println("Customer balance updated");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            String balancString = customerBalance.getText();
            String paidString = customerPay_Field.getText();
            if( balancString == null ||  balancString == "" || balancString.length() ==0){
                balancString = "0";
            }
            if(paidString == null || paidString == "" || paidString.length() ==0){
                paidString = "0";
            }
           
            float balnce= Float.parseFloat(balancString);
            float paid = Float.parseFloat(paidString);
            new InvoicePrint(invoiceList,paid,balnce , grandTotalLabel.getText(), Validation.Reversing_Date(datee), invoiceNumber.getText(), customerName_Field.getText(), contact_Field.getText(), address_Field.getText());
            clear();
            invoiceNumber.setText(String.valueOf(Database_Returns.getNextAutoIncrement("invoice" , "invoiceNumber", 1000)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    private String getCustomerId(){
        customerID_Field.setText(String.valueOf(Database_Returns.getNextAutoIncrement("customer" , "customerId", 1)));
        String customerId = customerID_Field.getText();
        String customerName = customerName_Field.getText();
        String contact  = contact_Field.getText();
        String address = address_Field.getText();
        boolean newEntry = true;
        try {
            PreparedStatement stm = con.prepareStatement("select customerId from customer where customerId =? ");
            stm.setString(1, customerId);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                newEntry = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(newEntry){
            try {
                PreparedStatement stm = con.prepareStatement("insert into customer(name,contact,address) values(?,?,?)");
                stm.setString(1, customerName);
                stm.setString(2, contact);
                stm.setString(3, address);
                stm.executeUpdate();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return customerId;
    }
    private void printCommand(KeyEvent evt){
        if(evt.getCode() == KeyCode.P && evt.isControlDown()){
            Invoice_Save_And_Print();
        }
    }
     private float getCustomerBalance(){
         float balance=0;
         try {
             PreparedStatement stm = con.prepareStatement("select balance from customer where customerId =?");
             stm.setString(1, customerID_Field.getText());
             ResultSet rs = stm.executeQuery();
             if(rs.next()){
                 balance= rs.getFloat("balance");
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return balance;
     }
     
     
}
