/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import utility.ReadWriteStream;
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
    
    public Client(String name) throws IOException {
        _socket = new Socket("localhost", 58000);
        System.out.println("Please start chating.");
        this._name = name;
        
        // Console's stream.
        InputStreamReader keyboardInputStream = new InputStreamReader(System.in);
        OutputStream keyboardOutputStream = System.out;
        
        InputStreamReader userInputStream = new InputStreamReader(_socket.getInputStream());
        OutputStream userOutputStream=_socket.getOutputStream();
        
        _keyboardReadStream = new ReadWriteStream(keyboardInputStream,userOutputStream,name);
        System.out.println(_keyboardReadStream);
        _userReadStream = new ReadWriteStream(userInputStream,keyboardOutputStream,name);
        System.out.println(_userInput);
        
        _keyboardReadStream.start();
        _userReadStream.start();
        
    }
}