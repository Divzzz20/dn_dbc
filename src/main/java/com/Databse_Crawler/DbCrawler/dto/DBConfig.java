
package com.Databse_Crawler.DbCrawler.dto;


public class DBConfig {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

