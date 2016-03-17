import java.net.InetAddress;

public class IPContainer 
{
	public byte[] IPAddress;
	
	//MAC Address of the client who lease this ip.
	private enum STATE {FREE, RESERVED, ALLOCATED};
	
	private STATE stateValue;
	
	public byte[] reserver;
	
	public int leaseDuration;
	
	public IPContainer(byte[] newIPAddress)
	{
		IPAddress = newIPAddress;
		reserver = null;
		leaseDuration = -1;
		stateValue = STATE.FREE;
	}
	
	public boolean isReserved()
	{
		return stateValue == STATE.RESERVED;
	}
	
	public void reserve(byte[] newReserver, int newLeaseDuration)
	{
		reserver = newReserver;
		leaseDuration = newLeaseDuration;
		stateValue = STATE.RESERVED;
	}
	
	public void allocate()
	{
		stateValue = STATE.ALLOCATED;
	}
	
	public void release()
	{
		reserver = null;
		leaseDuration = -1;
		stateValue = STATE.FREE;
	}

	public boolean isAllocated() {
		
		return stateValue == STATE.ALLOCATED;
	}

	public void update()
	{
		leaseDuration -=1;
	}
	
}
