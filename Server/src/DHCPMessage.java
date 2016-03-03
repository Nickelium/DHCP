
public class DHCPMessage 
{
	public byte opCode; //Op code: 1 = bootRequest, 2 = BootReply
    public byte hardWareType; //Hardware Address Type: 1 = 10MB ethernet
    public byte hardWareAdressLength; //hardware address length: length of MACID
    public byte hopCount; //Hw options
    
    public byte[] secs = new byte[2]; //elapsed time from trying to boot (3)
    public byte[] flags = new byte[2]; //flags (3)
    
    public byte[] transactionID = new byte[4]; //transaction id (5), 
    public byte[] clientIP = new byte[4]; // client IP (5)
    public byte[] yourIP = new byte[4]; // your client IP (5)
    public byte[] serverIP = new byte[4]; // Server IP (5)
    public byte[] gateWayIP = new byte[4]; // relay agent IP (5)
    
    public byte[] clientHardWareAddress = new byte[16]; // Client HW address (16)
    public byte[] serverHostName = new byte[64]; // Optional server host name (64)
    public byte[] bootFileName = new byte[128]; // Boot file name (128)
    public byte[] options; //options (rest)
    
    public final int minimalLength = 236; //bytes
    
    public DHCPMessage()
    {
    	//put parameters
    }
    
    public byte[] retrieveBytes()
    {
    	byte[] toReturn = new byte[getLength()];
    	
    	toReturn[0] = opCode;
    	toReturn[1] = hardWareType;
    	toReturn[2] = hardWareAdressLength;;
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
    	
    	for(int i = 0; i < options.length; i++, j++)
    		toReturn[i + j] = options[i];
    	
    	return toReturn;
    		
    }
    
    public int getLength()
    {
    	return minimalLength + options.length;
    }
    

}
