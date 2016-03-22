/**
 * In this class a DHCP server will be initialized and started
 * @author Tobias & Tri
 */
public class MainServer {
	public static void main(String[] args)
	{
		DHCPServer server = new DHCPServer();
		server.run();
	}
}
