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
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    
    private DataBaseService()
    {
        
    }
    static public DataBaseService newInstance() throws SQLException
    {
        DataBaseService ds = new DataBaseService();
        ds.conn = ds.createConnection();
        ds.stmt = ds.conn.createStatement();
        return ds;

    }
    public ResultSet executeQuery(String sql) throws SQLException
    {
        
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
    public void execute(String sql) throws SQLException
    {
        stmt.execute(sql);
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
