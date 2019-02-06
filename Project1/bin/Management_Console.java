import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
/**
 * @author Jakub
 *
 */
public class Management_Console
{
  static final int DEFAULT_SRC_PORT = 1;
  static final int DEFAULT_DST_PORT = 4; 
  static final String DEFAULT_DST_NODE = "localhost";
  private int port;
  private byte[] lastPayload;
  Terminal terminal;
  InetSocketAddress dstAddress;
  private String txt = "not initialised";

	Management_Console(Terminal terminal, String dstHost, int srcPort) throws SocketTimeoutException
	{
	  try 
		{
			setPort(srcPort);
			this.terminal = terminal;			
			socket = new DatagramSocket(srcPort);			
			listener.go();
		} 
		catch (java.lang.Exception e) 
		{
			e.printStackTrace();
		}
		
	}
  public synchronized void onReceipt(DatagramPacket packet) 
  {
    //checkIfBan();
  }
  public void start() throws SocketTimeoutException
  {
    try
    {
      while (true) 
      {
        //add to banlist
      }
    }
  }

  public void setPort(int p) 
  {
    this.port = p;
  }
  public static void initBanList()
  {
     BufferedReader reader = new BufferedReader(new FileReader("./blacklist.txt"));
     
    for (String line; (line = reader.readLine()) != null; txt += line);  
  }
  public static void add2ban(String banWord)
  {





    Writer writer = new FileWriter("blacklist.txt");
    writer.write(txt);
    writer.close();
    System.out.println("Bans updated");
  }
  public static boolean checkIfBan(String cmp)
  {
    // KMP search algorithm from my repository https://github.com/slow-J/TCD/blob/master/Year2/CS2010/KMP/KMPSearch.java
  

    /************ TODO
    * take out http and // from cmp, remove https://, sanitise, make function to santise
    *
    */

    if(txt.length()<1||cmp.length()<1)
       return false;
    int lenP = cmp.length();
    int lenT = txt.length();
    int i, j;
    for (i = 0, j = 0; i < lenT && j < lenP; i++)
    {
      if (txt.charAt(i) == cmp.charAt(j))
        j++;
      else
      {
        i -= j;
        j = 0;
      }
    }   
    if (j == lenP)
      return true; // found
    else 
      return false; // not found

  }
	public static void main(String[] args)
	{

  }

}