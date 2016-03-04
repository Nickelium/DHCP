
public class DHCPoption {
	
	private byte Code;
	private byte Length;
	private byte[] Data = new byte[Length];
	
	DHCPoption(byte Code, byte Length, byte[] Data){
		this.Code = Code;
		this.Length = Length;
		this.Data = Data;
	}
	
	public byte getLength(){
		return (byte) (this.Length + 2);
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
}
