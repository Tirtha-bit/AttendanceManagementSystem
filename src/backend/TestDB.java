package backend;

public class TestDB {
    public static void main(String[] args) {
        String url = "postgresql://postgres:yrCLKPWu24@localhost:5432/attendance_db";

        Database db = new Database(url);

        if (db.getConnection() != null) {
            System.out.println("Database connection test SUCCESS!");
        } else {
            System.out.println("Database connection test FAILED!");
        }
    }
}
