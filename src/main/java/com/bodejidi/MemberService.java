package com.bodejidi;

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
        DataBaseService ds = DataBaseService.newInstance();
        String sql = "INSERT INTO " + SHIHANG_TABLE + " ( " + SHIHANG_FIRST_NAME + ", " + SHIHANG_LAST_NAME + " ," 
                    + SHIHANG_DATE_CREATED + "," + SHIHANG_LAST_UPDATED + ") VALUES('"
                    + firstName + "','" + lastName +"',now(),now())";
        ds.execute(sql);
        return member;
    }
}