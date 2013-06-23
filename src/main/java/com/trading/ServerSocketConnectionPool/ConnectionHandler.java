package com.trading.ServerSocketConnectionPool;

import java.net.Socket;

public interface ConnectionHandler {

	public void serverProcess(Socket socket);
	
	
}
