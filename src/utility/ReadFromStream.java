/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

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
    
    public ReadFromStream(BufferedReader inputFromStream,String name) {
        super("Input from thread");
        this._name = name;
        this._inputStream = inputFromStream;
    }
    
    public ReadFromStream(BufferedReader inputFromStream,PrintStream outputStream) {
        super("Input from thread");
        this._inputStream = inputFromStream;
        this._outputStream = outputStream;
        this._flag = 1;
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
        this._line = _name+":-"+_inputStream.readLine();
        if(this._flag==0)
            System.out.println(this._line);
        else{
            this._outputStream.println(this._line);
        }
    }
}
