/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankingsystem;

/**
 *
 * @author mostafa
 */
import java.sql.*;
import java.io.*;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
public ClientHandler extends Thread{
         Socket c;
    Connection myconn = null;
    Statement mystat = null;
    ResultSet myres = null;
    public   ClientHandler (Socket c) {
        this.c = c;
        try{
            myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankingSystem","root","");
            mystat = myconn.createStatement();
        }catch(Exception f)
        {
            
        }
         public void run() {

        try {
            DataInputStream dis
                    = new DataInputStream(c.getInputStream());
            DataOutputStream dos
                    = new DataOutputStream(c.getOutputStream());
                String s = dis.readUTF();
                String p = dis.readUTF();
                String amount = checkVaild(s,p);
                
                dos.writeUTF(amount);
                
                String a = dis.readUTF();
                String withdraw_or_deposite = dis.readUTF();
                String h ="";
                if(withdraw_or_deposite.equals("w"))
                {
                   h = withdraw(a,s);
                }
                dos.writeUTF(h);
               c.close();

        } 
        catch (Exception e) 
        {
            System.out.println("Something went wrong");
        }
}
    }
}
public class BankingSystem{
   
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
