/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS.Invoices;

import Utils.DB_Connection;
import Utils.Validation;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Laeeq-Khan
 */
public class Sale_ReportController implements Initializable {
  
    
    @FXML    private DatePicker fromDate;
    @FXML    private DatePicker toDate;
    @FXML    private Button searchButton;
    @FXML    private TableView<ReportTableClass> reportTable;
    @FXML    private TableColumn<ReportTableClass, String> customerID_col;
    @FXML    private TableColumn<ReportTableClass, String> name_col;
    @FXML    private TableColumn<ReportTableClass, String> invoice_col;
    @FXML    private TableColumn<ReportTableClass, Float> amountCol;
    @FXML    private TableColumn<ReportTableClass, String> date_col;
    @FXML    private TableColumn<ReportTableClass, String> time_col;
    @FXML    private Label totalAmount;
    ObservableList<ReportTableClass> list;
    
    
    public void initialize(URL url, ResourceBundle rb) {
            events();
            initTable();
           
            Date date = new Date();  
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
            String strDate= formatter.format(date);
            fromDate.setValue(Validation.Default_Date(strDate));
            toDate.setValue(Validation.Default_Date(strDate));
    }    
    
    private void events(){
        searchButton.setOnAction(evt->{
            searchRecords();
        });
    }
    
    
        private void searchRecords(){
          LocalDate dateFrom = fromDate.getValue();
          LocalDate dateTo  = toDate.getValue();
          if(dateFrom == null || dateTo == null) return;

          // Convert LocalDate to java.sql.Date
          java.sql.Date sqlDateFrom = java.sql.Date.valueOf(dateFrom);
          java.sql.Date sqlDateTo = java.sql.Date.valueOf(dateTo);

          float total = 0;
          try {
             String query = "SELECT SUM(invoicedetails.total) AS abc, invoice.time, customer.name, " +
                            "invoice.invoiceNumber, invoice.customerId, invoice.displayDate " +
                            "FROM invoice " +
                            "INNER JOIN invoicedetails ON invoice.invoiceNumber = invoicedetails.invoicenumber " +
                            "INNER JOIN customer ON invoice.customerId = customer.customerId " +
                            "WHERE invoice.displayDate  >= ? AND invoice.displayDate <= ? " +
                            "GROUP BY invoice.invoiceNumber";

              PreparedStatement stm = DB_Connection.getConnection().prepareStatement(query);
              stm.setDate(1, sqlDateFrom);
              stm.setDate(2, sqlDateTo);
              ResultSet rs = stm.executeQuery();

              list.clear();
              while (rs.next()) {
                  String customerName = rs.getString("name"); // Alias or column name should be used directly
                  String customerId = rs.getString("customerId");
                  String displayDate = rs.getString("displayDate");
                  displayDate = Validation.Reversing_Date(displayDate);
                  String time = rs.getString("time");
                  float amount = rs.getFloat("abc");
                  total += amount;
                  String invoice = rs.getString("invoiceNumber");
                  list.add(new ReportTableClass(customerId, customerName, invoice, displayDate, time, amount));
              }

              reportTable.setItems(list);
              totalAmount.setText(String.valueOf(total));
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

    private void initTable(){
        customerID_col.setCellValueFactory(new PropertyValueFactory<ReportTableClass, String>("customerId"));
        name_col.setCellValueFactory(new PropertyValueFactory<ReportTableClass, String>("customerName"));
        invoice_col.setCellValueFactory(new PropertyValueFactory<ReportTableClass, String>("invoice"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ReportTableClass, Float>("amount"));
        date_col.setCellValueFactory(new PropertyValueFactory<ReportTableClass, String>("date"));
        time_col.setCellValueFactory(new PropertyValueFactory<ReportTableClass, String>("time"));
        list = FXCollections.observableArrayList();
        
    }
    public class ReportTableClass{
        String customerId, customerName, invoice,date,time;
        float amount;

        public ReportTableClass(String customerId, String customerName, String invoice, String date, String time, float amount) {
            this.customerId = customerId;
            this.customerName = customerName;
            this.invoice = invoice;
            this.date = date;
            this.time = time;
            this.amount = amount;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
        
        
    }
    
}
