import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DHCPServer 
{
	private IPStorage pool;
	private DatagramSocket serverSocket;
	public final int portServer = 1234;
	
	public DHCPServer()
	{
		System.out.print("Initialization of the server started\n");
		
		try 
		{
			serverSocket = new DatagramSocket(portServer);
			pool = new IPStorage();
			System.out.print("Initialization of the server completed\n");
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPOffer(DatagramPacket receivePacket) throws UnknownHostException
	{
		System.out.print("Building DHCPOffer\n");
		DHCPMessage message = new DHCPMessage(receivePacket.getData());
		
		message.opCode = DHCPMessage.BOOTREPLY;
		//Other fields to fill in
		byte[] buffer = message.retrieveBytes();
		int length = buffer.length;
		
		// TODO: IP geven
		//IP yourIP = pool.reserveAddress(message.clientHardWareAddress);
		//message.yourIP(yourIP);
		
		// Server IP set
		message.serverIP = InetAddress.getByName("localhost").getAddress();
		
		// reset options fields
		message.resetoptions();
		
		// Set option 53 to value 2
		int[] i = {2};
		message.addOption((byte)53, (byte)1, Utility.toBytes(i));
		
		// Set option 255
		int[] j = {0};
		message.addOption((byte) 255, (byte)0, Utility.toBytes(j));

		InetAddress IPClient = receivePacket.getAddress();
		int portClient = receivePacket.getPort();
		DatagramPacket response = new DatagramPacket(message.retrieveBytes(), length, IPClient, portClient);
		try 
		{
			serverSocket.send(response);
			System.out.print("DHCPOffer sent\n");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public boolean canAcceptRequest(DatagramPacket receivePacket)
	{
		return true;
	}
	
	public void DHCPAck(DatagramPacket receivePacket)
	{
		
	}
	
	public void DHCPNak(DatagramPacket receivePacket)
	{
		
	}

	public void handleDHCPRelease(DatagramPacket receivePacket)
	{
		
		
	}
	
	
	public void run()
	{
		while(true)
		{
			//listen to incoming packet
			byte[] buffer = new byte[576];
			DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);
			try 
			{
				serverSocket.receive(receivePacket);
				System.out.print("Server receives packet with a size of " + receivePacket.getLength() +"\n");
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		
			Utility.printDataBytes(receivePacket.getData());
			
			//determine type of request
			DHCPMessage response = new DHCPMessage(receivePacket.getData());
			System.out.println(response);
			
			switch (response.getType())
			{
				case DHCPMessage.DHCPDISCOVER:
				try {
					DHCPOffer(receivePacket);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;
				
				case DHCPMessage.DHCPREQUEST:
					
					if(canAcceptRequest(receivePacket))
						DHCPAck(receivePacket);
					else
						DHCPNak(receivePacket);
					break;
				
				case DHCPMessage.DHCPRELEASE:
					handleDHCPRelease(receivePacket);
					break;
			}
		
		}
	}
	
	
}
