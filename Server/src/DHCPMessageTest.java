import static org.junit.Assert.*;

import org.junit.Test;


public class DHCPMessageTest {
	
	@Test
	public void DHCPMessageWithoutOptions() {
		int[] intlist = {1,18,6,0,1,2,3,4,8,8,0,0,11,12,13,14,15,16,17,18,9,9,9,9,2,2,2,2,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8};
		byte[] message = toBytes(intlist);
		DHCPMessage tester = new DHCPMessage(message);
		assertTrue((byte)1 == tester.opCode);
		assertTrue((byte)18 == tester.hardWareType);
		assertTrue((byte)6 == tester.hardWareAddressLength);
		assertTrue((byte)0 == tester.hopCount);
		
		int[] inttrans = {1,2,3,4};
		byte[] trans = toBytes(inttrans);
		assertTrue(testequal(trans,tester.transactionID));
		
		int[] intsecs = {8,8};
		byte[] secs = toBytes(intsecs);
		assertTrue(testequal(secs, tester.secs));
		
		int[] intflags = {0,0};
		byte[] flags = toBytes(intflags);
		assertTrue(testequal(flags, tester.flags));
		
		int [] intclientip = {11,12,13,14};
		byte[] clientip = toBytes(intclientip);
		assertTrue(testequal(clientip, tester.clientIP));
		
		int [] intyourip = {15,16,17,18};
		byte[] yourip = toBytes(intyourip);
		assertTrue(testequal(yourip, tester.yourIP));
		
		int [] intserverip = {9,9,9,9};
		byte[] serverip = toBytes(intserverip);
		assertTrue(testequal(serverip, tester.serverIP));
		
		int [] intgatewayip = {2,2,2,2};
		byte[] gatewayip = toBytes(intgatewayip);
		assertTrue(testequal(gatewayip, tester.gateWayIP));
		
		int [] intclienthw = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		byte[] clienthw = toBytes(intclienthw);
		assertTrue(testequal(clienthw, tester.clientHardWareAddress));
		
		int [] intservername = {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3};
		byte[] servername = toBytes(intservername);
		assertTrue(testequal(servername, tester.serverHostName));
		
		int [] intfilename = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8};
		byte[] filename = toBytes(intfilename);
		assertTrue(testequal(filename, tester.bootFileName));
	}
	
	@Test
	public void DHCPMessageWithOneOptions() {
		int[] intlist = {1,18,6,0,1,2,3,4,8,8,0,0,11,12,13,14,15,16,17,18,9,9,9,9,2,2,2,2,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,
						53,1,1,255,0,0};
		byte[] message = toBytes(intlist);
		DHCPMessage tester = new DHCPMessage(message);
		assertTrue((byte)1 == tester.opCode);
		assertTrue((byte)18 == tester.hardWareType);
		assertTrue((byte)6 == tester.hardWareAddressLength);
		assertTrue((byte)0 == tester.hopCount);
		
		int[] inttrans = {1,2,3,4};
		byte[] trans = toBytes(inttrans);
		assertTrue(testequal(trans,tester.transactionID));
		
		int[] intsecs = {8,8};
		byte[] secs = toBytes(intsecs);
		assertTrue(testequal(secs, tester.secs));
		
		int[] intflags = {0,0};
		byte[] flags = toBytes(intflags);
		assertTrue(testequal(flags, tester.flags));
		
		int [] intclientip = {11,12,13,14};
		byte[] clientip = toBytes(intclientip);
		assertTrue(testequal(clientip, tester.clientIP));
		
		int [] intyourip = {15,16,17,18};
		byte[] yourip = toBytes(intyourip);
		assertTrue(testequal(yourip, tester.yourIP));
		
		int [] intserverip = {9,9,9,9};
		byte[] serverip = toBytes(intserverip);
		assertTrue(testequal(serverip, tester.serverIP));
		
		int [] intgatewayip = {2,2,2,2};
		byte[] gatewayip = toBytes(intgatewayip);
		assertTrue(testequal(gatewayip, tester.gateWayIP));
		
		int [] intclienthw = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		byte[] clienthw = toBytes(intclienthw);
		assertTrue(testequal(clienthw, tester.clientHardWareAddress));
		
		int [] intservername = {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3};
		byte[] servername = toBytes(intservername);
		assertTrue(testequal(servername, tester.serverHostName));
		
		int [] intfilename = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8};
		byte[] filename = toBytes(intfilename);
		assertTrue(testequal(filename, tester.bootFileName));
		
		assertTrue(tester.options.size() == 1);
		assertTrue(tester.options.get(0).getCode() == (byte)53);
		assertTrue(tester.options.get(0).getLength() == (byte)1);
		int[] intdata = {1};
		byte[] data = toBytes(intdata);
		assertTrue(testequal(tester.options.get(0).getData(),data));
	}
	
	
	private byte[] toBytes(int[] list){
		int k = list.length;
		byte[] out = new byte[k];
		for(int i=0; i<list.length; i++){
			out[i] = (byte)list[i];
		}
		return out;
	}
	
	private boolean testequal(byte[] l1, byte[] l2){
		for(int i=0; i<l1.length; i++){
			if(l1[i] != l2[i])
				return false;
		}
		return true;
	}
}
