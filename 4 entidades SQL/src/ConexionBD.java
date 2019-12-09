
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static Connection conn;
    private static final String driver= "com.mysql.jdbc.Driver";
    private static final String user= "root";
    private static final String password= "";
    private static final String url= "jdbc:mysql://localhost:3306/cuatro_entidades";
   

    public static void printSQLException(SQLException e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public Connection conectar(){
            conn = null;
        try{
            Class.forName(driver);
            conn =  (Connection) DriverManager.getConnection(url, user, password);
            
            if(conn!= null){
                System.out.println("Conexion establecida");
            }
            
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error al conectar " +e);
        }
        
        return conn; 
    }
    

    public Connection getConnection(){
        return conn;
    }

    public void disconnect(){
    conn = null;
        if(conn==null){
            System.out.println("Conexion terminada");
        }
    
    }
    

    
}
