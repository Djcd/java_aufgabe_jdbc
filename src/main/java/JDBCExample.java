import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class JDBCExample {

    // Use ?allowMultiQueries=true for showing an actual SQL Injection
    static final String DB = "jdbc:mysql://localhost/jdbc?allowMultiQueries=true";
    static final String USER = "root";
    static final String PASSWORD = "**************";
    static Connection sqlConnection;

    public static void addPerson(String firstname, String lastname, int age) throws SQLException {
        PreparedStatement insertStatement = sqlConnection.prepareStatement("INSERT INTO persons ( firstname, lastname, age ) VALUES ( ?, ?, ?)");
        insertStatement.setString(1, firstname );
        insertStatement.setString(2, lastname );
        insertStatement.setInt(3, age );
        insertStatement.execute();
    }

    public static void changeLastName(String oldName, String newName) throws SQLException {
        PreparedStatement updateStatement = sqlConnection.prepareStatement("UPDATE persons set lastname=? WHERE lastname=?");
        updateStatement.setString(1, newName);
        updateStatement.setString(2, oldName);
        updateStatement.execute();
    }

    public static void showAllPersons() throws SQLException {
        Statement selectStatement = sqlConnection.createStatement();
        String selectAll = "SELECT * FROM persons";
        ResultSet sqlResult = selectStatement.executeQuery(selectAll);

        if(!sqlResult.next()){
            System.out.println("Table is empty!");
        }
        else {
            do {
                System.out.print("Firstname: " + sqlResult.getString("firstname"));
                System.out.print(", Lastname: " + sqlResult.getString("lastname"));
                System.out.println(", Age: " + sqlResult.getInt("age"));
            } while (sqlResult.next());
        }
    }

    public static void deletePerson(String lastName) throws SQLException {
        PreparedStatement deleteStatement = sqlConnection.prepareStatement("DELETE FROM persons WHERE lastname=?");
        deleteStatement.setString(1, lastName);
        deleteStatement.execute();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            // You wrote the code to connect to a database with JDBC
            sqlConnection = DriverManager.getConnection(DB, USER, PASSWORD);

            // You wrote the code to insert new records in the persons table
            addPerson("Paul", "Paulsen", 42);
            addPerson("Cody", "Codeson", 55);

            // You wrote the code to retrieve all records from the persons table
            System.out.println("After adding Persons:");
            showAllPersons();

            // You wrote the code to update the lastname of one record in the persons table
            changeLastName("Paulsen", "Choppy");

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After update Person Lastname:");
            showAllPersons();

            // You wrote the code to delete two record from the persons table
            deletePerson("Codeson");
            deletePerson("Choppy");

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After deletion:");
            showAllPersons();

            // Add Person for SQL Injection Example
            String lastName = "Boys";
            String firstName = "Bad";
            int age = 2;
            addPerson("Bad", "Boys", 2);

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("Before SQL-Injection:");
            showAllPersons();

            // You attempted to write some bad SQL injection statements
            String newLastName = "Person";
            String badName = "TEST'; TRUNCATE persons;-- '";
            Statement badStatement = sqlConnection.createStatement();
            badStatement.execute("UPDATE persons " +"SET lastname = '" + newLastName + "' " +"WHERE lastname ='" + badName+"'");

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After SQL-Injection:");
            showAllPersons();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
