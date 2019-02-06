
/**
 * @author Jakub
 *
 */
public class Management_Console
{
  static final int DEFAULT_SRC_PORT = 2001;
  static final int DEFAULT_DST_PORT = 1004; 
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
  public static void initBanList()
  {
     BufferedReader reader = new BufferedReader(new FileReader("C:\\UNIV\\Redes\\workspace\\Copy of Ex_4.3_Teste\\lists\\blacklist.txt"));
     
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