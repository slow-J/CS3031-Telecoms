import java.net.DatagramSocket;
import java.io.BufferedReader;
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

        if(banned)
        {
	  header[2]=0;
	}
	else
	{
	  header[2]=1;
	}
        // use header[2] for ban or not
        // send to source
        dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_SRC_PORT);
        buffer = new byte[header.length + payload.length];
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

  public void start() throws SocketTimeoutException
  {
    terminal.println("Waiting for contact");
    try
    {
      this.wait();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static String initBanList()
  {
    // take in from file
    String txt = "";
    BufferedReader reader;
    try
    {
      reader = new BufferedReader(new FileReader("./blacklist.txt"));
      for (String line; (line = reader.readLine()) != null; txt += line)
        ;
    } catch (FileNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return txt;
  }

  public static void add2ban(String banWord, String txt)
  {
    Writer writer = null;
    try
    {
      writer = new FileWriter("blacklist.txt");
      writer.write(txt);
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try
    {
      writer.close();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
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