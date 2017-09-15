package sqlmaster;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.*;

public class JdbcDBM {


    private Connection myConnection = null;
    private Statement myStatement = null;
    private ResultSet myResultSet = null;
                                                                      //this will get rid of SSL warning
    private String dbUrl = "jdbc:mysql://localhost:3306/demo" + "?useSSL=false";
    private String user = "student";
    private String pass = "student";

    public JdbcDBM() throws SQLException {

        // 1. Get connection to database, in the constructor you can initilizate THE CONNECTON
        // you can even pass the database parameters here!!!
        // if you initilizate the connecton here you need to use a separate Resource close method
        myConnection = DriverManager.getConnection(dbUrl, user, pass);
        System.out.println("Database connection successful!\n");

    }

    public void insertNewRow(String firstName, String lastName, String email, String department, double salary) throws SQLException {

        if (validateString(firstName) && validateString(lastName) && isValidEmailAddress(email) &&
                validateString(department)) {

            try {


                // 2. Create Statement
                myStatement = myConnection.createStatement();

                System.out.println("Inserting a new employee to database\n");
                //Here we insert one or more row data, the int show how many rows are affected
                int rowsAffected = myStatement.executeUpdate("insert into employees " +
                        "(last_name, first_name, email, department, salary)" +
                        "values " +
                        "('" + lastName + "','" + firstName + "','" + email + "'," +
                        "'" + department + "'," + salary + ")");
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

    }


    public void updateEmail(String firstName, String lastName, String email) throws SQLException {

        if (validateString(firstName) && validateString(lastName) && isValidEmailAddress(email)) {


            try {

                myStatement = myConnection.createStatement();

                System.out.println("Modify an employee email address in the database\n");
                int rowsAffectedUpdate = myStatement.executeUpdate(
                        "update employees " +
                                "set email='" + email + "' " +
                                "where last_name='" + lastName + "' " + "and first_name='" + firstName + "'");

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public void deleteUser(String lastName, String firstName) throws SQLException{

        if (validateString(lastName) && validateString(firstName)){

            try {

                myStatement = myConnection.createStatement();

                System.out.println("Deleted employee: " + firstName + " " + lastName + "from the database\n");
                int rowsAffectedDelete = myStatement.executeUpdate(
                        "delete from employees " +
                                "where last_name='" + lastName + "' " + "and first_name='" + firstName + "'");

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public void getDatabase() throws SQLException {

        try {

            myStatement = myConnection.createStatement();

            // 4. Verify this by getting a list of employees
            myResultSet = myStatement.executeQuery("select * from employees order by last_name");

            // 5. Process the result set
            // read the data from each row, also give back a boolean
            while (myResultSet.next()) {
                System.out.println(myResultSet.getString("last_name") + ", " + myResultSet.getString("first_name"));
            }

        } catch (
                Exception exc) {
            exc.printStackTrace();
        }
    }

    private boolean validateString(String s) {

        return (s != null) && (!(s.isEmpty()));
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void closeResource() throws SQLException{

        if (myStatement != null) {
            myStatement.close();
        }

    }

}

