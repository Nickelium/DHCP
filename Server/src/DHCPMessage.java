import java.util.ArrayList;

public class DHCPMessage 
{
	public final static byte BOOTREQUEST = 1;
	public final static byte BOOTREPLY = 2;
	/**
	 * General type message
	 * 1=request | 2= reply
	 * No other values possible
	 */
	public byte opCode; 
	
	
	public final static byte ETHERNET = 1;
	public final static byte IEEE802 = 6;
	/**
	 * Hardware type
	 * 1 = 10MB ethernet
     * 6 = IEE802 Network
     * 7 = ARCNET
   	 * 11 = Localtalk
     * 12 = Localnet
     * 14 = SMDS
     * 15 = Frame relay
     * 16 = Asynchronous Transfer mode
     * 17 = HDLC
     * 18 = Fibre channel
     * 19 = Asynchronous Transfer mode
     * 20 = Serial Line
	 */
    public byte hardWareType;

    /**
     * Hardware address length: length of the MAC-ID
     * ex. PC_ASUS = 6
     */
    public byte hardWareAddressLength; 
    
    /**
     * Hw options
     */
    public byte hopCount;
    
    /**
     * Transaction-ID (5)
     * To recognize the message request VS reply
     */
    public byte[] transactionID = new byte[4]; 
    
    /**
     * elapsed time from trying to boot (3)
     */
    public byte[] secs = new byte[2];
    
    /**
     * flags (3)
     */
    public byte[] flags = new byte[2];
    
    /**
     * Client IP (5)
     * 0 when no IP or invalid
     */
    public byte[] clientIP = new byte[4]; 
    
    /**
     * Your client IP (5)
     * The IP address that the server is assigning to the client
     * */
    public byte[] yourIP = new byte[4];
    
    /**
     * Server IP (5)
     */
    public byte[] serverIP = new byte[4]; 
    
    /**
     * relay agent IP (5)
     */
    public byte[] gateWayIP = new byte[4];
    
    /**
     * Client HardWare address (16)
     */
    public byte[] clientHardWareAddress = new byte[16];
    
    /**
     * Optional server host name (64)
     */
    public byte[] serverHostName = new byte[64];
    
    /**
     * Boot file name (128)
     */
    public byte[] bootFileName = new byte[128];
    
    /**
     * Add option to the HDCP message
     * @param Code		The Code of the option
     * @param Length	The length corresponding with the option
     * @param Data		The actual value of the code
     */
    public void addOption(byte Code, byte Length, byte[] Data){
    	DHCPoption newoption = new DHCPoption(Code, Length, Data);
    	options.add(newoption);
    }
    public ArrayList<DHCPoption> options = new ArrayList<>(); //options (rest)
    
    public final static int MINLENGTH = 236; //bytes
    
    public final static int MAXLENGTH = 576;
    
    
    public DHCPMessage()
    {
    	//put parameters
    }
    
    public DHCPMessage(byte[] buffer)
    {
    	//if(buffer.length < minimalLength) throw new Exception();
    	opCode = buffer[0];
    	hardWareType = buffer[1];
    	hardWareAddressLength = buffer[2];
    	hopCount = buffer[3];
    	
    	int j = 4;
    	
    	for(int i = 0; i < transactionID.length; i++)
    		transactionID[i] = buffer[i+j];
    	j += transactionID.length;
    	
    	for(int i = 0; i < secs.length; i++)
    		secs[i] = buffer[i + j];
    	j += secs.length;
    	
    	for(int i = 0; i < flags.length; i++)
    		flags[i] = buffer[i+j];
    	j += flags.length;
    	
    	for(int i = 0; i < clientIP.length; i++)
    		clientIP[i] = buffer[i+j];
    	j += clientIP.length;
    	
    	for(int i = 0; i < yourIP.length; i++)
    		yourIP[i] = buffer[i+j];
    	j += yourIP.length;
    	
    	for(int i = 0; i < serverIP.length; i++)
    		serverIP[i] = buffer[i+j];
    	j += serverIP.length;
    	
    	for(int i = 0; i < gateWayIP.length; i++)
    		gateWayIP[i] = buffer[i+j];
    	j += gateWayIP.length;
    	
    	for(int i = 0; i < clientHardWareAddress.length; i++)
    		clientHardWareAddress[i] = buffer[i+j];
    	j += clientHardWareAddress.length;
    	
    	for(int i = 0; i < serverHostName.length; i++)
    		serverHostName[i] = buffer[i+j];
    	j += serverHostName.length;
    	
    	for(int i = 0; i < bootFileName.length; i++)
    		bootFileName[i] = buffer[i+j];
    	j += bootFileName.length;
    	
    	if(j < buffer.length-1)
    	{
	    	int k = buffer.length - j;
	    	byte[] bufferedoptions = new byte[k];
	    	for(int i = 0; i < k; i++)
	    		bufferedoptions[i] = buffer[j+i];
	    	createOptions(bufferedoptions); 
    	}
    }
    
