import java.util.ArrayList;

public class DHCPMessage 
{
	//For options only do the must, look up in the RFC
	/*
	 	DHCPMessageType in option 
	 	
	    DHCPDISCOVER = 1,   //a client broadcasts to locate servers

	    DHCPOFFER = 2,     //a server offers an IP address to the device

	    DHCPREQUEST = 3,   //client accepts offers from DHCP server

	    DHCPDECLINE = 4,   //client declines the offer from this DHCP server

	    DHCPACK = 5,       //server to client + committed IP address

	    DHCPNAK = 6,       //server to client to state net address incorrect

	    DHCPRELEASE = 7,   //graceful shutdown from client to Server

	    DHCPINFORM = 8     //client to server asking for local info

	*/
	
	public byte opCode; 
						/*general type message:
	 						1 = request,
	 						2 = reply
						*/
    public byte hardWareType;
    					/*Hardware Type: 
    							1 = 10MB ethernet
    							6 = IEE802 Network
    							7 = ARCNET
    							11 = Localtalk
    							12 = Localnet
    							14 = SMDS
    							15 = Frame relay
    							16 = Asynchronous Transfer mode
    							17 = HDLC
    							18 = Fibre channel
    							19 = Asynchronous Transfer mode
    							20 = Serial Line
    							*/
    public byte hardWareAddressLength; 
    					/*hardware address length: length of MACID
    					 * bv. PC_ASUS = 6
    					 */
    public byte hopCount; //Hw options
    
    public byte[] secs = new byte[2]; //elapsed time from trying to boot (3)
    public byte[] flags = new byte[2]; //flags (3)
    
    public byte[] transactionID = new byte[4]; 
    					/*transaction id (5)
    					 * to recognize the message request <-> reply 
    					 */
    public byte[] clientIP = new byte[4]; 
    					/* client IP (5)
    					 * 0 when no ip or unvalid
    					 */
    public byte[] yourIP = new byte[4];
    					/* your client IP (5)
    					 * The IP address that the server is assigning to the client.
    					 */
    public byte[] serverIP = new byte[4]; 
    					/* Server IP (5)
    					 * 
    					 */
    public byte[] gateWayIP = new byte[4]; // relay agent IP (5)
    
    public byte[] clientHardWareAddress = new byte[16]; // Client HW address (16)
    
    public byte[] serverHostName = new byte[64]; // Optional server host name (64)
    
    public byte[] bootFileName = new byte[128]; // Boot file name (128)
    
    public void addOption(byte Code, byte Length, byte[] Data){
    	DHCPoption newoption = new DHCPoption(Code, Length, Data);
    	options.add(newoption);
    }
    public ArrayList<DHCPoption> options = new ArrayList<>(); //options (rest)
    
    public final int minimalLength = 236; //bytes
    
    
    
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
    	
    	for(int i = 0; i < secs.length; i++)
    		secs[i] = buffer[i + j];
    	j += secs.length;
    	
    	for(int i = 0; i < flags.length; i++)
    		flags[i] = buffer[i+j];
    	j += flags.length;
    	
    	for(int i = 0; i < transactionID.length; i++)
    		transactionID[i] = buffer[i+j];
    	j += transactionID.length;
    	
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
    		clientHardWareAddress[i + j] = buffer[i+j];
    	j += clientHardWareAddress.length;
    	
    	for(int i = 0; i < serverHostName.length; i++)
    		serverHostName[i] = buffer[i+j];
    	j += serverHostName.length;
    	
    	for(int i = 0; i < bootFileName.length; i++)
    		bootFileName[i] = buffer[i+j];
    	j += bootFileName.length;
    	
    	int k = buffer.length - j;
    	byte[] bufferedoptions = new byte[k];
    	for(int i = 0; i < k; i++)
    		bufferedoptions[i] = buffer[j+i];
    	createOptions(bufferedoptions); 	
    }
    
    private void createOptions(byte[] Buffer){
    	if(Buffer.length == 0)
    		return;
    	byte option = Buffer[0];
    	byte length = Buffer[1];
    	int k = 2+length;
    	byte[] data = new byte[k];
    	for(int i=0; i<k+1; i++)
    		data[i] = Buffer[i+2];
    	addOption(option, length, data);
    	int l = Buffer.length - k;
    	byte[] topass = new byte[l];
    	for(int i=0; i<l+1; i++)
    		topass[i] = Buffer[k+i];
    	createOptions(topass);
    }
    
    public byte[] retrieveBytes()
    {
    	byte[] toReturn = new byte[getLength()];
    	
    	toReturn[0] = opCode;
    	toReturn[1] = hardWareType;
    	toReturn[2] = hardWareAddressLength;;
    	toReturn[3] = hopCount;
    	
    	int j = 4;
    	
    	for(int i = 0; i < secs.length; i++)
    		toReturn[i + j] = secs[i];
    	j += secs.length;
    	
    	for(int i = 0; i < flags.length; i++)
    		toReturn[i + j] = flags[i];
    	j += flags.length;
    	
    	for(int i = 0; i < transactionID.length; i++)
    		toReturn[i + j] = transactionID[i];
    	j += transactionID.length;
    	
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
    		toReturn[i + j] = serverHostName[i];
    	j += bootFileName.length;
    	
    	for(int i = 0; i < options.size(); i++){
    		System.arraycopy(options.get(i), 0, toReturn, j, options.get(i).getLength());
    		j += options.get(i).getLength();
    	}
    	
    	return toReturn;
    		
    }
    
    public int getLength()
    {
    	return minimalLength + getOptionsLength();
    }

	private int getOptionsLength() {
		int length = 0;
		for(int i = 0; i < options.size(); i++){
    		length += options.get(i).getLength();
    	}
		return length;
	}
    

}
