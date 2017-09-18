package sqlmaster;

import java.io.IOException;
import java.sql.*;

public class DatabaseUpdate implements AutoCloseable {

    private JdbcDBM jdbcDBM;

    public DatabaseUpdate(String loadFile) throws SQLException, IOException {
        jdbcDBM = new JdbcDBM(loadFile);
    }

    @Override
    public void close() throws Exception {
        jdbcDBM.closeResource();
    }

    public void insertNewRow(String firstName, String lastName, String email, String department, double salary) throws SQLException {

        if (validateString(firstName) && validateString(lastName) && isValidEmailAddress(email) &&
                validateString(department)) {

            try {

                // Prepare Statement
                PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection()
                        .prepareStatement("insert into employees " +
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

    public void updateEmail(String firstName, String lastName, String email) throws SQLException {

        if (validateString(firstName) && validateString(lastName) && isValidEmailAddress(email)) {

            try {
                PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection()
                        .prepareStatement("update employees " +
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
                PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection()
                        .prepareStatement("delete from employees " +
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
            CallableStatement myCallableStatement = jdbcDBM.getMyConnection()
                    .prepareCall("{call increase_salaries_for_department(?, ?)}");

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
            CallableStatement myCallableStatement = jdbcDBM.getMyConnection()
                    .prepareCall("{call greet_the_department(?)}");

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

    // SQL DATABASE TRANSACTIONS

    public void deleteDepartmentAndUpdateSalary(String departmentToDelete, String departmentToSetSalary, double setAmount) throws SQLException {

        if (!(validateString(departmentToDelete) || !(validateString(departmentToSetSalary)))) {

            throw new IllegalArgumentException("Invalid department: " + departmentToDelete +
                    ", " + departmentToSetSalary);
        }

        try {
            // 1. Turn off auto commit
            jdbcDBM.getMyConnection().setAutoCommit(false);

            // 2. First part of transaction, Delete all "departmentToDelete" employees
            Statement myStatement = jdbcDBM.getMyConnection().createStatement();
            myStatement.executeUpdate("delete from employees where department = '" + departmentToDelete + "'");

            // 3. Second part of transaction, Set(not ADD) salary to "amountToRaise" for all "departmentToSetSalary"
            myStatement.executeUpdate("update employees set salary =" + setAmount +
                    " where department= '" + departmentToSetSalary + "'");
            System.out.println("\n>> Transactions are ready. \n");

            boolean ok = askUserIfOkToSave();

            if (ok) {
                // Store in database
                jdbcDBM.getMyConnection().commit();
                System.out.println("\n>> Transaction COMMITTED. \n");
            } else {
                jdbcDBM.getMyConnection().rollback();
                System.out.println("\n>> Transaction ROLLED BACK. \n");
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }


    private boolean validateString(String s) {

        return jdbcDBM.validateString(s);
    }

    private boolean isValidEmailAddress(String email) {

        return jdbcDBM.isValidEmailAddress(email);
    }

    private boolean askUserIfOkToSave() {

        return jdbcDBM.askUserIfOkToSave();
    }
}
