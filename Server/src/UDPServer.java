import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class UDPServer 
{
	///release timer ?
	private String[] poolIP;
	private DatagramSocket serverSocket;
	public final int portServer = 68;
	
	public UDPServer()
	{
		try 
		{
			serverSocket = new DatagramSocket(portServer);
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPOffer()
	{
		
	}
	
	public void DHCPAck()
	{
		
	}
	
	public void DHCPNak()
	{
		
	}
	
	public void run()
	{
		while(true)
		{
			//listen to incoming packet
			DatagramPacket receivePacket = new DatagramPacket(BUFFER,BUFFER.length);
			serverSocket.receive(receivePacket);
		
			
			
			//determine type of request
			
			
			//determine type of reply by calling above functions
	
		}
	}
	
	
}
