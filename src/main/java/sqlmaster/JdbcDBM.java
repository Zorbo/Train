package sqlmaster;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;


public class JdbcDBM {

    private Connection myConnection = null;

    // dbUrl = "jdbc:mysql://localhost:3306/demo" + "?useSSL=false";
    // user = "student";
    // pass = "student";

    public JdbcDBM(String loadFile) throws SQLException, IOException {

        // 1. Load the properties file
        Properties properties = new Properties();
        // this is the file name, we read the info from here
        properties.load(new FileInputStream(loadFile));

        // getProperty = Get the value after the "="
        String dbUrl = properties.getProperty("dburl");
        String user = properties.getProperty("user");
        String pass = properties.getProperty("password");

        System.out.println("Connecting to database...");
        System.out.println("Database URL: " + dbUrl);
        System.out.println("User: " + user);

        // 3. Get connection to database
        //this will get rid of SSL warning
        myConnection = DriverManager.getConnection(dbUrl + "?useSSL=false", user, pass);
        System.out.println("Database connection successful!\n");

    }

    Connection getMyConnection() {
        return myConnection;
    }

    // HELPER METHODS FROM THIS POINT
    public boolean validateString(String s) {

        return (s != null) && (!(s.isEmpty()));
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public boolean askUserIfOkToSave() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Is it okay to save?  yes/no: ");
        String input = scanner.nextLine();

        scanner.close();

        return input.equalsIgnoreCase("yes");
    }

    public void closeResource() throws SQLException, IOException {

        if (myConnection != null) {
            myConnection.close();
        }
    }

    // ONLY FOR TESTING
    public void display(ResultSet myRs) throws SQLException {
        while (myRs.next()) {
            String lastName = myRs.getString("last_name");
            String firstName = myRs.getString("first_name");
            double salary = myRs.getDouble("salary");
            String department = myRs.getString("department");

            System.out.printf("%s, %s, %.2f, %s\n", lastName, firstName, salary, department);
        }
    }
}