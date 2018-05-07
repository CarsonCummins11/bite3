package org.bitenet.server;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.LinkedList;
import java.util.Stack;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
 
public class Executor implements Runnable{
    private int port = 9999;
    MongoClient mc;
    public static final String MONGO_URL = "";
    volatile private boolean isServerDone = false;
     volatile private Stack<TrainingTask> training;
    public static void main(String[] args){
    	try {
			new Executor(MONGO_URL);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
     
    Executor(String url) throws UnknownHostException{
    	training = new Stack<>();
    	mc = new MongoClient(new MongoClientURI(url));
    	new Thread(this).start();
    	go();
    }
     
    Executor(int port){
        this.port = port;
    }
    public void kill() {
    	isServerDone = true;
    }
     
    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("bitenet.jks"),"password".toCharArray());
             
            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "passphrase".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();
             
            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();
             
            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(km,  tm, null);
             
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
         
        return null;
    }
     
    // Start to run the server
    public void go(){
        SSLContext sslContext = this.createSSLContext();
         
        try{
            // Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
             
            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);
             
            System.out.println("SSL server started");
            while(!isServerDone){
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                 
                // Start the server thread
                new ServerThread(sslSocket,training).start();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

	@Override
	public void run() {
		while(!isServerDone) {
			try {
				training.pop().execute(mc);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
     
}