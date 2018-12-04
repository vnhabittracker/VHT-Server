/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Administrator
 */
public class DAO implements Serializable {
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;
    
    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkLogin(String username, String password) {
//        int role = 0;
        try {
//            String sql = "select Role from Employee where Username = ? AND Password = ?";
//            con = MyConnection.getMyConnection();
//            stm = con.prepareStatement(sql);
//            stm.setString(1, username);
//            stm.setString(2, password);
//            rs = stm.executeQuery();
//            if (rs.next()) {
//                role = rs.getInt("Role");
//            }
            if (username.equals("admin") && password.equals("admin")) {
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }
}
