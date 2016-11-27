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
   public void Register(String username,String passsword_of_register,String balance,
            String bank_id_of_register)
    {
        try{
             mystat.executeUpdate("insert into client (name,passwor,balance,bank_id) values ('"+username+"','"+passsword_of_register+"',"+balance+","+bank_id_of_register+")");
        }catch(Exception r)
        {
        }   
    }
    public static void Login(String ID , String pass)
    {
         
    }
    public  String Deposit(String amount,String i)
    {
         String new_balance="";
        try{
          myres = mystat.executeQuery("select balance from client where id = " + i);
          myres.next();
          float current_balance ;
          current_balance = myres.getFloat("balance");
          float k = Float.valueOf(amount);
          current_balance = current_balance + k ; // update balance 
          new_balance = Float.toString(current_balance); // convert it to string to use it in query
          mystat.executeUpdate("update client set balance = "+new_balance +"where id = " +i);
          
        }catch (Exception e)
        {
            
        }
        return new_balance;
    }
    public static void Withdraw()
    {
        
    }
    public  Vector<String>  GetTransHistory(String i)
    {
         Vector <String> history =history = new Vector<String>();
         
        try{
            myres = mystat.executeQuery("select * from transication where sender = " + i);
            while(myres.next())
            {
                String out= myres.getString("sender")+ " " + myres.getString("reciver") 
                          + " " +myres.getString("amount") + " " + myres.getString("date");
                history.addElement(out);   
            }
        }catch(Exception e)
        {
            
        }
        return history;
        
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
                String l_or_r = dis.readUTF();
                if(l_or_r.equals("r")
                {
                    String username = dis.readUTF();
                    String password_of_register = dis.readUTF();
                    String balance = dis.readUTF();
                    String bank_id_of_register = dis.readUTF();
                    Register(username,passsword_of_register,balance,bank_id_of_register);
                }
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
