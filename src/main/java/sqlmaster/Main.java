package sqlmaster;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {


        JdbcDBM jdbcDBM = null;

        // Everything can throw here SQLException we need to handle that!

        try {

            jdbcDBM = new JdbcDBM();
            //  jdbcDBM.updateEmail("John", "Doe", "csillagharcos@gmail.com");
            //  jdbcDBM.insertNewRow("Arthur","Almodovar","arthur@gobabyplease.com","specOps",3000);
            //  jdbcDBM.insertNewRow("Boby", "Supersonic", "bobo@gmail.com", "daily hero", 3000.12);
            //  jdbcDBM.deleteUser("Supersonic","Boby");
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
