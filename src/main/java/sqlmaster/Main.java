package sqlmaster;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {


        JdbcDBM jdbcDBM = null;

        // Everything can throw here SQLException we need to handle that!

        try {

            jdbcDBM = new JdbcDBM();
            //  jdbcDBM.updateEmail("John", "Doe", "legkalapacs@gmail.com");
            //  jdbcDBM.insertNewRow("Arthur","Almodovar","arthur@gobabyplease.com","specOps",3000);
            //  jdbcDBM.insertNewRow("Boby", "Supersonic", "bo@gil.com", "daily hero", 3000.12);
            //  jdbcDBM.deleteUser("Supersonic","Boby");
            //  jdbcDBM.selectEmployeesBySalaryAndDepartment(80000,"Legal");
            //  System.out.println("******************************");
            //  jdbcDBM.selectEmployeesBySalaryAndDepartment(25000,"HR");
            //  jdbcDBM.increaseSalaryByDepartment("Engineering",10000);
            //  jdbcDBM.greetDepartment("Engineering");
            jdbcDBM.countEmployeeByDepartment("Engineering");
            jdbcDBM.getEmployeesFromTheDepartment("Engineering");
            jdbcDBM.getDatabase();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (jdbcDBM != null) {
                    // We use the close resource method here
                    jdbcDBM.closeResource();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
