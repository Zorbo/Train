package sqlmaster;

import java.io.IOException;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {


        JdbcDBM jdbcDBM = null;

        // Everything can throw here SQLException we need to handle that!

        try {

            jdbcDBM = new JdbcDBM();
            //  jdbcDBM.updateEmail("John", "Doe", "legkalapacs@gmail.com");
            //  jdbcDBM.insertNewRow("Arthur","Almodovar","arthur@gobabyplease.com","specOps",3000);
            //  jdbcDBM.insertNewRow("Boby", "Supersonic", "bo@gil.com", "HR", 3000.12);
            //  jdbcDBM.deleteUser("Supersonic","Boby");
            //  jdbcDBM.selectEmployeesBySalaryAndDepartment(80000,"Legal");
            //  System.out.println("******************************");
            //  jdbcDBM.selectEmployeesBySalaryAndDepartment(25000,"HR");
            //  jdbcDBM.increaseSalaryByDepartment("Engineering",10000);
            //  jdbcDBM.greetDepartment("Engineering");
            //  jdbcDBM.countEmployeeByDepartment("Engineering");
            //  jdbcDBM.getEmployeesFromTheDepartment("Engineering");
            //  jdbcDBM.deleteDepartmentAndUpdateSalary("HR","Engineering",300000);
            jdbcDBM.schemaInfoTables("employees");
            System.out.println("*****************************");
            jdbcDBM.getMetaData();
            System.out.println("******************************");
            jdbcDBM.getResultSetMetadata();
            System.out.println("******************************");
            //  jdbcDBM.readEmployeeFileByEmail("john.doe@foo.com", "sample_resume.pdf");
            //  jdbcDBM.writeEmployeeFileByEmail("john.doe@foo.com","resume_from_db.pdf");
            //  jdbcDBM.readFileToColumnByEmail("john.doe@foo.com","sample_resume.txt");
            jdbcDBM.writeOutFileByEmail("john.doe@foo.com","resume_from_db.txt");
            System.out.println("****************************");
            jdbcDBM.getDatabase();


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (jdbcDBM != null) {
                    // We use the close resource method here
                    jdbcDBM.closeResource();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }


    }
}
