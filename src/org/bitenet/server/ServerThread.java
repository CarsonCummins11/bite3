package org.bitenet.server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.bitenet.client.SlaveDefinition;

/* 
 * 
 * Purpose: Thread handling the socket from client
 * Help from: https://www.pixelstech.net/profile.php?user=sonic0002
 * @author Carson Cummins
 * @version 0.0
 */
   public class ServerThread extends Thread {
        private SSLSocket sslSocket = null;
         
        ServerThread(SSLSocket sslSocket){
            this.sslSocket = sslSocket;
        }
         
        public void run(){
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
             
            try{
                // Start handshake
                sslSocket.startHandshake();
                 
                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();

                System.out.println("SSLSession :");
                System.out.println("\tProtocol : "+sslSession.getProtocol());
                System.out.println("\tCipher suite : "+sslSession.getCipherSuite());
                 
                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                ObjectInputStream in = new ObjectInputStream(inputStream);
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                 
                SlaveDefinition sd = null;
                while((sd = (SlaveDefinition)in.readObject()) != null){
                    out.writeObject(sd.execute());
                }
                 
                // Write data
                printWriter.print("HTTP/1.1 200\r\n");
                printWriter.flush();
                 
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }