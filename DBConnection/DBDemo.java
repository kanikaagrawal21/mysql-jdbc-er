package DBConnection;

import java.sql.*;

public class DBDemo {
    public static void main(String[] args) {


        String url = "jdbc:mysql://localhost:3306/addressbookdb";
        String user = "root";
        String password = "Route";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("update employee_payroll set basic_pay = ? where name = ?");
            preparedStatement.setInt(1,9000000);
            preparedStatement.setString(2,"dherya");
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows>0){
                System.out.println(affectedRows+"rows updated");
            }else {
                System.out.println("no rows updated");
            }

//            while(resultSet.next()){
//                int id = resultSet.getInt("id");
//                String name  = resultSet.getString("name");
//
//                System.out.println("name="+name);
//                System.out.println("id="+id);
//            }
        } catch(SQLException e){
            e.printStackTrace();

        }
    }
}