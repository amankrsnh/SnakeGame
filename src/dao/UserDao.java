package com.amcoder.dao;

import com.amcoder.DBConnection;
import com.amcoder.dto.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao 
{
    private static PreparedStatement ps,ps1,ps2,ps3;
    static
    {
        try
        {
            ps=DBConnection.getConnection().prepareStatement("Update user_details set userscore=? where username=? and password=?");
            ps1=DBConnection.getConnection().prepareStatement("Select * from user_details where username=?");
            ps2=DBConnection.getConnection().prepareStatement("Select userscore from user_details where username=? and password=?");
            ps3=DBConnection.getConnection().prepareStatement("Insert into user_details values(?,?,?)");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static boolean updateUserScore(User user) throws SQLException
    {
        ps.setInt(1, user.getUserscore());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        return ps.executeUpdate()==1;
    }
    public static boolean nameAlreadyPresent(String name) throws SQLException
    {
        ps1.setString(1, name);
        return ps1.executeQuery().next();
    }
    public static int getUserScore(User user) throws SQLException
    {
        ps2.setString(1, user.getUsername());
        ps2.setString(2, user.getPassword());
        ResultSet rs=ps2.executeQuery();
        rs.next();
        return rs.getInt(1);
    }
    public static boolean validate(User user) throws SQLException
    {
        ps2.setString(1, user.getUsername());
        ps2.setString(2, user.getPassword());
        ResultSet rs=ps2.executeQuery();
        return rs.next();
    }
    public static boolean addUser(User user) throws SQLException
    {
        ps3.setString(1, user.getUsername());
        ps3.setString(2, user.getPassword());
        ps3.setInt(3, user.getUserscore());
        return ps3.executeUpdate()==1;
    }
}
