package com.bodejidi;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import static com.bodejidi.Constants.FORM_FIRST_NAME; 
import static com.bodejidi.Constants.FORM_LAST_NAME;
import static com.bodejidi.Constants.FORM_ID;
import static com.bodejidi.Constants.FORM_ACTION;
import static com.bodejidi.Constants.SHIHANG_FIRST_NAME;
import static com.bodejidi.Constants.SHIHANG_LAST_NAME;
import static com.bodejidi.Constants.SHIHANG_ID;
import static com.bodejidi.Constants.SHIHANG_TABLE ;
import static com.bodejidi.Constants.SHIHANG_DATE_CREATED;
import static com.bodejidi.Constants.SHIHANG_LAST_UPDATED;

public class MemberService
{
    public Member save(Member member) throws Exception
    {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        if(firstName == null || firstName.length() == 0 || lastName == null || lastName.length() == 0)
        {
            throw new Exception("Member validator error!!!");
        }
        MemberDao memberDao = new MemberDao();
        member = memberDao.save(member);
        return member;
    }
    
    public List<Member> findAllMember() throws SQLException
    {
        List<Member> memberList = new ArrayList<Member>();
        DataBaseService ds = null;
        
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "SELECT * FROM " + SHIHANG_TABLE;
            debug(sql);
            ResultSet rs = ds.executeQuery(sql);
            while(rs.next())
            {
                Member member = new Member();
                member.setId(rs.getLong(SHIHANG_ID));
                member.setFirstName(rs.getString(SHIHANG_FIRST_NAME));
                member.setLastName(rs.getString(SHIHANG_LAST_NAME));
                memberList.add(member);
                
            }
        }
        finally
        {
            ds.close();
        }
        return memberList;
    }
    
    public Member getMemberById(Long pid) throws SQLException
    {
        Member member = new Member();
        
        String sql = "SELECT * FROM " + SHIHANG_TABLE;
        sql = sql + " " + " WHERE " + SHIHANG_ID + " = " + pid;
        debug(sql);
        DataBaseService ds = null;
        try
        {
            ds = DataBaseService.newInstance();
            ResultSet rs = ds.executeQuery(sql);   
            rs.next();
            member.setId(rs.getLong(SHIHANG_ID));
            member.setFirstName(rs.getString(SHIHANG_FIRST_NAME));
            member.setLastName(rs.getString(SHIHANG_LAST_NAME));             
        }
        finally
        {
            ds.close();
        }
        return member;       
    } 
    
     public Member update(Member member) throws SQLException
    {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();
        Long paraId = member.getId();
        DataBaseService ds = null;
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "update " + SHIHANG_TABLE + " set " + SHIHANG_FIRST_NAME + "=? , " + SHIHANG_LAST_NAME + "=? where " + SHIHANG_ID + "=?";
            debug("SQL: " + sql);
            ds.execute(sql,firstName,lastName,paraId);  
        }
        finally
        {
            ds.close();
        }
        return member;
    }
    
     public void deleteById(Long paraId) throws SQLException
    {
        DataBaseService ds = null;
        try
        {
            ds = DataBaseService.newInstance();
            String sql = "delete from " + SHIHANG_TABLE + " where " + SHIHANG_ID  + "=" + paraId;
            debug("SQL: " + sql);
            ds.execute(sql);
        }
        finally
        {
            ds.close();
        }
    }
    
    public void debug(String str)
    {
        String className = Manager.class.getName();
        System.out.println("[DEBUG] " + new Date() + " " + className + " " + str);
    }
}