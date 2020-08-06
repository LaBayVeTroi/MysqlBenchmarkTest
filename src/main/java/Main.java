import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main extends Thread {
    public static String[] updateQuery = new String[]{"INSERT INTO customers (first_name,last_name,email) VALUES ('", "','", "','", "')"};

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            Main main = new Main("debezium_create" + i);
            main.start();

        }
    }

    public static Connection createConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/demo?useSSL=false", "admin", "pass");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return con;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void run() {
        Connection connection = createConnection();
        if (connection == null) {
            System.out.println("Failed to create connection !");
            return;
        }
        String[] updateParams = new String[3];
        try (Statement statement = connection.createStatement();) {
            for (int i = 0; i < 1000; i++) {
                updateParams[0] = "FirstName" + currentThread().getName() + "," + i;
                updateParams[1] = "LastName" + currentThread().getName() + "," + i;
                updateParams[2] = "Email@" + currentThread().getName() + "," + i;
                String query = updateQuery[0] + updateParams[0] + updateQuery[1] + updateParams[1] + updateQuery[2] + updateParams[2] + updateQuery[3];
                System.out.println(query);
                statement.execute(query);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        closeConnection(connection);
    }

    public Main(String s) {
        super(s);
    }

    public Main() {
        super();
    }
}

