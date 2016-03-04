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
    public ArrayList<Byte> options = new ArrayList<>(); //options (rest)
    
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
    	
    	int j = 3;
    	
    	for(int i = 0; i < secs.length; i++, j++)
    		secs[i] = buffer[i + j];
    	for(int i = 0; i < flags.length; i++, j++)
    		flags[i] = buffer[i+j];
    	
    	for(int i = 0; i < transactionID.length; i++, j++)
    		transactionID[i] = buffer[i+j];
    	for(int i = 0; i < clientIP.length; i++, j++)
    		clientIP[i] = buffer[i+j];
    	for(int i = 0; i < yourIP.length; i++, j++)
    		yourIP[i] = buffer[i+j];
    	for(int i = 0; i < serverIP.length; i++, j++)
    		serverIP[i] = buffer[i+j];
    	for(int i = 0; i < gateWayIP.length; i++, j++)
    		gateWayIP[i] = buffer[i+j];
    	
    	for(int i = 0; i < clientHardWareAddress.length; i++, j++)
    		clientHardWareAddress[i + j] = buffer[i+j];
    	for(int i = 0; i < serverHostName.length; i++, j++)
    		serverHostName[i] = buffer[i+j];
    	for(int i = 0; i < bootFileName.length; i++, j++)
    		bootFileName[i] = buffer[i+j];
    	
    	for(int i = 0; i < buffer.length - minimalLength; i++, j++)
    		options.add(buffer[i+j]);
    	
    }
    
    public byte[] retrieveBytes()
    {
    	byte[] toReturn = new byte[getLength()];
    	
    	toReturn[0] = opCode;
    	toReturn[1] = hardWareType;
    	toReturn[2] = hardWareAddressLength;;
    	toReturn[3] = hopCount;
    	
    	int j = 3;
    	
    	for(int i = 0; i < secs.length; i++, j++)
    		toReturn[i + j] = secs[i];
    	for(int i = 0; i < flags.length; i++, j++)
    		toReturn[i + j] = flags[i];
    	
    	for(int i = 0; i < transactionID.length; i++, j++)
    		toReturn[i + j] = transactionID[i];
    	for(int i = 0; i < clientIP.length; i++, j++)
    		toReturn[i + j] = clientIP[i];
    	for(int i = 0; i < yourIP.length; i++, j++)
    		toReturn[i + j] = yourIP[i];
    	for(int i = 0; i < serverIP.length; i++, j++)
    		toReturn[i + j] = serverIP[i];
    	for(int i = 0; i < gateWayIP.length; i++, j++)
    		toReturn[i + j] = gateWayIP[i];
    	
    	for(int i = 0; i < clientHardWareAddress.length; i++, j++)
    		toReturn[i + j] = clientHardWareAddress[i];
    	for(int i = 0; i < serverHostName.length; i++, j++)
    		toReturn[i + j] = serverHostName[i];
    	for(int i = 0; i < bootFileName.length; i++, j++)
    		toReturn[i + j] = serverHostName[i];
    	
    	for(int i = 0; i < options.size(); i++, j++)
    		toReturn[i + j] = options.get(i);
    	
    	return toReturn;
    		
    }
    
    public int getLength()
    {
    	return minimalLength + options.size();
    }
    

}
