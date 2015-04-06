package net.fusemc.zcore.mysql;

import net.fusemc.zcore.ZCore;
import snaq.db.ConnectionPool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private MySQLData mySQLData;
    private ConnectionPool connectionPool;

    public MySQL(MySQLData mySQLData) {
        this.mySQLData = mySQLData;

        if (!ZCore.OFFLINE) {
            openConnection(2, 5, 10);
        }
    }

    public MySQL(MySQLDBType mySQLDBType) {
        this(mySQLDBType.getMySQLData());
    }

    public MySQL(MySQLData mySQLData, int minConnections, int maxConnections, int totalConnections) {
        this.mySQLData = mySQLData;

        openConnection(minConnections, maxConnections, totalConnections);
    }

    public MySQL(MySQLDBType mySQLDBType, int minConnections, int maxConnections, int totalConnections) {
        this(mySQLDBType.getMySQLData(), minConnections, maxConnections, totalConnections);
    }

    private void openConnection(int minConnections, int maxConnections, int totalConnections) {
        try {
            Class<?> c = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) c.newInstance();
            DriverManager.registerDriver(driver);
            this.connectionPool = new ConnectionPool("local", minConnections, maxConnections, totalConnections, 60, "jdbc:mysql://" + mySQLData.getHostname() + ":" + mySQLData.getPort() + "/" + mySQLData.getDatabase(), mySQLData.getUser(), mySQLData.getPassword());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnection() {
        return this.connectionPool != null;
    }

    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection(2000);
    }
}