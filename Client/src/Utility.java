import java.nio.ByteBuffer;

/**
 * This Utility class contains divers useful tools and methods to work with bytes, arrays of bytes, ...
 * 
 * @author Tobias & Tri
 */
public class Utility
{
	
	/**
	 * Convert the given list of integers into a list of bytes
	 * @param 	list of integers
	 * @return	equivalent list of bytes
	 */
	public static byte[] toBytes(int[] list)
	{
		int k = list.length;
		byte[] out = new byte[k];
		for(int i=0; i<list.length; i++)
			out[i] = (byte)list[i];
		return out;
	}
	
	/**
	 * Convert an integer to an array of bytes
	 * @param 	value
	 * @return	the corresponding list of bytes
	 */
	public static byte[] toByteArray(int value) 
	{
	     return  ByteBuffer.allocate(4).putInt(value).array();
	}
	
	/**
	 * Convert an array of bytes into an integer
	 * Reverse of toByteArray()
	 * @param 	a list of bytes
	 * @return	the corresponding integer
	 */
	public static int toInt(byte[] bytes)
	{
		int value = 0;
		for (int i = 0; i < bytes.length; i++)
		{
		   value = (value << 8) + (bytes[i] & 0xff);
		}
		return value;
	}
	
	/**
	 * TODO: Tri wat doet dit ?
	 * @param b
	 * @return
	 */
	public static int unsignedByte(byte b)
	{
		return (int)b & 0xFF;
	}
	
	/**
	 * TODO: Tri wat doet dit?
	 * @param Byte
	 * @return
	 */
	public static int[] unsignedBytes(byte[] Byte)
	{
		int[] toReturn = new int [Byte.length];
		for(int i = 0; i < Byte.length; i++)
			toReturn[i] = unsignedByte(Byte[i]);
		return toReturn;
	}
	
	/**
	 * Print out the given array of bytes
	 * @param list of bytes to print
	 */
	public static void printDataBytes(byte[] data)
	{
		for(byte b : data)
			System.out.print(unsignedByte(b) +" ");
		System.out.print("\n");
	}
	

}
