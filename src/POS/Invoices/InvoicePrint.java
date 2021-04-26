package POS.Invoices;



import POS.Invoices.CreateInvoiceController.InvoiceTable_Class;
import POS.Invoices.EditOldInvoice_Controller;
import POS.Invoices.EditOldInvoice_Controller.InvoiceTable_Class_Edit;
import Utils.DB_Connection;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class InvoicePrint implements Printable, ActionListener {

 private static Paper paper;
private ObservableList<InvoiceTable_Class> invoiceList ;
private String items="" , discount="" , totalBill="" ;
String date="" , invoiceNumber ="", soldTo ="", address="";
int[] pageBreaks;  // array of page break line positions.
String[] textLines;

    int totalPages = 0;
    private void initTextLines() {
        if (textLines == null) {
            int numLines=invoiceList.size();
            textLines = new String[numLines];
            for (int i=0;i<numLines;i++) {
                textLines[i]= "" + i;
            }
        }
        totalPages =(int)invoiceList.size() / 20;
    }
    int numBreaks=0;
 public int print(Graphics g, PageFormat pf, int pageIndex)
             throws PrinterException {
        Font font = new Font("Serif", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        
        if (pageBreaks == null) {
            initTextLines();
            int linesPerPage = 20;//(int)(pf.getImageableHeight()/lineHeight);
             numBreaks = (textLines.length-1)/linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b=0; b<numBreaks; b++) {
                pageBreaks[b] = (b+1)*linesPerPage; 
            }
        }
        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length)
                         ? textLines.length : pageBreaks[pageIndex];
  
        int x = 60 ,  y = 70 ; // these two are using to totaly change the posiciton of whole document form x to y cordinates
        Color gray = new Color(205, 205,205);
    
        // Pixcels width = 420 height 595
        int width = 430;
       
        
        g.setColor(gray);
        g.fillRect(x, y, 330, 15);
        g.setColor(Color.BLACK);
        
        g.setFont(new Font("default" , Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        g.setFont(new Font("default" , Font.PLAIN, 9));
        
        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm:ss a");
        Date datee = new Date();  
        String time = formatDate.format(datee);
        
        g.drawString("Time "+time, width-210 , y-5);
        g.drawString("Page "+(pageIndex+1)+" of "+ (totalPages+1), width-110 , y-5);
        g.setFont(new Font("default" , Font.PLAIN, 10));
        g.drawString("For Publicity", (width/2), y+12);
       
        g.drawString("Invoice Number : ", x, y+30);
        g.drawString(invoiceNumber, x+80, y+30);
        
        g.drawString("Date  :", (width/2)+90, y+30);
        g.drawString(date, (width/2)+125, y+30);
        
        g.drawString("Address :", (width/2)+80, y+45);
        g.drawString(address, (width/2)+125, y+45);
        
        
        g.drawString("Customer Name : "+soldTo, x, y+45);
        
     
        
        Color gray2 = new Color(225, 225,225);
        g.setColor(gray2);
        g.fillRect(x, y+48, 330, 15);
        g.setColor(Color.BLACK);
        
        // Bill header
         g.setFont(new Font("default" , Font.PLAIN, 9));
         g.drawString("Sr#", x+5, y+58);
         g.drawString("Goods Descriptions", x+48, y+58);
         g.drawString("Quantity", x+163, y+58);
         g.drawString("Item Price", x+208, y+58);
         g.drawString("Amount", x+280, y+58);
         
         //column lines
        
         
         g.setColor(Color.BLACK);
        
        float subTotal = 0;
        int totalQnty = 0;
 for (int line = start; line < end; line++) {
        y += lineHeight;
            
        InvoiceTable_Class row = invoiceList.get(line);
        String name = row.getName().getText();
        String items = row.getUnits().getText();
        String rate = row.getUnitPrice().getText();
        String amount = row.getTotal().getText();
        String sr = String.valueOf(row.getSr());
        g.setColor(gray);
        g.drawLine(x, y+46, x+330, y+46); // row line
        g.setColor(Color.BLACK);
       
        if(name.length() >=20)
          g.setFont(new Font("default" , Font.PLAIN , 8));
        else
          g.setFont(new Font("default" , Font.PLAIN , 9));
        g.drawString(name, x+38, y+60); 
 
         g.setFont(new Font("default" , Font.PLAIN , 9));   
         g.drawString(sr, x+3, y+60);
         g.drawString(items, x+165, y+58);
         g.drawString(rate, x+205, y+58);
         g.drawString(amount+"/-", x+270, y+58);
       
         
          g.setColor(gray);
         g.drawLine(x, y+30+lineHeight, x, y+30+lineHeight+lineHeight); //first line
         g.drawLine(x+30, y+30+lineHeight, x+30, y+30+lineHeight+lineHeight);//second line
         g.drawLine(x+158 ,y+30+lineHeight, x+158, y+30+lineHeight+lineHeight);//3rd line
         g.drawLine(x+200, y+30+lineHeight, x+200, y+30+lineHeight+lineHeight);//fourth line
         g.drawLine(x+265, y+30+lineHeight, x+265, y+30+lineHeight+lineHeight);//fifth
         g.drawLine(x+330, y+30+lineHeight, x+330, y+30+lineHeight+lineHeight); //6th line
         
         float sub = Float.parseFloat(amount);
         subTotal +=sub;
         int qty = Integer.parseInt(items);
         totalQnty +=qty;
         if(line == end-1){
            g.drawLine(x,y+30+lineHeight+lineHeight, x+330,y+30+lineHeight+lineHeight); // row lines
            g.setColor(gray);
            g.fillRect(x+200, y+30+lineHeight+lineHeight, 130, 15);
            g.setColor(Color.black);
            g.setFont(new Font("default" , Font.BOLD , 9));   
            g.drawString("Sub Total : "+subTotal+"/-" , x+203, y+30+lineHeight+lineHeight+10);
            subTotal=0;
             g.setFont(new Font("default" , Font.PLAIN , 9));   
         }
         
    }
        g.setColor(Color.BLACK);

        y = y+100;
        if(pageIndex == pageBreaks.length){
                
                g.setColor(gray);
                g.fillRect(x+158, y-100+30+lineHeight+lineHeight, 40, 15);
                g.setColor(Color.black);
                g.setFont(new Font("default" , Font.BOLD , 9));   
                g.drawString(""+totalQnty , x+162, y-100+30+lineHeight+lineHeight+10);

                g.setColor(gray);
                g.drawLine(x+160, y-15, x+325, y-15); // row line
                g.drawLine(x+160, y-15, x+160, y+57); // colum line
                g.drawLine(x+325, y-15, x+325, y+57); // colum 2nd line
                
                g.drawLine(x+160, y+lineHeight-10, x+325, y+lineHeight-10); // row line
                g.drawLine(x+160, y+lineHeight+lineHeight-10, x+325, y+lineHeight+lineHeight-10); // row line
                g.drawLine(x+160, y+lineHeight+lineHeight+lineHeight-10, x+325, y+lineHeight+lineHeight+lineHeight-10); // row line
                g.drawLine(x+160, y+lineHeight+lineHeight+lineHeight+lineHeight-10, x+325, y+lineHeight+lineHeight+lineHeight+lineHeight-10); // row line
                
                g.setColor(Color.BLACK);
                y = y+5;
                g.setFont(new Font("default" , Font.BOLD , 9)); 
                g.drawString("Grand Amount :", x+162, y-5);
                g.drawString(totalBill+"/-", x+250, y-5);
                
                
                String paid="0", balance="0", netBalance="0";
                try {
                    PreparedStatement stm = DB_Connection.getConnection().prepareStatement("select paid, balance, netbalance from invoice where invoiceNumber=?");
                    stm.setString(1, invoiceNumber);
                    ResultSet rs = stm.executeQuery();
                    if(rs.next()){
                        paid = rs.getString("paid");
                        balance= rs.getString("balance");
                        netBalance=  rs.getString("netbalance");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                g.drawString("Paid Amount :", x+162, y-5+lineHeight);
                g.drawString(paid+"/-", x+250, y-5+lineHeight);
                
                g.drawString("Previous Balance :", x+162, y-5+lineHeight+lineHeight);
                g.drawString(balance+"/-", x+250, y-5+lineHeight+lineHeight);
                
                g.drawString("Net Balance :", x+162, y-5+lineHeight+lineHeight+lineHeight);
                g.drawString(netBalance+"/-", x+250, y-5+lineHeight+lineHeight+lineHeight);
               
          }
        y = y+250; 
  
   
    /* tell the caller that this page is part of the printed document */
    return PAGE_EXISTS;
}


public void actionPerformed(ActionEvent e) {
   
}


float balance;
float paid;
String customerId;
public InvoicePrint(ObservableList<InvoiceTable_Class> invoiceList,  float paid ,float balance, String totalBill , String date , String invoice , String soldTo , String contact){

   this.paid = paid;
    this.balance = balance;
    this.soldTo  = soldTo;
    this.address = address;
    this.invoiceNumber = invoice;
    this.invoiceList= invoiceList;
    this.totalBill = totalBill;
    this.date  = date;
    
    paper = new Paper();
    paper.setSize(420, 595);

    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);
    boolean ok = true;
    if (ok) {
        try {
            job.print();
        } catch (PrinterException ex) {
            /* The job did not successfully complete */
        }
    }
}


public void bytt(){
    
    byte c = 10;
    byte d = 18;
    
    byte dd = (byte )(c + d);
    
}

}       