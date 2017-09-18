package sqlmaster;

public class Main {

    public static void main(String[] args) {


        // Everything can throw here SQLException we need to handle that!

        try (DatabaseUpdate databaseUpdate = new DatabaseUpdate("demo.properties");
             DatabaseInfo databaseInfo = new DatabaseInfo("demo.properties");
             DatabaseReadWrite databaseReadWrite = new DatabaseReadWrite("demo.properties")) {

            // Load the server config file here

            System.out.println("*****************************");
            //  databaseInfo.getMetaData();
            //  databaseInfo.schemaInfoTables("employees");
            //  databaseInfo.getEmployeesFromTheDepartment("HR");
            //  databaseInfo.selectEmployeesBySalaryAndDepartment(60000,"Engineering");
            //  databaseInfo.countEmployeeByDepartment("HR");
            //  databaseInfo.getResultSetMetadata();
            //  databaseInfo.getDatabase();
            System.out.println("******************************");
            //  databaseUpdate.insertNewRow("Bobo","Harrington","harry.bob@gmail.com","HR",300000);
            //  databaseUpdate.updateEmail("John", "Doe", "legkalapacs@gmail.com");
            //  databaseUpdate.deleteUser("Harrington","Bobo");
            //  databaseUpdate.increaseSalaryByDepartment("Engineering",20000);
            //  databaseUpdate.greetDepartment("HR");
            //  databaseUpdate.deleteDepartmentAndUpdateSalary("Legal","HR",120000);
            System.out.println("******************************");
            //  databaseReadWrite.readEmployeeFileByEmail("john.doe@foo.com", "sample_blob.pdf");
            //  databaseReadWrite.writeEmployeeFileByEmail("john.doe@foo.com","bigstuff_from_db.pdf");
            //  databaseReadWrite.readFileToResumeByEmail("john.doe@foo.com","sample_resume.txt");
            //  databaseReadWrite.writeOutFileByEmail("john.doe@foo.com","resume_from_db.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
