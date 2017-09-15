package sqlmaster;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.*;


public class JdbcDBM {


    private Connection myConnection = null;
    private Statement myStatement = null;
    private ResultSet myResultSet = null;
    private PreparedStatement myPreparedStatement = null;
    private CallableStatement myCallableStatement = null;
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

                // Prepare Statement
                myPreparedStatement = myConnection.prepareStatement("insert into employees " +
                        "(last_name, first_name, email, department, salary)" +
                        "values " +
                        "(?,?,?,?,?)");
                System.out.println("Inserting a new employee to database\n");
                // THE ? IS LEFT TO RIGHT: 1,2,3,4,5
                myPreparedStatement.setString(1, lastName);
                myPreparedStatement.setString(2, firstName);
                myPreparedStatement.setString(3, email);
                myPreparedStatement.setString(4, department);
                myPreparedStatement.setDouble(5, salary);

                // Execute SQL query
                myPreparedStatement.executeUpdate();

            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }

    }

    public void selectEmployeesBySalaryAndDepartment(double aboveSalary, String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department");
        }

        try {
            // 1. Prepare statement
            myPreparedStatement = myConnection.prepareStatement("select * from employees where salary > ? and department=?");

            // 2. Set parameters
            myPreparedStatement.setDouble(1, aboveSalary);
            myPreparedStatement.setString(2, department);

            // 3. Execute SQL query
            myResultSet = myPreparedStatement.executeQuery();

            // Call the testing method
            display(myResultSet);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }


    public void updateEmail(String firstName, String lastName, String email) throws SQLException {

        if (validateString(firstName) && validateString(lastName) && isValidEmailAddress(email)) {


            try {

                myPreparedStatement = myConnection.prepareStatement("update employees " +
                        "set email= ? " +
                        "where last_name= ? " + "and first_name=?");

                myPreparedStatement.setString(1, email);
                myPreparedStatement.setString(2, lastName);
                myPreparedStatement.setString(3, firstName);

                System.out.println("Modify an employee email address in the database\n");
                // Execute SQL query
                myPreparedStatement.executeUpdate();

            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void deleteUser(String lastName, String firstName) throws SQLException {

        if (validateString(lastName) && validateString(firstName)) {

            try {


                System.out.println("Deleted employee: " + firstName + " " + lastName + " from the database\n");
                myPreparedStatement = myConnection.prepareStatement("delete from employees " +
                        "where last_name=? " + "and first_name=?");

                myPreparedStatement.setString(1, lastName);
                myPreparedStatement.setString(2, firstName);

                myPreparedStatement.executeUpdate();

            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    // STORED PROCEDURES IN SQL, NEED A STORED PROCEDURE IN THE SQL DATABASE!!

    // THIS METHOD USE IN PARAMETERS
    public void increaseSalaryByDepartment(String department, double amountToRaise) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {
            myCallableStatement = myConnection.prepareCall("{call increase_salaries_for_department(?, ?)}");

            myCallableStatement.setString(1, department);
            myCallableStatement.setDouble(2, amountToRaise);

            // Execute the statement
            System.out.println("\n\nCalling stored procedure.  increase_salaries_for_department('" +
                    department + "', " + amountToRaise + ")");
            myCallableStatement.execute();
            System.out.println("Finished calling procedure");
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    // THIS METHOD USE INOUT PARAMETERS
    public void greetDepartment(String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {
            myCallableStatement = myConnection.prepareCall("{call greet_the_department(?)}");

            myCallableStatement.registerOutParameter(1, Types.VARCHAR); // USE THIS FOR INOUT
            myCallableStatement.setString(1, department);

            // Call stored procedure
            myCallableStatement.execute();

            // Get the value of the INOUT parameter
            String theResult = myCallableStatement.getString(1);

            System.out.println("\nThe result = " + theResult);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void countEmployeeByDepartment(String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {

            myCallableStatement = myConnection.prepareCall("{call get_count_for_department(?,?)}");

            myCallableStatement.setString(1, department);
            myCallableStatement.registerOutParameter(2, Types.INTEGER); // USE THIS FOR OUT

            myCallableStatement.execute();

            // Get the value of the OUT parameter
            int theCount = myCallableStatement.getInt(2);

            System.out.println("\nThe count = " + theCount);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void getEmployeesFromTheDepartment(String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {
            myCallableStatement = myConnection.prepareCall("{call get_employees_for_department (?)}");

            myCallableStatement.setString(1,department);
            myCallableStatement.execute();

            // Get the result set
            myResultSet = myCallableStatement.getResultSet();

            // display
            display(myResultSet);

        } catch (SQLException exc){
            exc.printStackTrace();
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

    public void closeResource() throws SQLException {

        if (myStatement != null) {
            myStatement.close();
        }
    }

    // TESTING METHOD ONLY FOR TESTING
    private void display(ResultSet myRs) throws SQLException {
        while (myRs.next()) {
            String lastName = myRs.getString("last_name");
            String firstName = myRs.getString("first_name");
            double salary = myRs.getDouble("salary");
            String department = myRs.getString("department");

            System.out.printf("%s, %s, %.2f, %s\n", lastName, firstName, salary, department);
        }
    }

}

