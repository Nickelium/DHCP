import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class UDPClient 
{
	public InetAddress IPServer; 
	public final int portServer = 1234;
	public final String IPServerString = "10.33.14.246";
	private DatagramSocket clientSocket;
	
	public  UDPClient()
	{
		try 
		{
			clientSocket = new DatagramSocket();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPDiscover() 
	{
		try 
		{
			DHCPMessage message = new DHCPMessage();
			ByteBuffer b = ByteBuffer.allocate(4);
			//hashcode 32 bit = 4 byte fills transactionID with exactly 4 byte
			b.putInt(message.hashCode());
			message.opCode = 1; //request
			message.hardWareAddressLength = 6;
			message.hopCount = 0;
			message.transactionID = b.array();
			message.clientIP = b.putInt(0).array();
			message.yourIP = b.putInt(0).array();
			
			IPServer = InetAddress.getByName(IPServerString);
			DatagramPacket sendingPacket = new DatagramPacket(message.retrieveBytes(), message.getLength(), IPServer, portServer);
			clientSocket.send(sendingPacket);
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public  void DHCPRequest()
	{
	
	}
	
	public void DHCPRelease()
	{
		
	}
	
}
