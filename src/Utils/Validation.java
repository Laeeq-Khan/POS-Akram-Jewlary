 
package Utils;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class Validation {
    
    
    public static void List_Jump_to_String(ComboBox box , String valeue){
     int size =  box.getItems().size();
                   for (int i = 0; i < size; i++) {
                       box.getSelectionModel().select(i);
                       if( box.getValue().toString().equalsIgnoreCase(valeue)) break;
                   }
}//end of list_Selector function
    
     
    public static boolean  List_Jump_To_Character(String c ,  ComboBox list){
     if(list.getSelectionModel().getSelectedIndex() == -1) return false;
        if(c.matches("[A-Za-z]") && c.length() == 1 || c.matches("[0-9]") && c.length() == 1){
         int total = list.getItems().size(); 
          c = c.toUpperCase();
          
          for (int i = 0; i < total; i++) {
             list.getSelectionModel().select(i);
             String abc  = list.getValue().toString();
              char character = abc.charAt(0);
              String charr =  String.valueOf(character);
              if(charr.equalsIgnoreCase(c)){
                  break;
              }//end of if 
          
          }//end of for loop 
        return true;  
        }else{
            return false;
        }
      }//end of funciton
      
      public static final LocalDate Default_Date (String datee){
          LocalDate localDate = null; 
          try {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          localDate = LocalDate.parse(datee , formatter);
           } catch (Exception e) {
               System.out.println("Error in validation class error in local date function "+e);
           }
          return localDate;
    }
      
      public static String Reversing_Date(String date){
                 String yy = date.substring(0, 4);
                 String dd = date.substring(5, 7);
                 String mm = date.substring(8, 10);
                  date = mm+"/"+dd+"/"+yy;
                  return date;
      }
      
      public static String encrypt(String str){
        int code;
        String result = "";
        for (int i = 0; i < str.length(); i++) {
          code = Math.round((float) Math.random()*8+1);
          result += code + Integer.toHexString( ((int) str.charAt(i) ) ^ code )+"-";
        }
        return result.substring(0, result.lastIndexOf("-"));
  }

  public static String decrypt(String str){
        str = str.replace("-", "");
        String result = "";
        for (int i = 0; i < str.length(); i+=3) {
          String hex =  str.substring(i+1, i+3);
          result += (char) (Integer.parseInt(hex, 16) ^ (Integer.parseInt(String.valueOf(str.charAt(i)))));
        }
        return result;
  }
      public static boolean validDate(String date){
        String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex).compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
      }
      public static int date_to_int(String date){ // YYYY // MM/ DD
                 String yy = date.substring(0, 4);
                 String mm = date.substring(5, 7);
                 String dd = date.substring(8, 10);
                 int y = Integer.parseInt(yy);
                 int m = Integer.parseInt(mm);
                 int d =  Integer.parseInt(dd);
                 
                 String complete = y+""+m+""+d;
                 int completeInt=  Integer.parseInt(complete);
                 return completeInt;
                 
      }
      
     public static String today_Date(){
         Date dd = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(dd);
         return date;
     } 
      
   public static boolean Only_Text_Diaglog( TextField field, int length , String name){
      
       if(field.getText().length()==0){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("Please Enter Product Name");
         alert.show();
         field.requestFocus();
          return false;
       }
       
      if(field.getText().length() > length){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("Max  "+length+" Character are allowed in "+name+" filed ");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement   
        
      if(!(field.getText().matches("[ a-zA-z]+(['-][a-zA-Z]+)*"))){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("Only Text is allowed in "+name+" filed ");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement 
        
        return true;
    }//end of functioin  
   public static boolean Only_Number_Diaglog( TextField field, int length, String name){
     
         if(field.getText().length() == 0){
         Alert a = new Alert(AlertType.WARNING);
         a.setTitle("Field is Empty");
         a.setContentText("Your Field "+name+" is empty which is Mandatory befor save");
         a.show();
         field.requestFocus();
         return false;
          }
          if(field.getText().length() > length){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("You have done mistake! \n Max  "+length+" Character are allowed in "+name+" filed ");
         alert.showAndWait();
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement   
        
   
      if(!(field.getText().matches("[0-9]*") )){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setHeaderText("Only Text is allowed in "+name+" filed ");
         alert.setContentText("You have done mistake!\n Only Text is allowed in "+name+" filed . Check your Entered Data Please . or your Field is Empty");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement 
         
        return true;
    }//end of functioin    
   public static boolean Name_Diaglog( TextField field, int length , String name){
      
      if(field.getText().length() > length){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("You have done mistake! \n Max  "+length+" Character are allowed in "+name+" Field  ");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement   
        
      if(!(field.getText().matches("[ a-zA-z]+([ '-][a-zA-Z]+)*"))){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("You have done mistake! \n Only Text is allowed in "+name+" filed or your Field is Empty");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement 
        
        return true;
    }//end of functioin
    
    
   
   public static boolean Only_Text_Color(  TextField field, int length ){
       if(field.getText() == null)return false;
      if(field.getText().length() > length){
            field.setStyle("-fx-text-inner-color:#ff0033;");
          return false;
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
        
      if(!(field.getText().matches("[ a-zA-z]+([ '-][a-zA-Z]+)*"))){
            field.setStyle("-fx-text-inner-color:#ff0033;");
          return false;
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
        
        return true;
    }//end of functioin  
   
   public static boolean Only_Number_Color( TextField field, int length ){
       if(field.getText().length() == 0) return false;
       
      if(field.getText().length() > length){
          field.setStyle("-fx-text-inner-color:#ff0033;");
          return false;
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
//end of if statement   
        
      if(!(field.getText().matches("[0-9]*") )){
            field.setStyle("-fx-text-inner-color:#ff0033;");
          return false;
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
         
        return true;
    }//end of functioin  
   
    public static boolean Only_Number_Color( String field, int length ){
       if(field.length() == 0) return false;
       
      if(field.length() > length){
          return false;
      }else{
      }      
      if(!(field.matches("[0-9]*") )){
          return false;
      }else{
      }
         
        return true;
    }//end of functioin  
   
   public static boolean Name_Color(TextField field, int length  ){
      
      if(field.getText().length() > length){
          field.setStyle("-fx-text-inner-color:#ff0033;");
      }else if(!(field.getText().matches("[ a-zA-z]+([ '-][a-zA-Z]+)*"))){
         field.setStyle("-fx-text-inner-color:#ff0033;");
            System.out.println("typing");
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
    
        return true;
    }//end of functioin
    
    public static boolean Name_Diaglog_with_Dot(String text , TextField field, int length , String name){
       if(text.length() > length){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("You have done mistake! \n Max  "+length+" Character are allowed in "+name+" Field  ");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement   
        
      if(!(text.matches("[ a-zA-z]+([ '-./()][a-zA-Z() ]+)*"))){
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning Dialog");
         alert.setContentText("You have done mistake! \n Only Text is allowed in "+name+" filed or your Field is Empty");
         alert.show();
         field.requestFocus();
          return false;
      }//end of if statement 
        
        return true;
        
    }//end of function
     public static boolean Name_Color_with_dot(TextField field, int length  ){
       if(field.getText().length() > length){
          field.setStyle("-fx-text-inner-color:#ff0033;");
      }else if(!(field.getText().matches("[ a-zA-z]+([ '-./()][a-zA-Z() ]+)*"))){
         field.setStyle("-fx-text-inner-color:#ff0033;");
            System.out.println("typing");
      }else{
             field.setStyle("-fx-text-inner-color:black;");
      }
    
        return true;
    }//end of functioin
    
   
   public static boolean valid_Length_Validation(TextField filed , int length){
       
       if(filed.getText().length() > length){
            filed.setStyle("-fx-text-inner-color: #ff0033;");
            return false;
       }else{
           filed.setStyle("-fx-text-inner-color: black");
       }
       
       
       if(!(filed.getText().matches("[ a-zA-z]+([0-9 '-][a-zA-Z-0-9]+)*"))){
         filed.setStyle("-fx-text-inner-color:#ff0033;");
            System.out.println("typing");
      }else{
             filed.setStyle("-fx-text-inner-color:black;");
      }
      
       return true;
   }// functioin here end
   
   
   
   public static boolean valid_Date(DatePicker datee){
       try {
           if(!(datee.getValue() == null)){
             String abc = datee.getValue().format(DateTimeFormatter.ofPattern("MM-dd-YYYY"));
           datee.setStyle("-fx-text-inner-color:#ff0033;");
           
           if(!(abc.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})"))){
                datee.setStyle("-fx-text-inner-color:#ff0033;");
           }else{
                datee.setStyle("-fx-text-inner-color: black;");
           }
     }else{
          datee.setStyle("-fx-text-inner-color: red;");
     }
       System.out.println(datee.getValue());
       } catch (Exception e) {
           System.out.println("Validation Class function valid date"+e);
       }
 return true;
   }//end of function
   
   
   public static boolean List_Validation(ComboBox list , String listname ){
       if(list.getSelectionModel().isSelected(-1)){
           Alert a = new Alert(AlertType.WARNING);
           a.setTitle("Data validation problem");
           a.setContentText("You have not select any data from "+listname+" List . ");
           a.showAndWait();
           list.show();
            return false;
       }
       
       
       return true;
   }
   
   public static boolean Min_Max_Length_Validation(TextField field , int max , int min , String name){
       Alert a = new Alert(AlertType.WARNING);
       a.setTitle("Error in "+name+" Field");
               
       if(field.getText().length() > max){
           a.setContentText("Max "+max+" character are allowed in "+name+" field");
           a.showAndWait();
           field.requestFocus();
       return false;
       }
       if(field.getText().length() < min){
           a.setContentText("Minimum "+min+" Characters are allowed in "+name+" Field");
           a.showAndWait();
           field.requestFocus();
           return false;
       }
       
       return true;
   }
   
   
      public static boolean product_Price_color( TextField field  , int max){
       
       if(field.getText().length() == 0) return false;

       if(!(field.getText().matches("[+-]?([0-9]*[.])?[0-9]+"))){
            field.setStyle("-fx-text-inner-color:#FC7104;");
             return false;
       }else{
           field.setStyle("-fx-text-inner-color:black;");
       }
       
       if(field.getText().matches("[+-]?([0-9]*[.])?[0-9]+")){
           float ff = Float.parseFloat(field.getText());
           if(ff>max){
                field.setStyle("-fx-text-inner-color:#FC7104;");
                return false;
           }else{
                field.setStyle("-fx-text-inner-color:black;");
                
           }
       }
     
      return true;
   }
       
       public static boolean product_Price_color( String field  , int max){
       
       if(field.length() == 0) return false;

       if(!(field.matches("[+-]?([0-9]*[.])?[0-9]+"))){
             return false;
       }else{
       }
       
       if(field.matches("[+-]?([0-9]*[.])?[0-9]+")){
           float ff = Float.parseFloat(field);
           if(ff>max){
                return false;
           }else{
                
           }
       }
     
      return true;
   }

   
   public static boolean product_price_dialog( TextField field, int length , int max ,String msg){
       
       
       if(field.getText().length() ==0){
             Alert a  = new Alert(AlertType.WARNING);
            a.setTitle("Wrong Marks Entry");
            a.setContentText("Enter "+msg);
            a.show();
            field.requestFocus();
            return false;
       }
        Alert a  = new Alert(AlertType.WARNING);
       a.setTitle("Wrong Marks Entry");
       a.setContentText(""+msg+" is Wrong. Check it and try again");
      
       if(!(field.getText().matches("[+-]?([0-9]*[.])?[0-9]+"))){
             a.showAndWait();
             field.requestFocus();
             return false;
       } 
       
       if(field.getText().matches("[+-]?([0-9]*[.])?[0-9]+")){
          double ff = Double.parseDouble(field.getText());
           if(ff>max){
                   a.setContentText("Max "+max+"  Amount is allowed");
                   a.showAndWait();
                   field.requestFocus();
                return false;
           } 
       }
       if(field.getText().length() > length ){
            a.showAndWait();
            field.requestFocus();
          return false;
      }
       
      return true;
   }
   
   
   public  static void capitalizeString(TextField field) {
    String string = field.getText();
  char[] chars = string.toLowerCase().toCharArray();
  boolean found = false;
  for (int i = 0; i < chars.length; i++) {
    if (!found && Character.isLetter(chars[i])) {
      chars[i] = Character.toUpperCase(chars[i]);
      found = true;
    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
      found = false;
    }
  }
  string =  String.valueOf(chars);
  field.setText(string);
     
}//end of funciton
   
}//end of class 


/*
 
   public void search_into_Table(){
        search_field.setOnKeyReleased(evt->{
            if(search_field.getText().length() ==0){
               table.setItems(list);
               return;
            }
        ObservableList<Table_class>  tb_items = FXCollections.observableArrayList();
        ObservableList<TableColumn<Table_class  , ?>>  cols = table.getColumns();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < cols.size(); j++) {
                TableColumn col = cols.get(j);
                String cell_value  = col.getCellData(list.get(i)).toString();
                cell_value = cell_value.toLowerCase();
                if(cell_value.contains(search_field.getText().toLowerCase())){
                    tb_items.add(list.get(i));
                            break;
                }
             }
        }
        table.setItems(tb_items);
        });
     }
*/