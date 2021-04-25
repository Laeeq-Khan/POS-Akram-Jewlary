
package Utils;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database_Returns {
    

      
    public static String Code_Return(String table ,String matchingColumn , String name , String returnColumn){
        String abc = null;
        try {   
            Statement st = DB_Connection.getConnection().createStatement();
            String query = "select "+returnColumn+"  from "+table+" where  "+matchingColumn+" = '"+name+"' ";
            ResultSet rs = st.executeQuery(query);
           if(rs.next()){
                abc = rs.getString(returnColumn);
           }else{
                System.out.println(returnColumn+" no exist in "+table+" Table");   
                return "Not Exsists";
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("database returnings class . code return functioin  "+e);
        }//end of catch statement   
        return abc;
    }//end of funciton
    
       
       public static int getNextAutoIncrement(String table, String column , int start){
        String query = "select auto_increment  from information_schema.TABLES where TABLE_NAME ='"+table+"' and TABLE_SCHEMA='pos'";
        PreparedStatement statement; 
        Connection con = DB_Connection.getConnection();
        try {
            statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                int next = rs.getInt("auto_increment");
                return next;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return start;
    }
}//end of clss
