import java.net.DatagramPacket;

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
  static final int DEFAULT_SRC_PORT = 4000;
 // private String url = "http://www.apache.org/";
  /**
   * @param args
   */
  public static void test()
  {
    CloseableHttpClient server = HttpClients.createDefault();
    String a = new String(getUrl());
    HttpGet httpGet = new HttpGet(Proxy_Server.getUrl());
  }
  public static void main(String[] args)
  {
  }
  
  public static String getUrl()
  {
    String url ="";
    return url;
  }
  

  public void onReceipt(DatagramPacket packet)
  {/*
    byte[] buffer = packet.getData();
    int dNumber = buffer[1] & 0xff;
    if ((this.port-2000)==dNumber)
    { 
      StringContent content = new StringContent(packet);
      terminal.println();
      terminal.println("--------------------------------------------------------------------");
      terminal.println("New message received: " + content.toString());
      terminal.println("String to send: ");
      this.notify();
    }
    else
    { 
      int outs;
      if (dNumber>this.port-2000)
        outs = Controller.getOut( 6, port);
      else 
        outs = Controller.getOut( 1, port);
      dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, outs);
      terminal.println();
      terminal.println("Message relayed to port: " + outs);
      
      packet.setSocketAddress(dstAddress);
      
      try 
      {
        socket.send(packet);
      }
      catch (IOException e) 
      {
        e.printStackTrace();
      }
      
    }
    */
  }

}
