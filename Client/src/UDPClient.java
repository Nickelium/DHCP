import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;

public class UDPClient 
{
	public InetAddress IPServer; 
	public final int portServer = 69;
	public final String IPServerString = "localhost";
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
			ByteBuffer a = ByteBuffer.allocate(4);
			ByteBuffer b = ByteBuffer.allocate(4);
			ByteBuffer c = ByteBuffer.allocate(4);
			ByteBuffer d = ByteBuffer.allocate(4);

			
			
			// TODO: more options by filling up the field?
			
			// Request
			message.opCode = 1; 
			
			// Length MAC-adress
			message.hardWareAddressLength = 6;
			
			// Hopcount set to zero by client
			message.hopCount = 0;
			
			// Hashcode 32 bit = 4 byte fills transactionID with exactly 4 byte
			a.putInt(message.hashCode());
			message.transactionID = a.array();
			
			// Not yet an IP adress -> null
			message.clientIP = b.putInt(0).array();
			
			// Not yet an IP adress -> null
			message.yourIP = d.putInt(0).array();
			
			// No broadcast because of the assignment, given IP-adress
			message.serverIP = InetAddress.getByName(IPServerString).getAddress();
			
			// TODO: set or not set gatewayIP
			
			// Client hardware adress
			//nullpointer exception when calling gethardwareaddress + each networkinterface has a different mac address
			//final byte[] mac = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
			//message.clientHardWareAddress = mac;
			
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

	
	public void run()
	{
		DHCPDiscover();
		//listen for DHCPOffer
		byte[] buffer = new byte[128];
		int length = buffer.length;
		DatagramPacket receivePacket = new DatagramPacket(buffer,length);
		try 
		{
			clientSocket.receive(receivePacket);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.print("Client receives :\n");
		for(byte b : receivePacket.getData())
			System.out.print(b +" ");
		DHCPRequest();
		//listen for DHCPAck OR DHCPNak
		
		DHCPRelease();
		//Later on DHCPRelease
		clientSocket.close();
	
		
	}
	
}
