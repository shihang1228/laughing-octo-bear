package com.bodejidi;

import java.util.Date;
import java.sql.SQLException;

public class MemberDao
{
    public Member save(Member member) throws SQLException
    {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        DataBaseService ds = null;
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "INSERT INTO shihang (first_name, last_name ,date_created,last_updated) VALUES(?,?,?,?)";
            ds.prepare(sql).setString(firstName).setString(lastName).setDate(new Date()).setDate(new Date()).execute();
            debug(sql);
        }
        finally
        {
            ds.close();
        }
        return member;
    }
    public void debug(String str)
    {
        String className = Manager.class.getName();
        System.out.println("[DEBUG] " + new Date() + " " + className + " " + str);
    }
}