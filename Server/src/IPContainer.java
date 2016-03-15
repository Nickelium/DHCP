import java.net.InetAddress;

public class IPContainer 
{
	public byte[] IPAddress;
	
	//MAC Address of the client who lease this ip.
	public byte[] reserver;
	
	public double leaseDuration;
	
	public IPContainer(byte[] newIPAddress, byte[] newReserver, double newLeaseDuration)
	{
		IPAddress = newIPAddress;
		reserver = newReserver;
		leaseDuration = newLeaseDuration;
	}
	
	public boolean isReserved()
	{
		return reserver != null;
	}
	
}
