package com.bodejidi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.lang.AutoCloseable;
import java.util.Date;


public class DataBaseService
{ 
    static final String jdbcUrl = "jdbc:mysql://localhost/test?" + "user=root" + "&password=";
    static final String jdbcDriver = "com.mysql.jdbc.Driver";
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    private Integer pIndex = null;
    
    private DataBaseService()
    {
        
    }
    
    static public DataBaseService newInstance() 
    {
        DataBaseService ds = new DataBaseService();
        try
        {
            ds.conn = ds.createConnection();
            ds.stmt = ds.conn.createStatement();           
        }
        catch(SQLException e)
        {
        
        }
        return ds;   
    }
    
    public DataBaseService prepare(String sql) throws SQLException
    {
        pstmt = conn.prepareStatement(sql);
        pIndex = 1;       
        return this;
    }
    public DataBaseService setString(String para) throws SQLException
    {
        pstmt.setString(pIndex, para);
        pIndex ++;
        return this;
    }
    public DataBaseService setDate(Date date) throws SQLException
    {
        pstmt.setDate(pIndex, new java.sql.Date(date.getTime()));
        pIndex ++;
        return this;
    }
    public void execute() throws SQLException
    {
        pstmt.execute();
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
        
        close(pstmt);
        pstmt = null;

    }
    
    
    
}
