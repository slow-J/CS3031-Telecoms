
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author slowinsj
 *
 */
public class Web_Proxy_Client
{
  static final String DEFAULT_DST_NODE = "localhost";
  static final int DEFAULT_SRC_PORT = 2001;
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://targethost/homepage");
  }

}
