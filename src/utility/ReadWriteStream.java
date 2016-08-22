/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;


/**
 *
 * @author Raghu
 */
public class ReadWriteStream extends Thread{
    static final Charset _utf8 = Charset.forName("UTF-8");
    
    public OutputStream _outputStream;
    public InputStream _inputStream;
    public String _name;
    
    public ReadWriteStream(InputStream inputStream, OutputStream outputStream, String name) throws IOException {
        super("Input from thread");
        _inputStream = inputStream;
        _outputStream = outputStream;
        _name = name;
    }
    
    @Override
    public void run(){
        byte[] buf=new byte[1024];
        try {
            int bytes_read;
            while(true){
                bytes_read = _inputStream.read(buf, 0, buf.length);
                
                if(bytes_read < 0) {
                    System.err.println("Server: Tried to read from socket, read() returned < 0,  Closing socket.");
                    return;
                }

                _outputStream.write(buf, 0, bytes_read);
                _outputStream.flush();
            }
        } catch (Exception ex) {
            System.out.println("IOException"+ex);
        }
    }
    
}
