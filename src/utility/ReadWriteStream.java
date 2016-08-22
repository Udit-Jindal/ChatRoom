/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;


/**
 *
 * @author Raghu
 */
public class ReadWriteStream extends Thread{
    static final Charset _utf8 = Charset.forName("UTF-8");
    
    public OutputStream _outputStream;
    public InputStream _inputStream;
    
    public BufferedReader _inputBufferedReader;
    public PrintStream _outputPrintStream;
    
    public String _name;
    
    public ReadWriteStream(InputStream inputStream, OutputStream outputStream, String name) throws IOException {
        super("Input from thread");
        
        _inputStream = inputStream;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        _inputBufferedReader = new BufferedReader(inputStreamReader);
        
        _outputStream = outputStream;
        _outputPrintStream=new PrintStream(outputStream);
        
        _name = name;
    }
    
    @Override
    public void run(){
        
        try {
            while(true){
                this._outputPrintStream.println(_name+"=>"+_inputBufferedReader.readLine());
            }
        } catch (Exception ex) {
            System.out.println("IOException"+ex);
        }
    }
    
}
