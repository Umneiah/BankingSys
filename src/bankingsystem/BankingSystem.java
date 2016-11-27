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
    public static void Register()
    {
        
    }
    public static void Login(String ID , String pass)
    {
         
    }
    public static void Deposit()
    {
        
    }
    public static void Withdraw()
    {
        
    }
    public static void GetTransHistory()
    {
        
    }
    public static void transfer(String id1 , String id2 , String amount,String bank_id)
    {
             
    }
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
                String id = dis.readUTF();
                String p = dis.readUTF();
                String BalanceAndName = Login(s,p);
                String name = "";
                String pass = "";
                 if(!BalanceAndName.equals("no"))
                 {
                    String n[] = BalanceAndName.split(" ");
                   name = n[0];
                   pass = n[1]; 
                   dos.WriteUTF(name);
                   dos.WriteUTF(pass);
                 }else{
                      dos.WriteUTF("no");
                 }
                 while(true)
                 {
                     String amount = dis.readUTF();
                     String withdraw_or_deposite_or_tran = dis.readUTF();
                     String h ="";
                     
                    if(withdraw_or_deposite-or_tran.equals("w"))
                     {
                        h = withdraw(amount,id);
                     }else if(withdraw_or_deposite-or_tran.equals("d"))
                     {
                         h = Deposit(amount,id);
                     }else if(withdraw_or_deposite-or_tran.equals("t"))
                     {
                          h = GetTransHistory(id);
                     }else if(withdraw_or_deposite-or_tran.equals("f"))
                     {
                         String id2 = dis.readUTF();
                         String bank_id = dis.readUtf();
                         h =  transfet(s,id2,amount,bank_id);    
                     }
                     dos.writeUTF(h);
                          
                 }
               
                
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
