import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import tcdIO.*;
/**
 * 
 */

/**
 * @author slowinsj
 *
 */
public class Proxy_Server extends Node
{
  Terminal terminal;
  InetSocketAddress dstAddress;
  static final String DEFAULT_DST_NODE = "localhost";
  static final int DEFAULT_SRC_PORT = 4000;
  static final int DEFAULT_CLIENT_PORT = 1000;
  static final int DEFAULT_MANAGE_PORT = 2000;
 // private String url = "http://www.apache.org/";
  /**
   * @param args
   */
  /*
   * public static void test() { CloseableHttpClient server =
   * HttpClients.createDefault(); String a = new String(getUrl()); HttpGet
   * httpGet = new HttpGet(Proxy_Server.getUrl()); }
   */
  Proxy_Server(Terminal terminal, int src_port )
  {
    try
    {
      this.terminal = terminal;
      socket = new DatagramSocket(DEFAULT_SRC_PORT);
      listener.go();
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) 
  {
    try
    {
      Terminal terminal = new Terminal("Proxy_Server");
      (new Proxy_Server(terminal,DEFAULT_SRC_PORT)).start();

      terminal.println("Program completed");
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onReceipt(DatagramPacket packet)
  {
    byte[] buffer = packet.getData();
    StringContent content = new StringContent(packet);
    int notBan = buffer[1];
    terminal.println(""+notBan);
    if ((notBan)<0)
    { 
      dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_MANAGE_PORT);
      terminal.println("Sent to manage");
      packet.setSocketAddress(dstAddress);
      
      try
      {
        socket.send(packet);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      
      
      this.notify();
    }
    else if(notBan==1)//http request
    { /*
      String urlString = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=de26f243bbc04aa67175c1c1c5dc";
      
      URL urlObject = new URL(urlString);
      HttpURLConnection connect = (HttpURLConnection) urlObject.openConnection();
      connect.setRequestMethod("GET");
      
      int responseCode = connect.getResponseCode(); 
      System.out.println("response code is " + responseCode);
*/
       
      
    }
    else
    {
      Byte finDest=null;
      
      DatagramPacket newPacket = null;

      byte[] payload = null;
      byte[] header = null;
      buffer = null;
      payload = (content.toString()+" BANNED").getBytes();
      dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_CLIENT_PORT);
     
      System.arraycopy(header, 0, buffer, 0, header.length);
      System.arraycopy(payload, 0, buffer, header.length, payload.length);
      terminal.println("Sending packet to port: " + DEFAULT_CLIENT_PORT);
      packet = new DatagramPacket(buffer, buffer.length, dstAddress); // send
                                                                      // packet
                                                                      // to dest

      try
      {
        socket.send(packet);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      terminal.println("Message sent");
    }
    
  }

  public synchronized void start() throws Exception 
  {
    terminal.println("Waiting for contact");
    this.wait();
  }

}
