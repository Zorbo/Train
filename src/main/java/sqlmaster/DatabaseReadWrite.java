package sqlmaster;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseReadWrite implements AutoCloseable {

    private JdbcDBM jdbcDBM;

    public DatabaseReadWrite(String loadFile) throws SQLException, IOException {
        jdbcDBM = new JdbcDBM(loadFile);
    }

    @Override
    public void close() throws Exception {
        jdbcDBM.closeResource();
    }

    // USE, READ AND WRITE BLOB'S

    public void readEmployeeFileByEmail(String email, String fileName) throws SQLException, FileNotFoundException {

        if (isValidEmailAddress(email) && validateString(fileName)) {

            try {

                // 1. Prepare statement
                // in the blob column goes the inputStream THE PLACEHOLDER IS THE "?"
                String sql = "update employees set bigstuff=? where email='" + email + "'";
                PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection().prepareStatement(sql);

                // 2. Set parameter for resume file name
                File theFile = new File(fileName);
                // THIS will goes to the place of the ? (blob column)
                InputStream inputStream = new FileInputStream(theFile);
                myPreparedStatement.setBinaryStream(1, inputStream);

                System.out.println("Reading bigstuff file: " + theFile.getAbsolutePath());

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

        if (isValidEmailAddress(email) && validateString(fileName)) {

            try {

                // 1. Execute statement
                Statement myStatement = jdbcDBM.getMyConnection().createStatement();
                String sql = "select bigstuff from employees where email= '" + email + "'";
                ResultSet myResultSet = myStatement.executeQuery(sql);

                // 2. Set up a handle to the file
                File theFile = new File(fileName);
                FileOutputStream outputStream = new FileOutputStream(theFile);

                if (myResultSet.next()) {
                    InputStream inputStream = myResultSet.getBinaryStream("bigstuff");
                    System.out.println("Reading bigstuff from database...");
                    System.out.println(sql);

                    byte[] buffer = new byte[1024];
                    while (inputStream.read(buffer) > 0) {
                        outputStream.write(buffer);
                    }

                    System.out.println("\nSaved to file: " + theFile.getAbsolutePath());
                    System.out.println("\nCompleted successfully!");
                }

            } catch (SQLException | IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    // READING AND WRITING CLOB'S USED TO STORE LARGE TEXT DOCUMENTS LIKE XML
    public void readFileToResumeByEmail(String email, String fileName) throws SQLException, IOException {

        if (isValidEmailAddress(email) && validateString(fileName)) {

            try {
                // 1. Prepare statement
                String sql = "update employees set resume =? where email ='" + email + "'";
                PreparedStatement myPreparedStatement = jdbcDBM.getMyConnection().prepareStatement(sql);

                // 2. Set parameter for resume file name
                File theFile = new File(fileName);
                FileReader inputRead = new FileReader(theFile);
                // the 1 is the place of the "?" at the String sql
                myPreparedStatement.setCharacterStream(1, inputRead);

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

        if (isValidEmailAddress(email) && validateString(fileName)) {

            // 1. Execute statement
            Statement myStatement = jdbcDBM.getMyConnection().createStatement();
            String sql = "select resume from employees where email='" + email + "'";
            ResultSet myResultSet = myStatement.executeQuery(sql);

            // 3. Set up a handle to the file
            File theFile = new File(fileName);
            try (FileWriter outputWrite = new FileWriter(theFile)) {
                Reader inputReader;

                if (myResultSet.next()) {

                    inputReader = myResultSet.getCharacterStream("resume");
                    System.out.println("Reading resume from database...");
                    System.out.println(sql);

                    int theChar;
                    while ((theChar = inputReader.read()) > 0) {
                        outputWrite.write(theChar);
                    }

                    System.out.println("Saved to file: " + theFile.getAbsolutePath());
                }

                System.out.println("Completed successfully!");

            } catch (SQLException | IOException exc) {
                exc.printStackTrace();
            }
        }
    }


    private boolean validateString(String s) {

        return jdbcDBM.validateString(s);
    }

    private boolean isValidEmailAddress(String email) {

        return jdbcDBM.isValidEmailAddress(email);
    }
}
