import java.io.IOException;
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
	public final int portServer = 69;
	
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
	
	public void DHCPOffer(DatagramPacket receivePacket)
	{
		DHCPMessage message = new DHCPMessage(receivePacket.getData());
		
		message.opCode = 2;
		//Other fields to fill in
		byte[] buffer = message.retrieveBytes();
		int length = buffer.length;
		// Mag dit wel ? Aangeziien de client nog geen ip address heeft ? 
		// Echte DHCP gebeurt via broadcasting
		InetAddress IPClient = receivePacket.getAddress();
		int portClient = receivePacket.getPort();
		DatagramPacket response = new DatagramPacket(buffer, length, IPClient, portClient);
		try 
		{
			serverSocket.send(response);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		
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
			byte[] buffer = new byte[256];
			DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);
			try 
			{
				serverSocket.receive(receivePacket);
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			System.out.print("Server receives :\n");
			for(byte b : receivePacket.getData())
				System.out.print(b+" ");
			
			
			//determine type of request
			DHCPMessage response = new DHCPMessage(receivePacket.getData());
			
			//determine type of reply by calling above functions
			DHCPOffer(receivePacket);
		}
	}
	
	
}
