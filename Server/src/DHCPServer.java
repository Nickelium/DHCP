import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.Timer;

public class DHCPServer 
{
	private IPStorage pool;
	private DatagramSocket serverSocket;
	public final int portServer = 1234;
	public final int MAXLEASEDURATION = 3600;
	
	public DHCPServer()
	{
		System.out.print("Initialization of the server started\n");
		
		try 
		{
			serverSocket = new DatagramSocket(portServer);
			pool = new IPStorage();
			System.out.println("Initialization of the server completed\n");
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
		
		//Reserve new IP address
		int leaseDuration = Utility.toInt(message.getOptionData((byte)51));//(int)(Math.random()*300);
		message.yourIP = pool.reserveAddress(message.clientHardWareAddress, leaseDuration);
		
		// Server IP set
		message.serverIP = InetAddress.getByName("localhost").getAddress();
		
		// reset options fields
		message.resetoptions();
		
		// Set option 53 to value 2
		byte[] i = {DHCPMessage.DHCPOFFER};
		message.addOption((byte)53, (byte)1, i);
		
		message.addOption((byte)54, (byte)4, message.serverIP);
		//TODO ADD LEASE TIME AS OPTION
		
		// Set option 255
		int[] j = {0};
		message.addOption((byte) 255, (byte)0, Utility.toBytes(j));

		InetAddress IPClient = receivePacket.getAddress();
		int portClient = receivePacket.getPort();
		
		byte[] sendingBytes = message.retrieveBytes();
		DatagramPacket response = new DatagramPacket(sendingBytes, sendingBytes.length, IPClient, portClient);
		try 
		{
			serverSocket.send(response);
			System.out.println("DHCPOffer sent\n");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public boolean canAcceptRequest(DatagramPacket receivePacket)
	{
		try
		{
			DHCPMessage message = new DHCPMessage(receivePacket.getData());
			return Utility.toInt(message.getOptionData((byte)51)) < MAXLEASEDURATION  && Arrays.equals(InetAddress.getByName("localhost").getAddress(), message.serverIP) ;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void DHCPAck(DatagramPacket receivePacket) throws UnknownHostException
	{
		try
		{
			System.out.print("Building DHCPAck\n");
			DHCPMessage message = new DHCPMessage(receivePacket.getData());
			
			pool.allocateAddress(message.clientHardWareAddress);
			
			message.opCode = DHCPMessage.BOOTREPLY;
			//Other fields to fill in
			
			
			// TODO: IP geven
			//IP yourIP = pool.reserveAddress(message.clientHardWareAddress);
			byte[] data = message.getOptionData((byte)50);
			message.yourIP = data;
			
		
			// Server IP set
			message.serverIP = InetAddress.getByName("localhost").getAddress();
			
			// reset options fields
			message.resetoptions();
			
			// Set option 53 to value 5
			byte[] i = {DHCPMessage.DHCPACK};
			message.addOption((byte)53, (byte)1, i);
			
			message.addOption((byte)54, (byte)4,message.serverIP);
			//TODO ADD LEASE TIME AS OPTION
			
			// Set option 255
			int[] j = {0};
			message.addOption((byte) 255, (byte)0, Utility.toBytes(j));
	
			InetAddress IPClient = receivePacket.getAddress();
			int portClient = receivePacket.getPort();
			
			byte[] sendingBytes = message.retrieveBytes();
			DatagramPacket response = new DatagramPacket(sendingBytes, sendingBytes.length, IPClient, portClient);
			try 
			{
				serverSocket.send(response);
				System.out.println("DHCPAck sent\n");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void DHCPNak(DatagramPacket receivePacket) throws UnknownHostException
	{
		try
		{
			System.out.print("Building DHCPNak\n");
			DHCPMessage message = new DHCPMessage(receivePacket.getData());
			
			pool.release(message.clientHardWareAddress);
			message.opCode = DHCPMessage.BOOTREPLY;
			//Other fields to fill in
			
			
			// TODO: IP geven
			//IP yourIP = pool.reserveAddress(message.clientHardWareAddress);
			byte[] data = message.getOptionData((byte)50);
			message.yourIP = data;
			
		
			// Server IP set
			message.serverIP = InetAddress.getByName("localhost").getAddress();
			
			// reset options fields
			message.resetoptions();
			
			// Set option 53 to value 5
			byte[] i = {DHCPMessage.DHCPNAK};
			message.addOption((byte)53, (byte)1, i);
			
			message.addOption((byte)54, (byte)4,message.serverIP);
			//TODO ADD LEASE TIME AS OPTION
			
			// Set option 255
			int[] j = {0};
			message.addOption((byte) 255, (byte)0, Utility.toBytes(j));
	
			InetAddress IPClient = receivePacket.getAddress();
			int portClient = receivePacket.getPort();
			
			byte[] sendingBytes = message.retrieveBytes();
			DatagramPacket response = new DatagramPacket(sendingBytes, sendingBytes.length, IPClient, portClient);
			try 
			{
				serverSocket.send(response);
				System.out.println("DHCPNak sent\n");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void handleDHCPRelease(DatagramPacket receivePacket)
	{
		DHCPMessage message = new DHCPMessage(receivePacket.getData());
		pool.release(message.clientHardWareAddress);
	}
	
	
	public void run()
	{
		ActionListener actionListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pool.update();
				
			}
		};
		Timer timer = new Timer(1000,actionListener);
		timer.start();
		while(true)
		{
			pool.printContent();
			
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
			try
			{
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
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		}
	}
	
	
}
