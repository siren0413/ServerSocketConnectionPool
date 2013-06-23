package com.trading.ServerSocketConnectionPool;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class PoolConnectionHandler implements Runnable{

	protected Socket connection;
	private static List<Socket> pool = new LinkedList<Socket>();
	private ConnectionHandler handler = null;

	
	
	public PoolConnectionHandler(ConnectionHandler handler) {
		super();
		this.handler = handler;
	}

	public static void processRequest(Socket socket) {
		synchronized (pool) {
			pool.add(socket);
			pool.notifyAll();
		}
	}

	public void run() {
		while (true) {
			synchronized (pool) {
				while (pool.isEmpty()) {
					try {
						pool.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				connection = pool.remove(0);
			}
			handler.serverProcess(connection);
		}

	}

}