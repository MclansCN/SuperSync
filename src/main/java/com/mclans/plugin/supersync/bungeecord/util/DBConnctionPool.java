package com.mclans.plugin.supersync.bungeecord.util;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;

public class DBConnctionPool {
    private HikariDataSource hikariDataSource;
    private String url;
    private String port;
    private String database;
    private String user;
    private String password;
    private int thread;

    public DBConnctionPool(String url, String port, String database, String user, String password, int thread) {
        this.url = url;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.thread = thread;
    }

    public boolean open() {
        try {
            hikariDataSource = new HikariDataSource();
            String link = MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", new Object[]{url, port, database});
            getHikariDataSource().setJdbcUrl(link);
            getHikariDataSource().setUsername(user);
            getHikariDataSource().setPassword(password);
            getHikariDataSource().setMaximumPoolSize(thread);
//            Connection connection = getHikariDataSoATE TABLE IF NOT EXISTS enderchest(id INT(11) UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,name CHAR(30) NOT NULL DEFAULT '',uuid CHAR(36) NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000',line1 LONGTEXT DEFAULT NULL ,line2 LONGTEXT DEFAULT NULL ,line3 LONGTEXT DEFAULT NULL ,line4 LONGTEXT DEFAULT NULL ,line5 LONGTEXT DEFAULT NULL ,line6 LONGTEXT DEFAULT NULL)ENGINE =InnoDB DEFAULT CHARSET = UTF8;");

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void execute(String sql) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public HashMap query(String sql) {
        HashMap list = new HashMap();
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsMeta=rs.getMetaData();
            int columnCount=rsMeta.getColumnCount();

            while(rs.next()) {
                HashMap map = new HashMap();
                for(int i = 1; i <= columnCount; i ++) {
                    map.put(rsMeta.getColumnLabel(i), rs.getObject(i));
//                    SuperSyncBC.log.info("key: " + rsMeta.getColumnLabel(i) + "; value: " + rs.getObject(i));
                }

                list.put(rs.getString("uuid"), map);

            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return list;
        }
    }

    public Connection getConnection() {
        if (getHikariDataSource() != null) {
            try {
                return getHikariDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }


}
