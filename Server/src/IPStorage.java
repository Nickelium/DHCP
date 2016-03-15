import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class IPStorage 
{
	private ArrayList<IPContainer> listIPContainer;
	private byte[] IP  = Utility.toBytes(new int[]{192,168,1,0});
	private int IPRange = 5;
	
	public IPStorage()
	{
		listIPContainer = new ArrayList<>();
		init();
	}
	
	private void init()
	{
		byte[] IPIncrementing = IP;
		try
		{
			for(int i = 0; i < IPRange; i++)
			{
				IPContainer ipcontainer = new IPContainer(IPIncrementing,null,0);
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
	private byte[] incrementIPAddress(byte[] IPAddress)
	{
		if(IPAddress.length != 4) throw new IllegalArgumentException("Invalid ip4 format");
		
		int IPInt = Utility.toInt(IPAddress);
		IPInt++;
		return Utility.toByteArray(IPInt);
	}
	
	private boolean isReserved(InetAddress ip)
	{
		for(IPContainer ipcontainer : listIPContainer)
			if(ip.equals(ipcontainer.IPAddress) && ipcontainer.isReserved())
				return true;
		return false;
	}
	
	public byte[] lookUp(byte[] macaddress)
	{
		for(IPContainer ipcontainer : listIPContainer)
			if(ipcontainer.reserver != null && Arrays.equals(ipcontainer.reserver, macaddress)) return ipcontainer.IPAddress;
		return null;
	}
	
	public byte[] reserveAddress(byte[] macaddress, double leaseDuration)
	{
		if(lookUp(macaddress) != null) return lookUp(macaddress);
		for(IPContainer ipcontainer : listIPContainer)
			if(!ipcontainer.isReserved())
			{
				ipcontainer.reserver = macaddress;
				ipcontainer.leaseDuration = leaseDuration;
				return ipcontainer.IPAddress;
			}
		return null;
	}
	
	public boolean release(byte[] macaddress)
	{
		if(lookUp(macaddress) == null) return false;
		for(IPContainer ipcontainer : listIPContainer)
			if(ipcontainer.reserver != null && Arrays.equals(ipcontainer.reserver, macaddress)) 
			{
				ipcontainer.reserver = null;
				ipcontainer.leaseDuration = 0.0;
				return true;
			}
		return false;
	}
}
