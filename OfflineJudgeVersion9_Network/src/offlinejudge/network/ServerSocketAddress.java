package offlinejudge.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ServerSocketAddress {
	
	private static InetSocketAddress serverSocketAddress;
	private static InetAddress inetAddress;
	private static int port;
	
	public static void setServerSocketAddress(InetSocketAddress serverSocketAddress) {
		ServerSocketAddress.serverSocketAddress = serverSocketAddress;
		ServerSocketAddress.inetAddress = ServerSocketAddress.serverSocketAddress.getAddress();
		ServerSocketAddress.port = ServerSocketAddress.serverSocketAddress.getPort();
	}
	
	public static InetSocketAddress getServerSocketAddress() {
		return serverSocketAddress;
	}
	
	public static InetAddress getInetAddress() {
		return inetAddress;
	}
	
	public static int getPort() {
		return port;
	}
}
