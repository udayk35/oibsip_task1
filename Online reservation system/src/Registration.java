import java.sql.SQLException;

public class Registration {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

    public Registration() {
        System.out.println("Welcome to Registration!");
        System.out.print("Enter username: ");
        this.username = Input.getString();

        System.out.print("Enter email: ");
        this.email = Input.getString();

        System.out.print("Enter phone number: ");
        this.phoneNumber = Input.getString();

        System.out.print("Enter password: ");
        this.password = Input.getString();

        System.out.print("Confirm password: ");
        this.confirmPassword = Input.getString();
    }

    public void register() throws SQLException {
        String[] userDetails = new String[] { this.username, this.email, this.phoneNumber, this.password };

        // Validate password
        if (validate()) {
            if (!ValidateUsername.validate(this.username)) {
                System.out.println("\nRegistration failed. Username already taken.\n");
                return;
            }

            InsertIntoDb iDb = new InsertIntoDb(userDetails);
            int loginId = iDb.insert();
            if (loginId > 0) {
                System.out.println("\nRegistration Successful! Your login_id: " + loginId);
            } else {
                System.out.println("\nRegistration failed. Please try again.");
            }
        } else {
            System.out.println("\nRegistration failed. Please fill in all the fields.\n");
        }
    }

    private boolean validate() {
        // Check if any field is empty
        if (this.username.isEmpty() || this.email.isEmpty() || this.phoneNumber.isEmpty()
                || this.password.isEmpty() || this.confirmPassword.isEmpty()) {
            System.out.println("Please fill in all the fields.");
            return false;
        }

        // Check if the password and confirm password match
        if (!this.password.equals(this.confirmPassword)) {
            System.out.println("Password and confirm password do not match.");
            return false;
        }

        // Add more validation as needed

        return true;
    }
}
