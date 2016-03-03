

import java.net.*;

public class Server extends Thread
{
	private DatagramSocket serverSocket;
	final static int PORT = 449;
	private String message = "Hello from server!";
	
	public static void main (String[] args) throws SocketException
	{
		Server server = new Server();
		server.run();
	}
	
	public Server() throws SocketException
	{
		serverSocket = new DatagramSocket(PORT);
		System.out.println("Server launched");
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
			
					byte[] BUFFER = new byte[256];
					//receive request
					DatagramPacket receivePacket = new DatagramPacket(BUFFER,BUFFER.length);
					
					System.out.println("Waiting for receiving packet");
					
					serverSocket.receive(receivePacket);
					
					String receiveMessage = new String(receivePacket.getData());
					System.out.println(receiveMessage);
					BUFFER = message.getBytes();
					
					//send response to client
					InetAddress IP_ADDRESS = receivePacket.getAddress();
					int PORT = receivePacket.getPort();
					DatagramPacket PACK = new DatagramPacket(BUFFER,BUFFER.length,IP_ADDRESS, PORT);
					serverSocket.send(PACK);
					System.out.println("Packet sent");
		
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}


}
