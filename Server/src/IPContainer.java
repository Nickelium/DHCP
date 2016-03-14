import java.net.InetAddress;

public class IPContainer 
{
	public InetAddress IPAddress;
	
	//MAC Address of the client who lease this ip.
	public String reserver;
	
	public double leaseDuration;
	
	public IPContainer(InetAddress newIPAddress, String newReserver, double newLeaseDuration)
	{
		IPAddress = newIPAddress;
		reserver = newReserver;
		leaseDuration = newLeaseDuration;
	}
	
	public boolean isReserved()
	{
		return reserver == null;
	}
	
}
