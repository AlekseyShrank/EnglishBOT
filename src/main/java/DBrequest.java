import java.sql.*;

public abstract class DBrequest {

    public static Connection Getconn(){//подключение к БД через JDBC
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            MainApp.logger.info("DB Driver not found" + e.getMessage());
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/EnglishDB","user", "pssword");
            return conn;
        } catch (SQLException e) {
            MainApp.logger.info("DB Connection loss" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static synchronized String getWords(int lvl){
        Connection conn = Getconn();
        Statement statement = null;
        String words = "!";

        String SQLrequest = String.format("select * \n" +
                "from words\n" +
                "where lvl = %d", lvl);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            ResultSet rs = statement.executeQuery(SQLrequest);
            rs.next();

            words = String.format("%s - %s",rs.getString("text_en"),rs.getString("text_ru"));


            statement.close();
            conn.close();

        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
        return words;
    }

    public static synchronized int getUserlvl(long chatid){
        Connection conn = Getconn();
        Statement statement = null;
        int lvl = 0;

        String SQLrequest = String.format("select * \n" +
                "from users\n" +
                "where chat_id = %d", chatid);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            ResultSet rs = statement.executeQuery(SQLrequest);
            rs.next();

            lvl = rs.getInt("lvl");


            statement.close();
            conn.close();

        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
        return lvl;

    }

    public static synchronized int getUserSettings(long chatid){
        Connection conn = Getconn();
        Statement statement = null;
        int settings = 1;

        String SQLrequest = String.format("select * \n" +
                "from users\n" +
                "where chat_id = %d", chatid);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            ResultSet rs = statement.executeQuery(SQLrequest);
            rs.next();

            settings = rs.getInt("settings");


            statement.close();
            conn.close();

        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
        return settings;
    }

    public static synchronized int getUserTimezone(long chatid){
        Connection conn = Getconn();
        Statement statement = null;
        int timezone = 0;

        String SQLrequest = String.format("select * \n" +
                "from users\n" +
                "where chat_id = %d", chatid);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            ResultSet rs = statement.executeQuery(SQLrequest);
            rs.next();

            timezone = rs.getInt("timezone");


            statement.close();
            conn.close();

        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
        return timezone;
    }

    public static synchronized boolean isNewUser(long chatid){
        Connection conn = Getconn();
        Statement statement = null;

        String SQLrequest = String.format("select count(*) from users\n" +
                "where chat_id = %d", chatid);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            ResultSet rs = statement.executeQuery(SQLrequest);
            rs.next();
            if(rs.getInt("count") == 0){
                statement.close();
                conn.close();
                return true;
            }else{
                statement.close();
                conn.close();
            }
        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
        return false;
    }

    public static synchronized void setNewUser(Users user){
        Connection conn = Getconn();
        Statement statement = null;

        String SQLrequest = String.format("insert into users\n" +
                "values (%d, '%s', %d, %d, %d, '%s')",user.chatid, user.name, user.timezone, user.lvl, user.settings, new Timestamp(System.currentTimeMillis()));

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            statement.executeQuery(SQLrequest);

            statement.close();
            conn.close();
            statement.close();
            conn.close();
            MainApp.logger.info("Add new user: "+user.name);
        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }
    }

    public static synchronized void setLvl(long chatid, int lvl){
        Connection conn = Getconn();
        Statement statement = null;

        String SQLrequest = String.format("update users \n" +
                "set lvl = %d\n" +
                "where chat_id = %d",lvl ,chatid);

        try {
            statement = conn.createStatement();

            // выполнить SQL запрос
            statement.executeQuery(SQLrequest);

            statement.close();
            conn.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            MainApp.logger.info(e.getMessage());
        }

    }

}




