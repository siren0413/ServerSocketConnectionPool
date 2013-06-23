package com.trading.ServerSocketConnectionPool;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionPool {
	
	private int maxConnections;
	private int listenPort;
	
	
	public ConnectionPool() {
		super();
	}

	public ConnectionPool(int maxConnections, int listenPort) {
		super();
		this.maxConnections = maxConnections;
		this.listenPort = listenPort;
	}
	
	
	public void acceptConnections(ConnectionHandler serverhandler) {
		setHandlers(serverhandler);
		try {
			ServerSocket serverSocket = new ServerSocket(listenPort);
			Socket socket = null;
			while(true) {
					socket = serverSocket.accept();
					PoolConnectionHandler.processRequest(socket);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setHandlers(ConnectionHandler serverhandler) {
		for(int i = 0 ; i < this.maxConnections; i++) {
			PoolConnectionHandler handler = new PoolConnectionHandler(serverhandler);
			new Thread(handler,"handler$"+i).start();
		}
	}
	
	public static void main(String[] args) {
		ConnectionPool pool = new ConnectionPool(5, 8888);
		
		ConnectionHandler handler = new ConnectionHandler() {
			
			public void serverProcess(Socket socket) {
				System.out.println(Thread.currentThread().getName() + " handler " + socket);
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(socket.getOutputStream());
					writer.write(Thread.currentThread().getName() + " handler me " + socket.getPort());
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer.close();
				}
				
			}
		};
		pool.acceptConnections(handler);
	}
	
	// getter and setter

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}
	
	
	

	

}
