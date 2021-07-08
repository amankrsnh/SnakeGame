
package com.amcoder;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection 
{
    private static Connection con=null;
    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/SnakeGame","root","redhat");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static Connection getConnection()
    {
        return con;
    }
}
