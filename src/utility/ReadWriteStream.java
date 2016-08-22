/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 *
 * @author Raghu
 */
public class ReadWriteStream extends Thread{
    static final Charset _utf8 = Charset.forName("UTF-8");
//    public InputStreamReader _inputStream;
    public BufferedReader _inputBufferedReader;
    public OutputStream _outputStream;
    public String _name;
    
    public ReadWriteStream(InputStreamReader inputStream, OutputStream outputStream, String name) throws IOException {
        super("Input from thread");
        _inputBufferedReader = new BufferedReader(inputStream);
        _outputStream = outputStream;
        _name = name;
    }
    
    @Override
    public void run(){
        System.out.println(this.toString());
        try {
            String line = null;
            while(!line.equalsIgnoreCase("DIE")){
                line = _name+"=>"+_inputBufferedReader.readLine();
                this._outputStream.write(line.getBytes(_utf8));
            }
        } catch (Exception ex) {
            System.out.println("IOException"+ex);
        }
    }
    
}
