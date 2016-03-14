import java.net.InetAddress;
import java.util.ArrayList;

public class IPStorage 
{
	public ArrayList<IPContainer> listIPContainer;
	//IP4 == 4 byte
	private String IP;
	private int IPRange;
	
	public IPStorage()
	{
		listIPContainer = new ArrayList<>();
		init();
	}
	
	private void init()
	{
		String IPIncrementing = IP;
		try
		{
			for(int i = 0; i < IPRange; i++)
			{
				IPContainer ipcontainer = new IPContainer(InetAddress.getByName(IPIncrementing),null,0);
				IPIncrementing = incrementIPAddress(IPIncrementing);
				listIPContainer.add(ipcontainer);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Assume only in the range of zero to 255
	//Does not support out of this range
	private String incrementIPAddress(String IPAddress)
	{
		String[] parts = IPAddress.split(".");
		if(parts.length != 4) throw new IllegalArgumentException("Invalid ip4 format");
		
		return parts[0] + parts[1] + parts[2] + (Integer.parseInt(parts[3]) + 1);
	}
	
	private boolean isReserved(InetAddress ip)
	{
		for(IPContainer ipcontainer : listIPContainer)
			if(ip.equals(ipcontainer.IPAddress) && ipcontainer.isReserved())
				return true;
		return false;
	}
	
	public InetAddress lookUp(String macaddress)
	{
		for(IPContainer ipcontainer : listIPContainer)
			if(ipcontainer.reserver.equals(macaddress)) return ipcontainer.IPAddress;
		return null;
	}
	
	public InetAddress reserveAddress(String macaddress)
	{
		if(lookUp(macaddress) != null) return null;
		for(IPContainer ipcontainer : listIPContainer)
			if(!ipcontainer.isReserved())
			{
				ipcontainer.reserver = macaddress;
				ipcontainer.leaseDuration = Math.random()*100;
				return ipcontainer.IPAddress;
			}
		return null;
	}
}
