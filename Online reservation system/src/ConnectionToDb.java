import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionToDb{
    private static    String jdbcUrl = "jdbc:mysql://localhost:3306/ors";
    private static    String host = "root";
    private static    String hostPassword = "9581494976@Uu";
    public static Connection connect() throws SQLException
    {
        Connection connection = DriverManager.getConnection(jdbcUrl, host, hostPassword);
        
        return connection;
    }
}
