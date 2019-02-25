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
  static final int DEFAULT_CLIENT_PORT = 1000;
  static final int DEFAULT_SRC_PORT = 2000;
  static final int DEFAULT_DST_PORT = 4000;
  static final String DEFAULT_DST_NODE = "localhost";
  
  Terminal terminal;
  InetSocketAddress dstAddress;

  Management_Console(Terminal terminal,  int srcPort) throws SocketTimeoutException
  {
    try
    {
      // can only have one destination
      dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_DST_PORT);
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
    byte[] buffer = packet.getData();
    byte client_no = buffer[0];
    boolean banned = true;	
    StringContent content = new StringContent(packet);
    String checkBan = content.toString();
    //init ban list at start
    //check now

    if(checkIfValidURL("http://"+checkBan))
    {
      if(!checkIfBan("http://"+checkBan))
      {
        banned=false;
      }
    }
      
    try
    {

      // add to banlist
      DatagramPacket sendPacket = null;

      byte[] payload = checkBan.getBytes();
      byte[] header = new byte[PacketContent.HEADERLENGTH];
      buffer = null;

      terminal.println("--------------------------------------------------------------------");
      // header[1] is where data is saved on ban or not ban
      if (banned)
      {
        header[1] = 0;
      } else
      {
        header[1] = 1;
      }
      header[0]= client_no;
      // send to source
      buffer = new byte[header.length + payload.length];
      System.arraycopy(header, 0, buffer, 0, header.length);
      System.arraycopy(payload, 0, buffer, header.length, payload.length);
      terminal.println(checkBan + ": is banned:" + banned);
      terminal.println("Sending packet to port: " + DEFAULT_DST_PORT);
      sendPacket = new DatagramPacket(buffer, buffer.length, dstAddress); 
      // send packet to dest

      socket.send(sendPacket);
      terminal.println("Message sent");

    } catch (IOException e)
    {
      e.printStackTrace();
    }
    this.notify();
  }

  public void start() throws Exception 
  {
    initBanList();
    terminal.println("Waiting for contact");
    while (true) 
    {
      int action = (terminal.readInt("press 1 to add url to banlist or 2 to check is a url banned\n"));
      if (action==1)
      {
        String ban = "http://"+(terminal.readString("type website to ban without the www or http\n"));
        if(checkIfValidURL(ban))
        {
          add2ban(ban);
        }
        else
        {
          terminal.println(ban+", not a valid url");
        }
      }
      else if(action==2)
      {
        String ban = "http://"+(terminal.readString("type website to ban without the www or http\n"));
        if(!checkIfValidURL(ban))
        {
          terminal.println(ban+", not a valid url");
        } 
        else
        {
          if (checkIfBan(ban))
            terminal.println(ban + ", is banned");
          else
            terminal.println(ban + ", is not  banned");
        }
      }
      else
      {
        terminal.println("Invalid selection");       
      }
    }
  }
  /* 
   * @param url including http://
   * 
   * checks to see if the syntax is valid or url is malformed
   * 
   * @return true if valid url
   */
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

  public void add2ban(String banWord)
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
        //adds the banned url to the next line in the txt file
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
      terminal.println(banWord+", successfully added to banlist");
    }
    else
      terminal.println(banWord+", already banned");
  }

  public static boolean checkIfBan(String cmp)
  {
    Path path = Paths.get("banlist.txt");
    ArrayList<String> lines=null;
    try
    {
      //each line of txt file to arraylist of strings
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
      //true if arraylist contains the compared word
    }
    return false;
  }

  public static void main(String[] args)
  {
    try
    {
      Terminal terminal = new Terminal("Management_Console");
      (new Management_Console(terminal,DEFAULT_SRC_PORT )).start();
     
      terminal.println("Program completed");
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }
  }

}