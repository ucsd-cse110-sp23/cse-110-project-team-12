/*
 * MyServer class runs a local server to link our app with data storage capabilities. 
 */

import com.sun.net.httpserver.*; //server create()
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*; //ThreadPoolExecutor

import javax.swing.JOptionPane;

/*
 * basic HTTP server
 */
public class MyServer {
	
	//initialize server port and hostname
	private static final int SERVER_PORT = 8100;
	private static final String SERVER_HOSTNAME = "localhost";
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final String  URL = "http://localhost:8100/";
	private static InetSocketAddress socket;
	
	public static Map<String,String> allData;
	
	public static boolean checkServerAvailability() {

		
		try (Socket s = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Server Connection Unavailable");
			System.exit(1);
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException {
		//create a thread pool to handle requests
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
		
		socket = new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT);
		
		//create a server
		HttpServer server = HttpServer.create(socket,0);
		
		//create map to store data in RequestHandler
		allData = new LinkedHashMap<String,String>();
		
		//set and start server
		server.createContext("/", new RequestHandler(allData));
		
		server.setExecutor(threadPoolExecutor);
		server.start();
		
		System.out.println("Server started on port " + SERVER_PORT);
	}
}
