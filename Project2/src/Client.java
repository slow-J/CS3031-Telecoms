
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import tcdIO.*;

/**
 * @author slowinsj
 *
 */
public class Client extends Node
{
  static final String DEFAULT_DST_NODE = "localhost";
  static final int DEFAULT_SRC_PORT = 1000;
  static final int DEFAULT_DST_PORT = 4000;
  byte client_no;
  Terminal terminal;
  InetSocketAddress dstAddress;
  /**
   * @param args
   */
  Client(Terminal terminal,byte client,  int src_port) throws SocketTimeoutException {
    try
    {     
      client_no = client;
      this.terminal = terminal;
      socket = new DatagramSocket(src_port);
      listener.go();
    } catch (java.lang.Exception e)
    {
      e.printStackTrace();
    }

  }

  public void start() throws SocketTimeoutException
  {

    while (true)
    {
      DatagramPacket packet = null;

      byte[] payload = null;
      byte[] header = null;
      byte[] buffer = null;

      payload = (terminal.readString("Website to access (without http or www): \n")).getBytes();
      header = new byte[PacketContent.HEADERLENGTH];
      header[0] = (byte)client_no;
      header[1] = -1;
      dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_DST_PORT);
      buffer = new byte[header.length + payload.length];
      System.arraycopy(header, 0, buffer, 0, header.length);
      System.arraycopy(payload, 0, buffer, header.length, payload.length);

      packet = new DatagramPacket(buffer, buffer.length, dstAddress); 
      // send packet to dest
      try
      {
        socket.send(packet);
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      terminal.println("Packet sent to port: " + DEFAULT_DST_PORT);
      terminal.println("--------------------------------------------------------------------");
      
    }
    

  }

  public static void main(String[] args)
  {
    try 
    {
      Terminal terminal = new Terminal("Client");
      byte client_no = terminal.readByte("Enter client number: ");
      (new Web_Proxy_Client(terminal, client_no,DEFAULT_SRC_PORT+client_no)).start();

      terminal.println("Program completed");
    }
    catch (java.lang.Exception e) 
    {
      e.printStackTrace();
}
  }

 
  public synchronized void onReceipt(DatagramPacket packet) 
  {
    StringContent content = new StringContent(packet);
    terminal.println("New message received:\n " + content.toString());
    terminal.println("--------------------------------------------------------------------");
    
    this.notify();
  }

}
