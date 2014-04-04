package com.bodejidi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.lang.AutoCloseable;

public class DataBaseService
{ 
    static final String jdbcUrl = "jdbc:mysql://localhost/test?" + "user=root" + "&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public DataBaseService()
    {
        
    }   
    public ResultSet executeQuery(String sql) throws SQLException
    {
        conn = createConnection();
        stmt = conn.createStatement();
        return stmt.executeQuery(sql);
        
    }
    protected Connection createConnection() throws SQLException
    {
        try
        {        
            Class.forName(jdbcDriver).newInstance();    
        }
        catch(Exception ex)
        {
            //ignore
        }
        return DriverManager.getConnection(jdbcUrl);
    }
    protected void close(AutoCloseable obj) 
    {
        try
        {
            obj.close();
        }
        catch(Exception ex)
        {
            //ignore;
        }        
    }
    
    public void close()
    {
        close(rs);
        rs = null;
        
        close(stmt);
        stmt = null;
        
        close(conn);
        conn = null;
    }
    
    
    
}
