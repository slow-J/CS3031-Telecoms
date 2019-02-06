import java.net.DatagramPacket;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 
 */

/**
 * @author slowinsj
 *
 */
public class Proxy_Server extends Node
{
 // private String url = "http://www.apache.org/";
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    CloseableHttpClient server = HttpClients.createDefault();
    String a = new String(getUrl());
    HttpGet httpGet = new HttpGet(Proxy_Server.getUrl());
  }
  
  public static String getUrl()
  {
    String url ="";
    return url;
  }
  

  @Override
  public void onReceipt(DatagramPacket packet)
  {
    // TODO Auto-generated method stub
    
  }

}
