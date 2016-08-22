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
public final class ChatRoom {
    
    List<ProxyClient> _clientList;
    ServerSocket _serverSocket;
    Thread _clientListener;
    Thread _proxyClient;
    
    public ChatRoom(String name) throws IOException{
        
        _serverSocket = new ServerSocket(58000);
        _serverSocket.setSoTimeout(60000);
        _clientList = new ArrayList<>();
        
        System.out.println("Chat room with name "
                + name
                +" started on:-"
                + _serverSocket.getLocalSocketAddress()
                +":"+ _serverSocket.getLocalPort());
        
        
        this._clientListener = new ClientListener();
        _clientListener.start();
    }
    
    class ClientListener extends Thread {
    
    @Override
    public void run(){
        while(true){
            try {
                Socket socket= _serverSocket.accept();
                _proxyClient = new ProxyClient(socket);
                _clientList.add((ProxyClient) _proxyClient);
                _proxyClient.start();
            } catch (IOException ex) {
                System.out.println("IOException Occoured." + ex);
            }
            catch (Exception ex){
                System.out.println("Generic exception Occoured. " + ex);
            }finally{
            }
        }
    }
    
}
    
    class ProxyClient extends Thread{
    
    Socket _socket;
    BufferedReader _inputFromClient;
    PrintStream _outputToClient;
    
    public ProxyClient(Socket socket) throws IOException{
        _socket = socket;
        _inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        _outputToClient = new PrintStream(socket.getOutputStream());
    }
    
    @Override
    public void run(){
        String line;
        try{
            while(true){
                line = _inputFromClient.readLine();
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
    
    public void writeToClients(String line){
        PrintStream tempObj;
        
        for(ProxyClient pxObj:_clientList){
            if(pxObj.equals(this)){
                
            }else{
                tempObj = pxObj._outputToClient;
                tempObj.println(line);
            }
        }
    }
    
}
    
}




