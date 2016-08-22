/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    public String _name;
    public Thread _keyboardReadStream;
    public BufferedReader _inputFromStream;
    public Thread _userReadStream;
//    public InputStream _keyboardInputStream;
//    public OutputStream _keyboardOutputStream;
//    public InputStream _userInputStream;
//    public OutputStream _userOutputStream;
    
    public Client(String name) throws IOException {
        _socket = new Socket("localhost", 58000);
        System.out.println("Please start chating.");
        this._name = name;
        
        // Console's stream.
//        _keyboardInputStream = System.in;
//        _keyboardOutputStream = System.out;
        
//        _userInputStream = _socket.getInputStream();
//        _userOutputStream=_socket.getOutputStream();
        
        _keyboardReadStream = new ReadWriteStream(System.in,_socket.getOutputStream(),name);
        _userReadStream = new ReadWriteStream(_socket.getInputStream(),System.out,name);
        
        _keyboardReadStream.start();
        _userReadStream.start();
        
    }
}