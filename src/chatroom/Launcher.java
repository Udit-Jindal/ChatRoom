package chatroom;


import chatroom.ChatRoom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Raghu
 */
public class Launcher {
    
    public static ChatRoom _chatRoom;
    
    public static void main(String[] args) throws IOException {
        
        try{
            
            BufferedReader userInputMain=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the chat room name:- ");
            String name = userInputMain.readLine();
            _chatRoom = new ChatRoom(name);
        }catch(BindException bindException){
            System.out.println("Port is un-available. Exiting." + bindException);
            System.exit(1);
        }catch(IOException ex){
            System.out.println("IOException Occoured." + ex);
            System.exit(1);
        }
        

    }
    
}
    

