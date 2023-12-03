import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertIntoDb {
    String username;
    String email;
    String phno;
    String password;

    InsertIntoDb(String[] userdetails) {
        username = userdetails[0];
        email = userdetails[1];
        phno = userdetails[2];
        password = userdetails[3];
    }

    public int insert() {
      
        try (Connection connection = ConnectionToDb.connect()) {
            // Example: Reading data with a specific login_id
            String insertQuery = "INSERT INTO user ( username,email,phonenumber,password) VALUES (?, ?, ?, ?)";
            String selectQuery = "SELECT login_id FROM user ORDER BY login_id DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phno);
                preparedStatement.setString(4, password);

                preparedStatement.executeUpdate();
                try (PreparedStatement preparedStatement1 = connection.prepareStatement(selectQuery)) {
                    try (ResultSet resultSet = preparedStatement1.executeQuery()) {
                        if (resultSet.next()) {
                            int login_id = resultSet.getInt(1);
                            return login_id;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
