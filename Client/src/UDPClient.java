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
	/**
	 * Hardcode values
	 */
	public final int portServer = 1234;
	public final String IPServerString = "10.33.14.246";
	public final String IPServerLocalhost = "localhost";
	public final int[] macAddress = {0x40,0xE2,0x30,0xCB,0xDE,0xE3,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public final byte MACLENGTH = 6;
	
	/**
	 * Attributes
	 */
	public InetAddress IPServer; 
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
	
			
			// TODO: more options by filling up the field?
			
			// Request
			message.opCode = DHCPMessage.BOOTREQUEST; 
			
			message.hardWareType = DHCPMessage.ETHERNET;
			// Length MAC-adress
			message.hardWareAddressLength = MACLENGTH;
			
			// Hopcount set to zero by client
			message.hopCount = 0;
			
			// Hashcode 32 bit = 4 byte fills transactionID with exactly 4 byte
			ByteBuffer a = ByteBuffer.allocate(4);
			a.putInt(message.hashCode());
			message.transactionID = a.array();
			
			int[] b = {0,0};
			message.secs = Utility.toBytes(b);
			
			int[] c = {0,0};
			message.flags = Utility.toBytes(c);
			
			
			// Not yet an IP adress -> null
			int[] d = {0,0,0,0};
			message.clientIP = Utility.toBytes(d);
			
			// Not yet an IP adress -> null
			int[] e = {0,0,0,0};
			message.yourIP =  Utility.toBytes(e);
			
			// No broadcast because of the assignment, given IP-adress
			message.serverIP = InetAddress.getByName(IPServerString).getAddress();
			
			// Gateway ip set to o
			int[] f = {0,0,0,0};
			message.gateWayIP = Utility.toBytes(f);
			
			// Client hardware adress
			//nullpointer exception when calling gethardwareaddress + each networkinterface has a different mac address
			//final byte[] mac = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
			
			message.clientHardWareAddress = Utility.toBytes(macAddress);
			
			// TODO: set or not set Server Host Name
			int[] g = new int[64];
			for(int i = 0; i < g.length; i++)
				g[i] = 0;
			message.serverHostName = Utility.toBytes(g);
			
			// TODO: set or not set Boot File Name
			int[] h = new int[128];
			for(int i = 0; i < h.length; i++)
				h[i] = 0;
			message.bootFileName = Utility.toBytes(h);
			
			// Set option 53 to value 1
			
			
			int[] i = {1};
			message.addOption((byte)53, (byte)1, Utility.toBytes(i));
			
			int[] j = {0};
			message.addOption((byte) 255, (byte)0, Utility.toBytes(j));
			
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
		System.out.println(receivePacket.getLength());
		for(byte b : receivePacket.getData())
			System.out.print(b +" ");
		DHCPRequest();
		//listen for DHCPAck OR DHCPNak
		
		DHCPRelease();
		//Later on DHCPRelease
		clientSocket.close();
	
		
	}	
}
