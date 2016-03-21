package ClientTest;


// TODO: Tri wat doet dit??

import java.net.*;

public class Client 
{
	private DatagramSocket clientSocket;
	final static int PORT = 449;
	
	public Client()
	{
		
	}
	
	public void run()
	{
		try
		{
			String message = "Hello from client!";
			byte[] BUFFER = message.getBytes();
			clientSocket = new DatagramSocket();
			InetAddress IP_ADDRESS = InetAddress.getByName("localhost");
			DatagramPacket sendingPacket = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, PORT);
			clientSocket.send(sendingPacket);
			
			DatagramPacket receivePacket = new DatagramPacket(BUFFER, BUFFER.length);
			clientSocket.receive(receivePacket);
			
			String receiveMessage = new String(sendingPacket.getData());
			System.out.println(receiveMessage);
			clientSocket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args)
	{
		Client client = new Client();
		client.run();
	}
}
