package de.tavendo.autobahn;

import de.tavendo.autobahn.secure.WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification;
import de.tavendo.autobahn.secure.WebSocketMessage;

public class WampConnectionHandler implements Wamp.ConnectionHandler {

	public void onOpen() {
	}

   @Override
   public void onClose(WebSocketCloseNotification code, String reason) {
   }

   public void onCloseMessage(WebSocketMessage.Close close){
   }
}
