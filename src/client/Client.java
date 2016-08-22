/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import utility.ReadFromStream;
import java.net.Socket;

/**
 *
 * @author Raghu
 */
public class Client {
    
    
    public Socket _socket;
    public PrintStream _outputToStream;
    public BufferedReader _userInput;
    public String _name;
    public Thread _keyboardReadStream;
    public BufferedReader _inputFromStream;
    public Thread _userReadStream;
    
    public Client(Socket socket, String name) {
        this._socket = socket;
        this._name = name;
    }
    
    public static void main(String args[]) throws IOException, InterruptedException{
        
        
        BufferedReader userInputMain=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter your name:- ");
        String name = userInputMain.readLine();
        Socket socket = null;
        
        socket = new Socket("localhost", 58000);
        System.out.println("Please start chating.");
        
        Client client = new Client(socket,name);
        client.fire();
    }
    
    public void fire() throws IOException, InterruptedException
    {
        
        _keyboardReadStream = new ReadFromStream(_socket);
        _userReadStream = new ReadFromStream(this._name,_socket);
        
        _keyboardReadStream.start();
        _userReadStream.start();
    }
}
