package eu.ibagroup.easyrpa.openframework.db.service;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import eu.ibagroup.easyrpa.openframework.core.sevices.RPAServicesAccessor;
import javax.inject.Inject;

public class MySqlService{
    private String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    String driver = "";
    String dbURL = "";
    String userName = "";
    String password = "";
    private Connection connection;

    @Inject
    public MySqlService(RPAServicesAccessor rpaServices) {
        this.driver = rpaServices.getConfigParam("db.driver");
        this.dbURL = rpaServices.getConfigParam("db.url");
        this.userName = rpaServices.getCredentials("db.credentials").getUser();
        this.password = rpaServices.getCredentials("db.credentials").getPassword();
    }

    public MySqlService() {
    }

    public MySqlService(String driver, String dbURL, String userName, String password) {
        this.driver = driver;
        this.dbURL = dbURL;
        this.userName = userName;
        this.password = password;
    }

    Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection == null || connection.isClosed()){
            Class.forName(getDriver());
            String url = getDbUrl();
            Properties props = new Properties();
            props.setProperty("user", getUserName());
            props.setProperty("password", getPassword());
            //props.setProperty("ssl","true");
            this.connection = DriverManager.getConnection(url, props);
        }
        return this.connection;
    }

    public ResultSet executePreparedStatement(String query) throws SQLException, ClassNotFoundException {
        PreparedStatement st = getConnection().prepareStatement(query);
        return st.executeQuery();
    }
    public int executeUpdateStatement(String query) throws SQLException, ClassNotFoundException {
        Statement st = getConnection().createStatement();
        return st.executeUpdate(query);
    }
    public boolean executeStatement(String query) throws SQLException, ClassNotFoundException {
        Statement st = getConnection().createStatement();
        return st.execute(query);
    }
    public void closeConnection() throws SQLException {
        if(connection != null || !connection.isClosed()){
            connection.close();
        }
    }

    public int[] executeBatch(List<String> queries) throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        boolean autoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        Statement st = getConnection().createStatement();
        for(String query : queries) {
            st.addBatch(query);
        }
        int[] count = st.executeBatch();
        con.setAutoCommit(autoCommit);
        return count;
    }

    private String getUserName() {
        return this.userName;
    }

    private String getPassword() {
        return this.password;
    }

    private String getDbUrl() {
        return this.dbURL;
    }

    private String getDriver() {
        return StringUtils.isBlank(this.driver) ? DEFAULT_DRIVER : this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MySqlService driver(String driver) {
        setDriver(driver);
        return this;
    }

    public MySqlService dbURL(String dbURL) {
        setDbURL(dbURL);
        return this;
    }

    public MySqlService userName(String userName) {
        setUserName(userName);
        return this;
    }

    public MySqlService password(String password) {
        setPassword(password);
        return this;
    }
}
