package org.bitenet.client;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
 
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JFrame;
import javax.swing.JWindow;

import org.bitenet.lang.Memory;
import org.bitenet.lang.TimeDecayMap;
import org.bitenet.predict.Predictor;
 
public class Remote implements Runnable{
    private String host = "";
    private int port = 9999;
    ObjectOutputStream out;
    ObjectInputStream in;
    SSLSocket sslSocket;
    TimeDecayMap<SlaveDefinition,Memory> predictions;
    Predictor pred;
    volatile JFrame f;
    public static final int WAIT_PERIOD = 5;
    ConnectionShutdown cshut;
	private BufferedImage curScreen;
	public final static double DATA_COLLECTION_RATE = .1;
    public Remote(JFrame ff) throws ClassNotFoundException, FileNotFoundException, IOException{
    	connect();
    	f=ff;
    	pred = Predictor.build();
    	predictions = new TimeDecayMap<>();
    	Thread qqq = new Thread(cshut);
    	cshut = new ConnectionShutdown(pred.app_id,qqq,sslSocket);
    	Runtime.getRuntime().addShutdownHook(qqq);
    }
    public boolean contains(SlaveDefinition sd) {
    	if(Math.random()<DATA_COLLECTION_RATE) {
    	cshut.dataAdd(curScreen,sd);
    	}
		return predictions.containsKey(sd);
	}

	public Memory retrieve(SlaveDefinition sd) {
		return predictions.get(sd);
	}
	public BufferedImage screenShot(JFrame in) {
		int imgWidth = in.getWidth();
		int imgHeight= in.getHeight();
		GraphicsConfiguration gConf = new JWindow().getGraphicsConfiguration();
		BufferedImage img = gConf.createCompatibleImage(imgWidth, imgHeight);
		Graphics g = img.getGraphics();
		f.paint(g);
		g.dispose();
		return img;
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
    public void connect(){
        SSLContext sslContext = this.createSSLContext();
         
        try{
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
             
            // Create socket
           sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);
             
            System.out.println("SSL client started");
            new Thread(this).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private Memory execute(SlaveDefinition sd) throws IOException, ClassNotFoundException {
		out.writeObject(sd);
		return (Memory)in.readObject();
	}
	@Override
	public void run() {
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
               
             in = new ObjectInputStream(inputStream);
              out= new ObjectOutputStream(outputStream);
              cshut.out = out;
               
              while(true) {
            	 curScreen = screenShot(f);
      			SlaveDefinition[] sd = pred.generate(curScreen);
      			try {
      				for (int i = 0; i < sd.length; i++) {
      					predictions.put(sd[i],execute(sd[i]));	
					}
      			} catch (IOException |  ClassNotFoundException e) {
      				e.printStackTrace();
      			}
      		}
          } catch (Exception ex) {
              ex.printStackTrace();
          }
		
	}
}