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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author Laeeq-Khan
 */
public class InvoiceHistory_Controller  implements Initializable {

    
    @FXML    private TableView<InvoiceTable_HistoryClass> invoiceTable;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, String> productCode_InvoiceColumn;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, Integer> invoiceSrColumn;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, TextField> productNameInvoice;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, TextField> UnitsInvoice;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, TextField> unitPriceInvoice;
    @FXML    private TableColumn<InvoiceTable_HistoryClass, Label> totalInvoice;
    @FXML    private MenuItem deleteRecord;
    @FXML    private Label grandTotalLabel;
    @FXML    private Button printButton;
    @FXML    private TextField invoiceNumber;
    @FXML    private Label grandTotalLabel11;
    @FXML    private TextField searchField;
    @FXML    private Button searchButton;
    @FXML    private TableView<SearchTableClass> invoiceSearchTable;
    @FXML    private TableColumn<SearchTableClass, String> invoiceNumber_Col;
    @FXML    private TableColumn<SearchTableClass, String> customer_col;
    @FXML    private TableColumn<SearchTableClass, String> date_col,customerID_col;
    @FXML    private TableColumn<SearchTableClass, Float> amout_Col;
    @FXML    private Label customerName_Label;
    @FXML    private Label customerContact_Label;
    @FXML    private Label address_Label;
    @FXML    private Label dateInvoice_Label,customerID_Label;
    private Connection con;
    private ObservableList<InvoiceTable_HistoryClass> invoiceList;
    private float oldBalance;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            con = DB_Connection.getConnection();
            initTables();
            events();
//            searchInvioceInDatabase();
//            invoiceSearchTable.requestFocus();
//            invoiceSearchTable.getSelectionModel().select(0);
//            invoiceDetailsDisplay();
//            System.exit(0);
    }
    private void events(){
        searchField.setOnKeyReleased(evt->{
            if(evt.getCode() == KeyCode.ENTER){
                searchInvioceInDatabase();
            }
        });
        searchButton.setOnAction(evt->{
            searchInvioceInDatabase();
        });
        
        invoiceSearchTable.setOnKeyReleased(evt->{
           invoiceDetailsDisplay();
        });
        invoiceSearchTable.setOnMouseClicked(evt->{
             invoiceDetailsDisplay();
        });
        
       
        printButton.setOnAction(evt->{
           printBill();
        });
        
    }
    
    private void initTables(){
        invoiceNumber_Col.setCellValueFactory(new PropertyValueFactory<SearchTableClass , String>("invoice"));
        customer_col.setCellValueFactory(new PropertyValueFactory<SearchTableClass , String>("name"));
        date_col.setCellValueFactory(new PropertyValueFactory<SearchTableClass , String>("date"));
        amout_Col.setCellValueFactory(new PropertyValueFactory<SearchTableClass , Float>("amount"));
        customerID_col.setCellValueFactory(new PropertyValueFactory<SearchTableClass, String>("customerId"));
       
        
        invoiceSrColumn.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , Integer>("sr"));
        productCode_InvoiceColumn.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , String>("code"));
        productNameInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , TextField>("name"));
        unitPriceInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , TextField>("unitPrice"));
        UnitsInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , TextField>("units"));
        totalInvoice.setCellValueFactory(new PropertyValueFactory<InvoiceTable_HistoryClass , Label>("total"));
        invoiceList = FXCollections.observableArrayList();
    }
    public class InvoiceTable_HistoryClass{
        String code;
        TextField name, unitPrice, units;
        Label total;
        int sr;

        public InvoiceTable_HistoryClass(String code, TextField name, TextField unitPrice, TextField units, Label total, int sr) {
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
    ObservableList<SearchTableClass> searchList;
    private void searchInvioceInDatabase(){
        
        searchList = FXCollections.observableArrayList();
        
        List<String> idList= new ArrayList<String>();
        try {
            String search = searchField.getText();
            if(search.length()==0)return; 
            PreparedStatement stm = null;
            ResultSet rs  = null;
            
            if(Validation.Only_Number_Color(search, 10)){
                  stm = con.prepareStatement("select sum(invoicedetails.total) as 'abc', invoice.invoiceNumber,invoice.customerId,  invoice.displayDate from invoice inner join invoicedetails on "
                           + "invoice.invoiceNumber = invoicedetails.invoicenumber where invoice.invoiceNumber like '%"+search+"%' GROUP BY invoice.invoiceNumber");
                   rs = stm.executeQuery();
                   while(rs.next()){
                       String number = rs.getString("invoice.invoiceNumber");
                       String displayDate  = rs.getString("invoice.displayDate");
                       if(displayDate != null && displayDate.length() > 3){
                            displayDate = Validation.Reversing_Date(displayDate);
                       }
                       String cstmId  = rs.getString("invoice.customerId");
                       String tt = rs.getString("abc");
                       if(tt == null || tt.equalsIgnoreCase("null")) tt="0";
                       float total =(float) Float.parseFloat(tt);
                       System.out.println("Custmer search for id "+search);
                       String customerName = Database_Returns.Code_Return("customer", "customerId", cstmId, "name");
                       if(customerName.equalsIgnoreCase("not exsists"))customerName=" ";
                       searchList.add(new SearchTableClass(number, customerName, displayDate, cstmId, total));

                   }
                invoiceSearchTable.setItems(searchList);
                invoiceNumber_Col.setSortType(TableColumn.SortType.DESCENDING);
                invoiceSearchTable.getSortOrder().setAll(invoiceNumber_Col);
                return;
            }
            
            
            // Search by date
                if(search.contains("/") || search.contains("-")){
                    
                 search = dateStandard(search);
                 if(search.equalsIgnoreCase("Invalid"))return;
                  int date = Validation.date_to_int(search);
                  // handle / in date formate
                  stm = con.prepareStatement("select  *, sum(invoicedetails.total) as 'abc' from invoice inner join invoicedetails on "
                           + "invoice.invoiceNumber = invoicedetails.invoicenumber  where invoice.date=?  GROUP BY invoice.invoiceNumber");
                  stm.setInt(1, date);
                  rs = stm.executeQuery();
                   while(rs.next()){
                       String number = rs.getString("invoice.invoiceNumber");
                       String displayDate  = rs.getString("invoice.displayDate");
                       if(displayDate != null && displayDate.length() > 3){
                            displayDate = Validation.Reversing_Date(displayDate);
                       }
                       String cstmId  = rs.getString("invoice.customerId");
                       String tt = rs.getString("abc");
                       if(tt == null || tt.equalsIgnoreCase("null")) tt="0";
                       float total =(float) Float.parseFloat(tt);
                       String customerName = Database_Returns.Code_Return("customer", "customerId", cstmId, "name");
                       if(customerName.equalsIgnoreCase("not exsists"))customerName=" ";
                       searchList.add(new SearchTableClass(number, customerName, displayDate, cstmId, total));
                   }
                invoiceSearchTable.setItems(searchList);
                invoiceNumber_Col.setSortType(TableColumn.SortType.DESCENDING);
                invoiceSearchTable.getSortOrder().setAll(invoiceNumber_Col);
                return;
               }
            
               
            stm = con.prepareStatement("select customerId from customer where name like '%"+search+"%'");
            rs = stm.executeQuery();
            while(rs.next()){
                 idList.add(rs.getString("customerId"));
            }
            for (int i = 0; i < idList.size(); i++) {
                 stm = con.prepareStatement("select sum(invoicedetails.total) as 'abc', invoice.invoiceNumber,invoice.customerId,  invoice.displayDate from invoice inner join invoicedetails on "
                    + "invoice.invoiceNumber = invoicedetails.invoicenumber where invoice.customerId like '%"+idList.get(i)+"%' GROUP BY invoice.invoiceNumber ");
                 rs = stm.executeQuery();
                 while(rs.next()){
                        String number = rs.getString("invoice.invoiceNumber");
                        String displayDate  = rs.getString("invoice.displayDate");
                        if(displayDate != "" && displayDate.length() > 3){
                            displayDate = Validation.Reversing_Date(displayDate);
                        }
                        String cstmId  = rs.getString("invoice.customerId");
                        String tt = rs.getString("abc");
                        if(tt == null || tt.equalsIgnoreCase("null")) tt="0";
                        float total =(float) Float.parseFloat(tt);
                        String customerName = Database_Returns.Code_Return("customer", "customerId", cstmId, "name");
                        if(customerName.equalsIgnoreCase("not exsists"))customerName=" ";
                        searchList.add(new SearchTableClass(number, customerName, displayDate, idList.get(i), total));
                 } 
            }
              invoiceSearchTable.setItems(searchList);
              invoiceNumber_Col.setSortType(TableColumn.SortType.DESCENDING);
              invoiceSearchTable.getSortOrder().setAll(invoiceNumber_Col);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class SearchTableClass{
        String invoice, name,date,customerId;
        float amount;

        public SearchTableClass(String invoice, String name, String date, String customerId, float amount) {
            this.invoice = invoice;
            this.name = name;
            this.date = date;
            this.customerId = customerId;
            this.amount = amount;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

            

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
        
    }//end of class
    
    private void invoiceDetailsDisplay(){
         if(invoiceSearchTable.getSelectionModel().getSelectedItem() == null)return;
            
            invoiceList = FXCollections.observableArrayList();
            SearchTableClass row = invoiceSearchTable.getSelectionModel().getSelectedItem();
            String invoiceNum = row.getInvoice();
            String customerId = row.getCustomerId();
            String customerName = row.getName();
            float amount = row.getAmount();
            String dateD = row.getDate();
            oldBalance = row.getAmount();
            
            try {
            PreparedStatement stm = con.prepareStatement("select contact, address from customer where customerId=?");
            stm.setString(1, customerId);
                System.out.println("Queryfor customer id is "+customerId);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                String contact=  rs.getString("contact");
                String address = rs.getString("address");
                invoiceNumber.setText(invoiceNum);
                customerName_Label.setText(customerName);
                address_Label.setText(address);
                customerContact_Label.setText(contact);
                dateInvoice_Label.setText(dateD);
                customerID_Label.setText(customerId);
            }
            
            stm = con.prepareStatement("select * from invoicedetails where invoiceNumber=?");
            stm.setString(1, invoiceNum);
            rs = stm.executeQuery();
            while(rs.next()){
                int serial = rs.getInt("serial");
                String productName1 = rs.getString("productName");
                String code  = rs.getString("Sr");
                String units=  rs.getString("units");
                String price1 = rs.getString("price");
                String total1 = rs.getString("total");
                
                TextField price = new TextField(price1);
                price.setAlignment(Pos.CENTER);
                price.setEditable(false);
                TextField items = new TextField(units);
                items.setEditable(false);
                TextField productName = new TextField(productName1);
                productName.setEditable(false);
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
                invoiceList.add(new InvoiceTable_HistoryClass(code, productName, price, items, total,0));
                calculateGrandTotal();
                invoiceTable.setItems(invoiceList);
            }//end of loop
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void calculate(TextField price, TextField items, Label total){
        if(!Validation.Only_Number_Color(items, 6)){
            total.setText("Invalid");
            return;
        }
        if(!Validation.product_Price_color(price, 1000000)){
            total.setText("Invalid");
            return;
        }
        int units = Integer.parseInt(items.getText());
        float priceD = Float.parseFloat(price.getText());
        float t =(float) units * priceD;
        total.setText(String.valueOf(t));
        calculateGrandTotal();
       
    }
    private boolean calculateGrandTotal(){
        float gTotal =0;
        boolean stats= true;
        for (int i = 0; i < invoiceList.size(); i++) {
            
            invoiceList.get(i).setSr(i+1);
            if(Validation.Only_Number_Color(invoiceList.get(i).getUnits(), 4) && Validation.product_Price_color(invoiceList.get(i).getUnitPrice(), 10000
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
            
            printButton.setDisable(false);
        }else{
            grandTotalLabel.setText("Invalid Entry in Invoice Table");
            printButton.setDisable(true);
            return false;
        }
       // calculateBalance();
     return true;   
    }  
     
     
   
     private String dateStandard(String date){
         if(Validation.validDate(date) == false)return "Invalid";
         if(date.contains("-") && date.contains("/"))return "Invalid";
         if(date.length() != 10 && date.length() != 9 && date.length() !=8)return "Invalid";
         if(date.contains("-")){
             String split[] = date.split("-");
             String day = split[0];
            String month = split[1];
             String year =split[2];
             if(day.length()==1){
                 day = "0"+day;
              }       
             if(month.length()==1){
             month = "0"+month;
             }
         
             date  = year+"/" +month+"/"+day;
             return date;
         }else{
         String split[] = date.split("/");
         String day = split[0];
         String month = split[1];
         String year =split[2];
         if(day.length()==1){
             day = "0"+day;
         }       
         if(month.length()==1){
             month = "0"+month;
         }
         
         date  = year+"/" +month+"/"+day;
         return date;
         }
          
     }
     
     private void printBill(){
           if(calculateGrandTotal()==false){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning! Can't print");
                a.setContentText("Please Enter Valid value in Invoice Table, Before Print");
                a.show();
                return;
            }
            
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
             alert.setTitle("Message");
             String s = "Do you Want to Save Edited Invoice";
             alert.setContentText(s);
             Optional<ButtonType> result = alert.showAndWait();
            if(!(result.get() == ButtonType.OK)) {
               return;
             } 
            
         if(invoiceList.size()<=0){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Message");
            a.setContentText("Your Invice is empty. Can't Print");
            a.show();
            return;
         }
          new HistoryInvoice_Print(invoiceList,getCustomerBalance(), grandTotalLabel.getText(), dateInvoice_Label.getText(), invoiceNumber.getText(), customerName_Label.getText(), customerContact_Label.getText() , address_Label.getText());

         
     }
     
     private float getCustomerBalance(){
         float balance=0;
         try {
             PreparedStatement stm = con.prepareStatement("select balance from customer where customerId =?");
             stm.setString(1, customerID_Label.getText());
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
