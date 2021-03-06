import java.sql.*;

public class JDBCExample {

    // Use ?allowMultiQueries=true for showing an actual SQL Injection
    static final String DB = "jdbc:mysql://localhost/jdbc?allowMultiQueries=true";
    static final String USER = "root";
    static final String PASSWORD = "**************";
    static Connection sqlConnection;

    public static void addPerson(String firstname, String lastname, int age) {
        PreparedStatement insertStatement;
        try {
            insertStatement = sqlConnection.prepareStatement("INSERT INTO persons ( firstname, lastname, age ) VALUES ( ?, ?, ?)");
            insertStatement.setString(1, firstname );
            insertStatement.setString(2, lastname );
            insertStatement.setInt(3, age );
            insertStatement.execute();
            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeLastName(String oldName, String newName) {
        PreparedStatement updateStatement;
        try {
            updateStatement = sqlConnection.prepareStatement("UPDATE persons set lastname=? WHERE lastname=?");
            updateStatement.setString(1, newName);
            updateStatement.setString(2, oldName);
            updateStatement.execute();
            updateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showAllPersons() {
        Statement selectStatement;
        try {
            selectStatement = sqlConnection.createStatement();
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
            selectStatement.close();
            sqlResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deletePerson(String lastName) {
        PreparedStatement deleteStatement;
        try {
            deleteStatement = sqlConnection.prepareStatement("DELETE FROM persons WHERE lastname=?");
            deleteStatement.setString(1, lastName);
            deleteStatement.execute();
            deleteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
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
            addPerson("Bad", "Boys", 2);

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("Before SQL-Injection:");
            showAllPersons();

            // You attempted to write some bad SQL injection statements
            String newLastName = "Person";
            String badName = "Boys'; TRUNCATE persons;-- '";
            Statement badStatement = sqlConnection.createStatement();
            System.out.println("Bad Query: "+"UPDATE persons " +"SET lastname = '" + newLastName + "' " +"WHERE lastname ='" + badName+"'");
            badStatement.execute("UPDATE persons " +"SET lastname = '" + newLastName + "' " +"WHERE lastname ='" + badName+"'");
            badStatement.close();

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After SQL-Injection:");
            showAllPersons();

            sqlConnection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
