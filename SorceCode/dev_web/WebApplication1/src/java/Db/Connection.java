/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.io.Serializable;
import java.sql.DriverManager;

/**
 *
 * @author Administrator
 */
public class Connection implements Serializable{
    public static java.sql.Connection getMyConnection(){
        java.sql.Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=FPTEvent","sa","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
