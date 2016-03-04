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
			
			// TODO: more options by filling up the field?
			
			// Request
			message.opCode = 1; 
			
			// Length MAC-adress
			message.hardWareAddressLength = 6;
			
			// Hopcount set to zero by client
			message.hopCount = 0;
			
			// Hashcode 32 bit = 4 byte fills transactionID with exactly 4 byte
			b.putInt(message.hashCode());
			message.transactionID = b.array();
			
			// Not yet an IP adress -> null
			message.clientIP = b.putInt(0).array();
			
			// Not yet an IP adress -> null
			message.yourIP = b.putInt(0).array();
			
			// No broadcast because of the assignment, given IP-adress
			message.serverIP = IPServerString.getBytes();
			
			// TODO: set or not set gatewayIP
			
			// Client hardware adress
			final byte[] mac = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
			message.clientHardWareAddress = mac;
			
			// TODO: set or not set Server Host Name
			
			// TODO: set or not set Boot File Name
			
			// Set option 53 to value 1
			ByteBuffer option = ByteBuffer.allocate(4);
			option.putInt(1);
			message.addOption((byte)53, (byte)1, option.array());
			
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
