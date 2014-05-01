package de.tavendo.autobahn;

import de.tavendo.autobahn.secure.WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification;

public class WampConnectionHandler implements Wamp.ConnectionHandler {

	public void onOpen() {
	}

   @Override
   public void onClose(WebSocketCloseNotification code, String reason) {
   }

}