	//For options only do the must, look up in the RFC
	/*
	 	DHCPMessageType in option 
	    DHCPDISCOVER = 1,  //a client broadcasts to locate servers
	    DHCPOFFER = 2,     //a server offers an IP address to the device
	    DHCPREQUEST = 3,   //client accepts offers from DHCP server
	    DHCPDECLINE = 4,   //client declines the offer from this DHCP server
	    DHCPACK = 5,       //server to client + committed IP address
	    DHCPNAK = 6,       //server to client to state net address incorrect
	    DHCPRELEASE = 7,   //graceful shutdown from client to Server
	    DHCPINFORM = 8     //client to server asking for local info

	*/
    
    public final static byte DHCPDISCOVER = 1;
    public final static byte DHCPOFFER = 2;
    public final static byte DHCPREQUEST = 3;
    public final static byte DHCPDECLINE = 4;
    public final static byte DHCPACK = 5;
    public final static byte DHCPNAK = 6;
    public final static byte DHCPRELEASE = 7;
    public final static byte DHCPINFORM = 8;
    
    public byte getType()
    {
    	for(DHCPoption opt : options)
    		if(opt.getCode() == 53 && opt.getLength() == 1) return opt.getData()[0];
    	return 0;
    }
    
    private void createOptions(byte[] Buffer){
    	//1 byte geeft error bij checken buffer.length ==0, want outofbound
    	//2 bytes minimaal aantal bytes :: bv. 2(=code) 0(lengte)
    	if(Buffer.length < 2 || Buffer[0] == (byte)255)
    		return;
    	byte option = Buffer[0];
    	byte length = Buffer[1];
    	int k = length+2;
    	byte[] data = new byte[length];
    	for(int i=0; i<length; i++)
    		data[i] = Buffer[i+2];
    	addOption(option, length, data);
    	int l = Buffer.length - k;
    	byte[] topass = new byte[l];
    	for(int i=0; i<l; i++)
    		topass[i] = Buffer[k+i];
    	createOptions(topass);
    }
    
    public byte[] retrieveBytes()
    {
    	byte[] toReturn = new byte[getLength() + 1];
    	
    	toReturn[0] = opCode;
    	toReturn[1] = hardWareType;
    	toReturn[2] = hardWareAddressLength;;
    	toReturn[3] = hopCount;
    	
    	int j = 4;
    	
    	for(int i = 0; i < transactionID.length; i++)
    		toReturn[i + j] = transactionID[i];
    	j += transactionID.length;
    	
    	for(int i = 0; i < secs.length; i++)
    		toReturn[i + j] = secs[i];
    	j += secs.length;
    	
    	for(int i = 0; i < flags.length; i++)
    		toReturn[i + j] = flags[i];
    	j += flags.length;
    	
    	for(int i = 0; i < clientIP.length; i++)
    		toReturn[i + j] = clientIP[i];
    	j += clientIP.length;
    	
    	for(int i = 0; i < yourIP.length; i++)
    		toReturn[i + j] = yourIP[i];
    	j += yourIP.length;
    	
    	for(int i = 0; i < serverIP.length; i++)
    		toReturn[i + j] = serverIP[i];
    	j += serverIP.length;
    	
    	for(int i = 0; i < gateWayIP.length; i++)
    		toReturn[i + j] = gateWayIP[i];
    	j += gateWayIP.length;
    	
    	for(int i = 0; i < clientHardWareAddress.length; i++)
    		toReturn[i + j] = clientHardWareAddress[i];
    	j += clientHardWareAddress.length;
    	
    	for(int i = 0; i < serverHostName.length; i++)
    		toReturn[i + j] = serverHostName[i];
    	j += serverHostName.length;
    	
    	for(int i = 0; i < bootFileName.length; i++)
    		toReturn[i + j] = bootFileName[i];
    	j += bootFileName.length;
    	
    	for(int i = 0; i < options.size(); i++){
    		System.arraycopy(options.get(i).getBytes(), 0, toReturn, j, options.get(i).getTotalLength());
    		j += options.get(i).getTotalLength();
    	}

    	toReturn[getLength()] = (byte) 255;
    	return toReturn;
    		
    }
    
    public int getLength()
    {
    	return MINLENGTH + getOptionsLength();
    }
    

	private int getOptionsLength() {
		int length = 0;
		for(int i = 0; i < options.size(); i++){
    		length += options.get(i).getTotalLength();
    	}
		return length;
	}
    

}
