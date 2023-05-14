package main;
import com.sun.net.httpserver.*; //server create()
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.*; //ThreadPoolExecutor

/*
 * basic HTTP server with specified port and hostname
 */
public class MyServer {
	
	//initialize server port and hostname
	private static final int SERVER_PORT = 8100;
	private static final String SERVER_HOSTNAME = "localhost";
	public static Map<String,String> allData;
	
	public static void main(String[] args) throws IOException {
		//create a thread pool to handle requests
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
		
		//create map to store data in RequestHandler
		allData = new LinkedHashMap<>();
		
		//create a server
		HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),0);
		
		//set and start server
		server.createContext("/", new RequestHandler(allData));
		
		server.setExecutor(threadPoolExecutor);
		server.start();
		
		System.out.println("Server started on port " + SERVER_PORT);
	}
}
