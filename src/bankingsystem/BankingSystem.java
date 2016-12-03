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
import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
class ClientHandler extends Thread
{
    Socket c;
    Connection myconn = null;
    Statement mystat = null;
    ResultSet myres = null;
    int ServerId = 1;
    public synchronized String Register (String username,String passsword_of_register,String balance,
            String bank_id_of_register)
    {
        try
        {
             mystat.executeUpdate("insert into client (name,passwor,balance,bank_id) values ('"+username+"','"+passsword_of_register+"',"+balance+","+bank_id_of_register+")");
             String qur = "select id from client where name = '"+username+"' and passwor = '"+passsword_of_register+"' and balance = '"+balance+"' and bank_id = '"+bank_id_of_register+"'";
             ResultSet res = mystat.executeQuery(qur);
             res.next();
             return res.getString("id");
             
        }
        catch(SQLException | NumberFormatException e)
        {   
            return "no";
        }   
    }
    public synchronized String Login(String ID , String pass)
    {
        ResultSet result;
         try
         {
             String qur="SELECT name, balance FROM client WHERE id = '"+ID+"' AND passwor='"+pass+"'";
             result = mystat.executeQuery(qur);
             result.next();
             String name = result.getString("name");
             String balance = result.getString("balance");
             return name+" "+balance;
         }
         catch(SQLException | NumberFormatException e)
         {
            return "no";
         }
    }
    public synchronized String Deposit(String amount,String i)
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
          
        }catch (SQLException | NumberFormatException e)
        {
            
        }
        return new_balance;
    }
    public synchronized String Withdraw(String amount, String id)
    {
         try
        {
            String qur="SELECT balance FROM client WHERE id = '"+id+"'";
            ResultSet result = mystat.executeQuery(qur);
            result.next();
            String Current = result.getString("balance");
            if(Float.parseFloat(Current) >= Float.parseFloat(amount)){
                float curr = Float.parseFloat(Current) - Float.parseFloat(amount);
                String UpQur="UPDATE client SET balance='"+curr+"' WHERE id='"+id+"'";
                mystat.executeUpdate(UpQur);
                return Float.toString(curr);
            }
            else
            {
                return "no";
            }
        }
        catch(SQLException | NumberFormatException f)
        {
            return "no";
        }
    }
    public synchronized String  GetTransHistory(String i)
    {
         String history = "";
         
        try{
            myres = mystat.executeQuery("select * from transication where sender = " + i);
            while(myres.next())
            {
                int r = myres.getInt("reciever");
                float amount = myres.getFloat("amount");
                String date = myres.getString("dateOfSend");
                int BankId = myres.getInt("ReciverBankID");
                String out = "You transfered "+ Float.toString(amount) +" $ To client ID: "+Integer.toString(r)+" registered in Bank: " +Integer.toString(BankId)+" at " +date+"\n";
                history = history + out;    
            }
            myres = mystat.executeQuery("select * from transication where reciever = " + i);
            while(myres.next())
            {
                int s = myres.getInt("sender");
                float amount = myres.getFloat("amount");
                String date = myres.getString("dateOfSend");
                int BankId = myres.getInt("SenderBankID");
                String out = "You recieved "+ Float.toString(amount) +" $ From client ID: "+Integer.toString(s)+" registered in Bank: " +Integer.toString(BankId)+" at " +date+"\n";
                history = history + out;    
            }
        }catch(Exception e)
        {
            
        }
        return history;
        
    }
    public synchronized String transfer(String id1 , String id2 , String amount,String bank_id)
    {
        try
        {
            String qur="SELECT balance, bank_id FROM client WHERE id = '"+id1+"'";
            ResultSet result = mystat.executeQuery(qur);
            result.next();
            String Current = result.getString("balance");
            String BankID = result.getString("bank_id");
            if(Float.parseFloat(Current) >= Float.parseFloat(amount))
            {        
                if(Integer.parseInt(bank_id) == ServerId)
                {                
                    float curr1 = Float.parseFloat(Current) - Float.parseFloat(amount);
                    String UpQur="UPDATE client SET balance='"+curr1+"' WHERE id='"+id1+"'";
                    mystat.executeUpdate(UpQur);
                    String qur3="SELECT balance FROM client WHERE id = '"+id2+"'";
                    ResultSet result2 = mystat.executeQuery(qur3);
                    result2.next();
                    String Current2 = result2.getString("balance");
                    float curr2 = Float.parseFloat(Current2) + Float.parseFloat(amount);
                    String UpQur2="UPDATE client SET balance='"+curr2+"' WHERE id='"+id2+"'";
                    mystat.executeUpdate(UpQur2);
                    Date date = new Date();
                    String InsQur ="INSERT INTO transication (sender,reciever,amount,dateOfSend,ReciverBankID,SenderBankID) VALUES ('"+id1+"','"+id2+"','"+amount+"','"+date.toString()+"','"+bank_id+"','"+BankID+"')";
                    mystat.executeUpdate(InsQur);
                    return Float.toString(curr1);
                }
                else
                {
                    Socket client = new Socket("192.168.43.38", 1234);
                    DataInputStream dis1 = new DataInputStream(client.getInputStream());
                    DataOutputStream dos1 = new DataOutputStream(client.getOutputStream());
                    dos1.writeUTF("s");
                    dos1.writeUTF(id1);
                    dos1.writeUTF(id2);
                    dos1.writeUTF(amount);
                    dos1.writeUTF(bank_id);
                    dos1.writeUTF(BankID);
                    float curr1 = Float.parseFloat(Current) - Float.parseFloat(amount);
                    String UpQur="UPDATE client SET balance='"+curr1+"' WHERE id='"+id1+"'";
                    mystat.executeUpdate(UpQur);
                    Date date = new Date();
                    String InsQur ="INSERT INTO transication (sender,reciever,amount,dateOfSend,ReciverBankID,SenderBankID) VALUES ('"+id1+"','"+id2+"','"+amount+"','"+date.toString()+"','"+bank_id+"','"+BankID+"')";
                    mystat.executeUpdate(InsQur);
                    if("ok".equals(dis1.readUTF()))
                    {
                        return Float.toString(curr1);
                    }
                    else
                    {
                        return "no";
                    }
                }
            }
            else
            {
                return "no";
            }
        }
        catch(NumberFormatException | SQLException | IOException g)
        {
            return "no";
        }
    }
    public ClientHandler (Socket c) {
        this.c = c;
        try
        {    
            myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankingSystem","root","Microsoft880");
            mystat = myconn.createStatement();
        }catch(Exception f)
        {
            
        }
    }   
    @Override
    public void run() {

        try 
        {   
            DataInputStream dis = new DataInputStream(c.getInputStream());
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            String id;
            String l_or_r = dis.readUTF();
            if(l_or_r.equals("s"))
            {
                String id1 = dis.readUTF();
                String id2 = dis.readUTF();
                String amount = dis.readUTF();
                String bank_id = dis.readUTF();
                String BankID = dis.readUTF();
                try{
                    
                    String qur3="SELECT balance FROM client WHERE id = '"+id2+"'";
                    ResultSet result2 = mystat.executeQuery(qur3);
                    result2.next();
                    String Current2 = result2.getString("balance");
                    float curr2 = Float.parseFloat(Current2) + Float.parseFloat(amount);
                    String UpQur2="UPDATE client SET balance='"+curr2+"' WHERE id='"+id2+"'";
                    mystat.executeUpdate(UpQur2);
                    Date date = new Date();
                    String InsQur ="INSERT INTO transication (sender,reciever,amount,dateOfSend,ReciverBankID,SenderBankID) VALUES ('"+id1+"','"+id2+"','"+amount+"','"+date.toString()+"','"+bank_id+"','"+BankID+"')";
                    mystat.executeUpdate(InsQur);
                    dos.writeUTF("ok");
                }
                catch(SQLException | NumberFormatException | IOException d)
                {
                    dos.writeUTF("no");
                }
            }
            else
            {
                while(true)
                {
                    while(true)
                    {
                        if(l_or_r.equals("r"))
                        {
                            String username = dis.readUTF();
                            String password_of_register = dis.readUTF();
                            String balance = dis.readUTF();
                            String bank_id_of_register = dis.readUTF();
                            id =Register(username,password_of_register,balance,bank_id_of_register);
                            dos.writeUTF(id);
                            break;
                        }
                        else
                        {
                            id = dis.readUTF();
                            String p = dis.readUTF();
                            String BalanceAndName = Login(id,p);
                            String name;
                            String pass;
                            if(!BalanceAndName.equals("no"))
                            {
                                String n[] = BalanceAndName.split(" ");
                                name = n[0];
                                pass = n[1]; 
                                dos.writeUTF(name);
                                dos.writeUTF(pass);
                                break;
                            }
                            else
                            {
                                dos.writeUTF("no");
                                l_or_r = dis.readUTF();
                            }
                        }
                    }
                    OUTER:
                    while (true) {
                        String withdraw_or_deposite_or_tran = dis.readUTF();
                        String h;
                        switch (withdraw_or_deposite_or_tran) {
                            case "w":
                                {
                                    String amount = dis.readUTF();
                                    h = Withdraw(amount,id);
                                    dos.writeUTF(h);
                                    break;
                                }
                            case "d":
                                {
                                    String amount = dis.readUTF();
                                    h = Deposit(amount,id);
                                    dos.writeUTF(h);
                                    break;
                                }
                            case "t":
                                    h = GetTransHistory(id);
                                    dos.writeUTF(h);
                                    break;
                            case "f":
                                {
                                    String amount = dis.readUTF();
                                    String id2 = dis.readUTF();
                                    String bank_id = dis.readUTF();
                                    h =  transfer(id,id2,amount,bank_id);
                                    dos.writeUTF(h);
                                    break;
                                }
                            case "u":
                            {
                                try{
                                    myres = mystat.executeQuery("SELECT balance from client WHERE id = "+id);
                                    myres.next();
                                    String current_balance = myres.getString("balance");
                                    dos.writeUTF(current_balance);
                                }
                                catch(SQLException | IOException e)
                                {
                                    
                                }
                                break;
                            }
                            case "x":
                                l_or_r = dis.readUTF();
                                break OUTER;
                            default:
                                break;
                        }
                    }
                }
            }
        } 
        catch (IOException | NumberFormatException e) 
        {
            try {
                System.out.println("Client leaved");
                c.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
}
public class BankingSystem{
   
    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(1234);
            while (true) {
                Socket c = server.accept();
                System.out.println("Client Arrived");
                ClientHandler ch = new ClientHandler(c);
                ch.start();

            }

        } catch (Exception e) {
            System.out.println("Something Went Wrong");
        }
    }
    
}
