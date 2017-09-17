package sqlmaster;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.sql.*;
import java.util.Scanner;


public class JdbcDBM {


    private Connection myConnection = null;
    private Statement myStatement = null;
    private ResultSet myResultSet = null;
    private PreparedStatement myPreparedStatement = null;
    private InputStream inputStream = null;
    private FileOutputStream outputStream = null;
    private CallableStatement myCallableStatement = null;
    private FileReader inputRead = null;
    private FileWriter outputWrite = null;
    private Reader inputReader = null;
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

    // THIS METHOD USE OUT PARAMETER
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

            myCallableStatement.setString(1, department);
            myCallableStatement.execute();

            // Get the result set
            myResultSet = myCallableStatement.getResultSet();

            // display
            display(myResultSet);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }


    // SQL DATABASE TRANSACTIONS

    public void deleteDepartmentAndUpdateSalary(String departmentToDelete, String departmentToSetSalary, double setAmount) throws SQLException {

        if (!(validateString(departmentToDelete) || !(validateString(departmentToSetSalary)))) {

            throw new IllegalArgumentException("Invalid department: " + departmentToDelete +
                    ", " + departmentToSetSalary);
        }

        try {
            // 1. Turn off auto commit
            myConnection.setAutoCommit(false);

            // 2. First part of transaction, Delete all "departmentToDelete" employees
            myStatement = myConnection.createStatement();
            myStatement.executeUpdate("delete from employees where department = '" + departmentToDelete + "'");

            // 3. Second part of transaction, Set(not ADD) salary to "amountToRaise" for all "departmentToSetSalary"
            myStatement.executeUpdate("update employees set salary =" + setAmount +
                    " where department= '" + departmentToSetSalary + "'");

            System.out.println("\n>> Transactions are ready. \n");

            boolean ok = askUserIfOkToSave();

            if (ok) {
                // Store in database
                myConnection.commit();
                System.out.println("\n>> Transaction COMMITTED. \n");
            } else {
                myConnection.rollback();
                System.out.println("\n>> Transaction ROLLED BACK. \n");
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    // METHODS TO GET AND USE METADATA FROM THE SQL SERVER

    public void getMetaData() throws SQLException {

        try {
            // 1. Get metadata
            DatabaseMetaData databaseMetaData = myConnection.getMetaData();

            // 2. Display info about database
            System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
            System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());
            System.out.println();

            // 3. Display info about JDBC Driver
            System.out.println("JDBC Driver name: " + databaseMetaData.getDriverName());
            System.out.println("JDBC Driver version: " + databaseMetaData.getDriverVersion());

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void schemaInfoTables(String tableName) throws SQLException {

        if (!(validateString(tableName))) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }


        // WE CAN GIVE ALL THESE STRINGS IN THE METHOD PARAMETER
        String catalog = null;
        String schemaPattern = null;
        String columnNamePattern = null;
        String tableNamePattern = null;
        String[] types = null;

        try {
            // 1. Get metadata
            DatabaseMetaData databaseMetaData = myConnection.getMetaData();

            // 2. Get the list of tables
            System.out.println("List of tables");
            System.out.println("--------------");

            myResultSet = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);

            while (myResultSet.next()) {
                System.out.println(myResultSet.getString("TABLE_NAME"));
            }

            // 3. Get list of columns
            System.out.println("\n\nList of Columns");
            System.out.println("-------------------");

            myResultSet = databaseMetaData.getColumns(catalog, schemaPattern, tableName, columnNamePattern);

            while (myResultSet.next()) {
                // Go trough the employee table and GET THE COLUMNS
                System.out.println(myResultSet.getString("COLUMN_NAME"));
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void getResultSetMetadata() throws SQLException {

        try {

            // 1. Run query
            myStatement = myConnection.createStatement();
            myResultSet = myStatement.executeQuery("select id, last_name, first_name, email, salary from employees");

            // 2. Get the ResultSet metadata
            ResultSetMetaData resultSetMetaData = myResultSet.getMetaData();

            // 3. Display info
            int columnCount = resultSetMetaData.getColumnCount();
            System.out.println("Column count: " + columnCount + "\n");

            for (int column = 1; column <= columnCount; column++) {
                System.out.println("Column name: " + resultSetMetaData.getColumnName(column));
                System.out.println("Column type name: " + resultSetMetaData.getColumnTypeName(column));
                // Is Nullable? its a primitive or an Object type ( =null )
                System.out.println("Is Nullable: " + resultSetMetaData.isNullable(column));
                // Auto Increment(The value is rise automatically, ID)
                System.out.println("Is Auto Increment: " + resultSetMetaData.isAutoIncrement(column) + "\n");
            }
        } catch (SQLException exc) {
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

    // USE, READ AND WRITE BLOB'S

    public void readEmployeeFileByEmail(String email, String fileName) throws SQLException, FileNotFoundException {

        if (isValidEmailAddress(email) && validateString(fileName)) {

            try {

                // 1. Prepare statement
                // in the blob column goes the inputStream THE PLACEHOLDER IS THE "?"
                String sql = "update employees set blob=? where email='" + email + "'";
                myPreparedStatement = myConnection.prepareStatement(sql);

                // 2. Set parameter for resume file name
                File theFile = new File(fileName);
                // THIS will goes to the place of the ? (blob column)
                inputStream = new FileInputStream(theFile);
                myPreparedStatement.setBinaryStream(1, inputStream);

                System.out.println("Reading input file: " + theFile.getAbsolutePath());

                // 3. Execute statement
                System.out.println("\nStoring blob in database: " + theFile);
                System.out.println(sql);

                myPreparedStatement.executeUpdate();

                System.out.println("\nCompleted successfully!");
            } catch (SQLException | FileNotFoundException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void writeEmployeeFileByEmail(String email, String fileName) throws SQLException, IOException {

        if (isValidEmailAddress(email) && validateString(fileName)){

            try {

                // 1. Execute statement
                myStatement = myConnection.createStatement();
                String sql = "select blob from employees where email= '" + email + "'";
                myResultSet = myStatement.executeQuery(sql);

                // 2. Set up a handle to the file
                File theFile = new File(fileName);
                outputStream = new FileOutputStream(theFile);

                if (myResultSet.next()){
                    inputStream = myResultSet.getBinaryStream("blob");
                    System.out.println("Reading blob from database...");
                    System.out.println(sql);

                    byte[] buffer = new byte[1024];
                    while (inputStream.read(buffer) > 0){
                        outputStream.write(buffer);
                    }

                    System.out.println("\nSaved to file: " + theFile.getAbsolutePath());
                    System.out.println("\nCompleted successfully!");
                }

            } catch (SQLException | IOException exc){
                exc.printStackTrace();
            }
        }
    }

    // READING AND WRITING CLOB'S USED TO STORE LARGE TEXT DOCUMENTS LIKE XML

    public void readFileToColumnByEmail(String email, String fileName) throws SQLException, IOException{

        if (isValidEmailAddress(email) && validateString(fileName)){

            try {
                // 1. Prepare statement
                String sql = "update employees set resume =? where email ='" + email + "'";
                myPreparedStatement = myConnection.prepareStatement(sql);

                // 2. Set parameter for resume file name
                File theFile = new File(fileName);
                inputRead = new FileReader(theFile);
                myPreparedStatement.setCharacterStream(1,inputRead);

                System.out.println("Reading input file: " + theFile.getAbsolutePath());

                // 3. Execute statement
                System.out.println("\nStoring resume in database: " + theFile);
                System.out.println(sql);

                myPreparedStatement.executeUpdate();

                System.out.println("\nCompleted successfully!");

            } catch (SQLException | IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void writeOutFileByEmail(String email, String fileName) throws SQLException, IOException {

        if (isValidEmailAddress(email) && validateString(fileName)){

            try {

                // 1. Execute statement
                myStatement = myConnection.createStatement();
                String sql = "select resume from employees where email='" + email + "'";
                myResultSet = myStatement.executeQuery(sql);

                // 3. Set up a handle to the file
                File theFile = new File(fileName);
                outputWrite = new FileWriter(theFile);

                if (myResultSet.next()){

                    inputReader = myResultSet.getCharacterStream("resume");
                    System.out.println("Reading resume from database...");
                    System.out.println(sql);
                }

                int theChar;
                while ((theChar = inputReader.read()) > 0){
                    outputWrite.write(theChar);
                }

                System.out.println("Saved to file: " + theFile.getAbsolutePath());
                System.out.println("Completed successfully!");

            } catch (SQLException | IOException exc){
                exc.printStackTrace();
            }
        }
    }

    // HELPER METHODS FROM THIS POINT

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

    private static boolean askUserIfOkToSave() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Is it okay to save?  yes/no: ");
        String input = scanner.nextLine();

        scanner.close();

        return input.equalsIgnoreCase("yes");
    }

    public void closeResource() throws SQLException, IOException {

        if (myStatement != null) {
            myStatement.close();
        }
        if (inputStream != null){
            inputStream.close();
        }
        if (outputStream != null){
            outputStream.close();
        }
        if (inputRead != null){
            inputRead.close();
        }
        if (inputReader != null){
            inputReader.close();
        }
        if (outputWrite != null){
            outputWrite.close();
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