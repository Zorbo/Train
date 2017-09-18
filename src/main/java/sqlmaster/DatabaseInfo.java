package sqlmaster;

import java.io.IOException;
import java.sql.*;

public class DatabaseInfo implements AutoCloseable {

    private JdbcDBM jdbcDBM;

    public DatabaseInfo(String loadFile) throws SQLException, IOException {
        jdbcDBM = new JdbcDBM(loadFile);
    }

    @Override
    public void close() throws Exception {
        jdbcDBM.closeResource();
    }

    public void getDatabase() throws SQLException {

        try {

            Statement myStatement = jdbcDBM.getMyConnection().createStatement();

            // 4. Verify this by getting a list of employees
            ResultSet myResultSet = myStatement.executeQuery("select * from employees order by last_name");

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

    // METHODS TO GET AND USE METADATA FROM THE SQL SERVER

    public void getMetaData() throws SQLException {

        try {
            // 1. Get metadata
            DatabaseMetaData databaseMetaData = jdbcDBM.getMyConnection().getMetaData();

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

    public void getResultSetMetadata() throws SQLException {

        try {

            // 1. Run query
            Statement myStatement = jdbcDBM.getMyConnection().createStatement();
            ResultSet myResultSet = myStatement.executeQuery("select id, last_name, first_name, email, salary from employees");

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
            DatabaseMetaData databaseMetaData = jdbcDBM.getMyConnection().getMetaData();

            // 2. Get the list of tables
            System.out.println("List of tables");
            System.out.println("--------------");

            ResultSet myResultSet = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);

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

    public void getEmployeesFromTheDepartment(String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {
            CallableStatement myCallableStatement = jdbcDBM.getMyConnection()
                    .prepareCall("{call get_employees_for_department (?)}");

            myCallableStatement.setString(1, department);
            myCallableStatement.execute();

            // Get the result set
            ResultSet myResultSet = myCallableStatement.getResultSet();

            // display
            display(myResultSet);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    // PREPARED STATEMENTS

    public void selectEmployeesBySalaryAndDepartment(double aboveSalary, String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department");
        }

        try {
            // 1. Prepare statement
            PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection()
                    .prepareStatement("select * from employees where salary > ? and department=?");

            // 2. Set parameters
            myPreparedStatement.setDouble(1, aboveSalary);
            myPreparedStatement.setString(2, department);

            // 3. Execute SQL query
            ResultSet myResultSet = myPreparedStatement.executeQuery();

            // Call the testing method
            display(myResultSet);

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    // STORED PROCEDURES IN SQL, NEED A STORED PROCEDURE IN THE SQL DATABASE!!
    // THIS METHOD USE OUT PARAMETER

    public void countEmployeeByDepartment(String department) throws SQLException {

        if (!(validateString(department))) {
            throw new IllegalArgumentException("Invalid department: " + department);
        }

        try {

            CallableStatement myCallableStatement = jdbcDBM.getMyConnection()
                    .prepareCall("{call get_count_for_department(?,?)}");

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

    private boolean validateString(String s) {

        return jdbcDBM.validateString(s);
    }

    private void display(ResultSet myRs) throws SQLException {

        jdbcDBM.display(myRs);
    }
}
