/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raghu
 */
public class ReadFromStream extends Thread{
    public BufferedReader _inputStream;
    public PrintStream _outputStream;
    public String _line="Input from stream";
    public String _name;
    private int _flag = 0;
    private Socket _socket;
    
    public ReadFromStream(Socket socket) throws IOException {
        super("Input from thread");
        this._socket = socket;
        this._inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public ReadFromStream(String name,Socket socket) throws IOException {
        super("Input from thread");
        this._socket = socket;
        InputStreamReader keyboardStream = new InputStreamReader(System.in);
        this._inputStream = new BufferedReader(keyboardStream);
        this._outputStream = new PrintStream(socket.getOutputStream());
        this._flag = 1;
        this._name = name;
    }
    
    @Override
    public void run(){
        try {
            while(!this._line.equalsIgnoreCase("DIE")){
                read();
            }
            this.interrupt();
        } catch (IOException ex) {
            System.out.println("IOException"+ex);
        }
    }
    
    public void read()throws IOException{
        this._line = _inputStream.readLine();
        if(this._flag==0)
            System.out.println(this._line);
        else{
            this._outputStream.println(_name+"=>"+this._line);
        }
    }
}
