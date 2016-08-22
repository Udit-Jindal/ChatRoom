/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Raghu
 */
public class ChatRoom {
    
    List<Socket> _clientList;
    ServerSocket _serverSocket;
    Socket _socket;
    Thread _clientListener;
    Thread _clientReaderWriter;
//    int _executionFlag;
    
//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    
    public ChatRoom(ServerSocket serverSocket){
        _serverSocket = serverSocket;
//        _executionFlag = 0;
_clientList = new ArrayList<>();
    }
    
    public static void main(String[] args) throws IOException {
        
        BufferedReader userInputMain = null;
        ServerSocket serverSocket = null;
        String name = null;
        try{
            
            userInputMain=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the chat room name:- ");
            name = userInputMain.readLine();
            
            serverSocket = new ServerSocket(58000);
            serverSocket.setSoTimeout(60000);
        }catch(BindException bindException){
            serverSocket = new ServerSocket(0);
            System.out.println("Port was busy");
        }catch(IOException ex){
            System.out.println("Sorry All channels busy. Please try later.");
        }
        
        System.out.println("Chat room with name "
                + name
                +" started on:-"
                + serverSocket.getLocalSocketAddress()
                +":"+ serverSocket.getLocalPort());
        
        ChatRoom chatRoom = new ChatRoom(serverSocket);
        chatRoom.listenForNewConnections();
        chatRoom.broadcastChat();
    }
    
    public void listenForNewConnections(){
        this._clientListener = new ClientListener(_serverSocket,this);
        _clientListener.start();
    }
    
    public void broadcastChat(){
        _clientReaderWriter = new ClientReaderWriter(this);
        _clientReaderWriter.start();
    }
    
}

class ClientListener extends Thread {
    
    ServerSocket _serverSocket;
    ChatRoom _chatRoom;
    
    public ClientListener(ServerSocket serverSocket,ChatRoom chatRoom) {
        _serverSocket = serverSocket;
        _chatRoom = chatRoom;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Socket socket= this._serverSocket.accept();
                this._chatRoom._clientList.add(socket);
                System.out.println("User connected. You can start chating.");
//                if(this._chatRoom._executionFlag==0){
//                    this._chatRoom.broadcastChat();
//                }
            } catch (IOException ex) {
                System.out.println("Error while listening for client");
            }
        }
    }
    
}

class ClientReaderWriter extends Thread {
    
    public ChatRoom _chatRoom;
    
    public ClientReaderWriter(ChatRoom chatRoom){
        _chatRoom = chatRoom;
    }
    
    @Override
    public void run(){
        try {
            readFromClient();
        } catch (IOException ex) {
            System.out.println("Error while reading and writing from/to client");
        }
    }
    
    public void readFromClient() throws IOException{
        
        int numberOfClients = this._chatRoom._clientList.size();
        while(true && numberOfClients>0){
            int i = 0;
            BufferedReader inputFromClient = null;
            System.out.println("Inside readFromClient");
            
            while (i < numberOfClients) {
                Socket currentClient = _chatRoom._clientList.get(i);
                inputFromClient=new BufferedReader(new InputStreamReader(currentClient.getInputStream()));
                System.out.println("input is ready to be read from:- "+currentClient);
                String line = inputFromClient.readLine();
                writeToClients(currentClient,line);
                i++;
            }
        }
    }
    
    public void writeToClients(Socket currentClient,String line) throws IOException{
        System.out.println("came insiede this method.");
        int i = 0;
        int numberOfClients = _chatRoom._clientList.size();
        while (i < numberOfClients) {
            Socket tempSocket = _chatRoom._clientList.get(i);
            if(tempSocket.equals(currentClient))
            {
                System.out.println("current client. Not writing :- "+tempSocket);
            }
            else{
                System.out.println("writing to client:- "+tempSocket);
                PrintStream outputToClient=new PrintStream(tempSocket.getOutputStream());
                outputToClient.println(line);
            }
            i++;
        }
    }
    
}
