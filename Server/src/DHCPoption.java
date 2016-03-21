import java.util.Arrays;

public class DHCPoption {
	
	private byte Code;
	private byte Length;
	//Byte representation of data
	private byte[] Data = new byte[Length];
	
	
	DHCPoption(byte Code, byte Length, byte[] Data){
		this.Code = Code;
		this.Length = Length;
		this.Data = Data;
	}
	
	public byte getTotalLength(){
		return (byte) (this.Length + 2);
	}
	
	public byte getLength(){
		return this.Length;
	}
	
	public byte getCode(){
		return (byte) this.Code;
	}
	
	public byte[] getData(){
		return this.Data;
	}
	
	public byte[] getBytes(){
		int K = 2 + Length;
		byte[] tosend = new byte[K];
		tosend[0] = Code;
		tosend[1] = Length;
		for(int i=0; i<Length; i++){
			tosend[i+2] = Data[i];
		}
		return tosend;
	}
	
	
	//Data is as a int representation
	@Override
	public String toString()
	{
		return "Option :" + Arrays.toString(new String[]{
			Utility.unsignedByte(Code)+ "",
			Utility.unsignedByte(Length)+ "", 
			Arrays.toString(Utility.unsignedBytes(Data)) 
			});
	}
}
