package org.bitenet.server;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.bitenet.client.SlaveDefinition;
import org.bitenet.lang.Memory;

/* 
 * 
 * Purpose: Thread handling the socket from client
 * Help from: https://www.pixelstech.net/profile.php?user=sonic0002
 * @author Carson Cummins
 * @version 0.0
 */
   public class ServerThread extends Thread {
        private SSLSocket sslSocket = null;
         Queue<TrainingTask> tasks;
        ServerThread(SSLSocket sslSocket,Queue<TrainingTask> tt){
            this.sslSocket = sslSocket;
            tasks = tt;
        }
        @SuppressWarnings("unchecked")
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
                ObjectInputStream in = new ObjectInputStream(inputStream);
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                 
                SlaveDefinition sd = null;
                while((sd = (SlaveDefinition)in.readObject()) != null){
                    out.writeObject(sd.execute());
                }
         
				ArrayList<BufferedImage> ts_dat = (ArrayList<BufferedImage>) in.readObject();
                HashMap<BufferedImage,String> clas_dat = (HashMap<BufferedImage,String>)in.readObject();
                HashMap<String,HashMap<BufferedImage,Memory>> arg_dat = (HashMap<String,HashMap<BufferedImage,Memory>>)in.readObject();
                int app = (Integer)in.readObject();
                tasks.add(new TrainingTask(app,ts_dat,clas_dat,arg_dat));
                if(!sslSocket.isClosed()) {
                sslSocket.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }