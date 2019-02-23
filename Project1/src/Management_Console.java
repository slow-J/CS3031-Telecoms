import java.net.DatagramSocket;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
    if(checkIfValidURL(checkBan))
    {
      if(!checkIfBan(checkBan))
      {
        banned=false;
      }
    }
    
  
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
        //header[1] is where data is saved on ban or not ban
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
      int action = (terminal.readInt("press 1 to add url to banlist\n"));
      if (action==1)
      {
        String ban = "http://"+(terminal.readString("type website to ban without the www or http\n"));
        //String ban = "http://"+java.net.URLEncoder.encode(firstban, "UTF-8");
        boolean isValid=checkIfValidURL(ban);
        if(isValid)
        {
          add2ban(ban);
          terminal.println(ban+", successfully added to banlist");
        }
        else
        {
          terminal.println(ban+", not valid");
        }
      }
      else
      {
        //terminal.println("Invalid selection");
        //System.out.println(true);
        if(checkIfBan("http://"+terminal.readString("checkIfBan\n")))
          terminal.println("true");
        else
          terminal.println("false");
      }
    }
}
  private static boolean checkIfValidURL(String tryurl)
  {
    //checks if url valid
    try
    {
      URL url1 = new URL(tryurl); 
      url1.toURI();
    }
    catch (MalformedURLException | URISyntaxException e)
    {
      //e.printStackTrace();
      return false;
    }
    return true;
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
    if (!checkIfBan(banWord))
    {
      // adds banWord to the banlist
      BufferedWriter output = null;
      try
      {
        output = new BufferedWriter(new FileWriter("banlist.txt", true));
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
    else
      System.out.println("Already banned");
  }

  public static boolean checkIfBan(String cmp)
  {
    Path path = Paths.get("banlist.txt");
    ArrayList<String> lines=null;
    try
    {
      lines = (ArrayList<String>) Files.readAllLines(path);
    } catch (IOException e)
    {
      e.printStackTrace();
      return false;
    }
    for(int i=0;i<lines.size();i++)
    {
      if(lines.get(i).equals(cmp))
        return true;
    }
    return false;
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