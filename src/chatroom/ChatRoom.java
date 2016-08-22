/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package chatroom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raghu
 */
public class ChatRoom {
    
    List<ProxyClient> _clientList;
    ServerSocket _serverSocket;
    Thread _clientListener;
    Thread _clientReaderWriter;
    int _executionFlag;
    
//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    
    public ChatRoom(ServerSocket serverSocket){
        _serverSocket = serverSocket;
        _executionFlag = 0;
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
            System.out.println("Port is un-available. Exiting.");
            System.exit(1);
        }catch(IOException ex){
            System.out.println("Sorry All channels busy. Please try later.");
            System.exit(1);
        }
        
        System.out.println("Chat room with name "
                + name
                +" started on:-"
                + serverSocket.getLocalSocketAddress()
                +":"+ serverSocket.getLocalPort());
        
        ChatRoom chatRoom = new ChatRoom(serverSocket);
        chatRoom.listenForNewConnections();
    }
    
    public void listenForNewConnections(){
        this._clientListener = new ClientListener(_serverSocket,this);
        _clientListener.start();
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
                ProxyClient proxyClient = new ProxyClient(socket, Integer.toString(socket.getLocalPort()), _chatRoom);
                this._chatRoom._clientList.add(proxyClient);
                Thread thread = new Thread(proxyClient);
                thread.start();
            } catch (IOException ex) {
                System.out.println("Error while listening for client");
            }
            catch (Exception ex){
                System.out.println("Error while listening for client");
            }
        }
    }
    
}

class ProxyClient implements Runnable{
    
    Socket _socket;
    String _name;
    BufferedReader _inputFromClient;
    PrintStream _outputToClient;
    ChatRoom _chatroomObject;
    
    public ProxyClient(Socket socket,String name,ChatRoom chatRoomObject) throws IOException{
        _socket = socket;
        _name = name;
        _inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        _outputToClient = new PrintStream(socket.getOutputStream());
        _chatroomObject = chatRoomObject;
    }
    
    @Override
    public void run(){
        String line;
        try{
            while(true){
                line = readFromClient();
                if(!line.isEmpty())
                {
                    writeToClients(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProxyClient.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                _inputFromClient.close();
                _outputToClient.close();
            } catch (Exception ex) {
                System.out.println("Un-able to close the streams.");
            }
        }
    }
    
    public String readFromClient() throws IOException{
        String line = _inputFromClient.readLine();
        return line;
    }
    
    public void writeToClients(String line){
        int i;
        int numberOfClients = _chatroomObject._clientList.size();
        PrintStream tempObj;
        for(i=0;i<numberOfClients;i++){
            ProxyClient tempProxyClientObject = _chatroomObject._clientList.get(i);
            if(tempProxyClientObject.equals(this)){
                
            }else{
                tempObj = tempProxyClientObject._outputToClient;
                tempObj.println(line);
            }
        }
    }
    
}