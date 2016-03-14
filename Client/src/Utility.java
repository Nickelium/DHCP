
public class Utility
{
	
	public static byte[] toBytes(int[] list)
	{
		int k = list.length;
		byte[] out = new byte[k];
		for(int i=0; i<list.length; i++)
			out[i] = (byte)list[i];
		
		return out;
	}
	
	public static int unsignedByte(byte b)
	{
		return (int)b & 0xFF;
	}
	
	public static void printData(byte[] data)
	{
		for(byte b : data)
			System.out.print(unsignedByte(b) +" ");
		System.out.print("\n");
	}

}
