import java.net.DatagramSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import tcdIO.*;

/**
 * @author Jakub
 *
 */
public class Management_Console extends Node
{
  static final int DEFAULT_SRC_PORT = 2000;
  static final int DEFAULT_DST_PORT = 4000;
  static final String DEFAULT_DST_NODE = "localhost";
  
  Terminal terminal;
  InetSocketAddress dstAddress;
  // String txt = "not initialised";

  Management_Console(Terminal terminal, String dstHost, int dstPort, int srcPort) throws SocketTimeoutException
  {
    try
    {
      dstAddress = new InetSocketAddress(dstHost, dstPort);
      this.terminal = terminal;
      socket = new DatagramSocket(srcPort);
      listener.go();
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }

  }

  public synchronized void onReceipt(DatagramPacket packet)
  {
    boolean banned = true;	
    StringContent content = new StringContent(packet);
    String checkBan = content.toString();
    //init ban list at start
    //check now

    //make class for banlist and constructor 
	
    // Banlist.checkIfBan(checkBan);

    try
    {
      while (true)
      {
        // add to banlist
        Byte finDest = null;

        DatagramPacket sendPacket = null;

        byte[] payload = null;
        byte[] header = null;
        byte[] buffer = null;

        terminal.println("--------------------------------------------------------------------");
        header = new byte[PacketContent.HEADERLENGTH];

        if (banned)
        {
          header[1] = 0;
        } else
        {
          header[1] = 1;
        }
        // use header[2] for ban or not
        // send to source
        dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_SRC_PORT);
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(payload, 0, buffer, header.length, payload.length);

        terminal.println("Sending packet to proxy: " + DEFAULT_SRC_PORT);
        sendPacket = new DatagramPacket(buffer, buffer.length, dstAddress); // send packet to dest

        socket.send(sendPacket);
        terminal.println("Message sent");
      }
    } catch (SocketTimeoutException s)
    {
      System.out.println("Socket timed out 10 seconds!");
      s.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public synchronized void start() throws Exception 
  {
    initBanList();
    terminal.println("Waiting for contact");
    while (true) 
    {
      //terminal.println("press 1 to add to banlist, 2 to view ");
      int action = (terminal.readInt("press 1 to add to banlist, 2 to view"));
      if (action==1)
      {
        String ban = (terminal.readString("type website to ban"));
        add2ban(ban);
        terminal.println(ban+" successfully added to banlist");
      }
      else if (action==2)
      {
        
      }
      else
      {
        terminal.println("Invalid selection");
      }
    }
}

  public static void initBanList()
  {
    //creates file if doesnt exist
    try
    {
      File myFile; 
      myFile = new File("banlist.txt");
      myFile.createNewFile();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }
  
  public static void add2ban(String banWord)
  {
    //adds banWord to the banlist
    BufferedWriter output=null;
    try
    {
      output = new BufferedWriter(new FileWriter("banlist.txt", true) );
    } catch (IOException e2)
    {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    try
    {
      output.newLine();
      output.append(banWord);
    } catch (IOException e2)
    {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    try
    {
      output.close();
    } catch (IOException e2)
    {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    System.out.println("Bans updated");
  }

  public static boolean checkIfBan(String cmp, String txt)
  {
    // KMP search algorithm from my repository
    // https://github.com/slow-J/TCD/blob/master/Year2/CS2010/KMP/KMPSearch.java

    /************
     * TODO take out http and // from cmp, remove https://, sanitise, make function
     * to santise
     *
     */

    if (txt.length() < 1 || cmp.length() < 1)
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
    try
    {
      Terminal terminal = new Terminal("Management_Console");
      (new Management_Console(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT )).start();
     
      terminal.println("Program completed");
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }
  }

}