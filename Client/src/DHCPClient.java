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


public class DHCPClient 
{
	/**
	 * Hardcode values
	 */
	public final int portServer = 1234;
	public final String IPServerString = "10.33.14.246";
	public final String IPServerLocalhost = "localhost";
	
	public final String IPInUse = IPServerLocalhost;
	
	//macaddress with padding
	public final int[] macAddress = {0x40,0xE2,0x30,0xCB,0xDE,0xE3,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	public final byte MACLENGTH = 6;
	
	/**
	 * Attributes
	 */
	public InetAddress IPServer; 
	private DatagramSocket clientSocket;
	
	
	public  DHCPClient()
	{
		System.out.print("Initialization of the client started\n");
		try 
		{
			clientSocket = new DatagramSocket();
			System.out.println("Initialization of the client completed\n");
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPDiscover() 
	{
		System.out.print("Building DHCPDiscover\n");
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
			
			// Set server IP to zero
			int[] e2 = {0,0,0,0};
			message.serverIP =  Utility.toBytes(e2);
			
			// Gateway ip set to o
			int[] f = {0,0,0,0};
			message.gateWayIP = Utility.toBytes(f);
			
			// Client hardware adress			
			message.clientHardWareAddress = Utility.toBytes(macAddress);
			
			// Optional: set or not set Server Host Name
			int[] g = new int[64];
			for(int i = 0; i < g.length; i++)
				g[i] = 0;
			message.serverHostName = Utility.toBytes(g);
			
			// Optional: set or not set Boot File Name
			int[] h = new int[128];
			for(int i = 0; i < h.length; i++)
				h[i] = 0;
			message.bootFileName = Utility.toBytes(h);
			
			message.magicCookie = DHCPMessage.COOKIE;
			
			// Set option 53 to value 1
			int[] i = {1};
			message.addOption((byte)53, (byte)1, Utility.toBytes(i));
			
			// Set option 255 to indicate the end of the message
			int[] j = {0};
			message.addOption((byte) 255, (byte)0, Utility.toBytes(j));
			
			IPServer = InetAddress.getByName(IPInUse);
			DatagramPacket sendingPacket = new DatagramPacket(message.retrieveBytes(), message.getLength(), IPServer, portServer);
			clientSocket.send(sendingPacket);	
			
			System.out.println("DHCPDiscover sent\n");
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public  void DHCPRequest(DatagramPacket receivePacket){
		try
		{
			System.out.print("Building DHCP request \n");
			DHCPMessage message = new DHCPMessage(receivePacket.getData());
			
			// Set the opcode to request
			message.opCode = DHCPMessage.BOOTREQUEST;
			
			// clear the options
			message.resetoptions();
			
			// Set option 50 to the offered IP adress
			message.addOption((byte)50, (byte)4, message.yourIP);
			
			// Set yout IP to zero
			int[] yourip = {0,0,0,0};
			message.yourIP =  Utility.toBytes(yourip);
			
			// Set option 53 to value 3
			int[] i = {3};
			message.addOption((byte)53, (byte)1, Utility.toBytes(i));
			
			// Set option 54 to the IP adress of the server
			message.addOption((byte)54, (byte)4, message.serverIP);
						
			// Set option 255 to indicate the end of the message
			int[] j = {0};
			message.addOption((byte) 255, (byte)0, Utility.toBytes(j));
			
			IPServer = InetAddress.getByAddress(message.serverIP);
			int portServer = receivePacket.getPort();
			DatagramPacket sendingPacket = new DatagramPacket(message.retrieveBytes(), message.getLength(), IPServer, portServer);
			clientSocket.send(sendingPacket);	
			System.out.println("DHCPRequest sent\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPRelease()
	{
		
	}

	
	public void run()
	{
		DHCPDiscover();
		//listen for DHCPOffer
		byte[] buffer = new byte[576];
		int length = buffer.length;
		DatagramPacket receivePacketOffer = new DatagramPacket(buffer,length);
		try 
		{
			clientSocket.receive(receivePacketOffer);
			System.out.print("Client receives packet with a size of " + receivePacketOffer.getLength() + "\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
		Utility.printDataBytes(receivePacketOffer.getData());
		
		DHCPMessage message = new DHCPMessage(receivePacketOffer.getData());
		System.out.println(message);
		
		DHCPRequest(receivePacketOffer);
		//listen for DHCPAck OR DHCPNak
		//if DHCPNak return method
		DatagramPacket receivePacketAck = new DatagramPacket(buffer,length);
		try 
		{
			clientSocket.receive(receivePacketAck);
			System.out.print("Client receives packet with a size of " + receivePacketAck.getLength() + "\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		Utility.printDataBytes(receivePacketAck.getData());
		
		DHCPMessage message2 = new DHCPMessage(receivePacketAck.getData());
		System.out.println(message2);
		
		DHCPRelease();
		//Later on DHCPRelease
		clientSocket.close();
	
		
	}	
}
