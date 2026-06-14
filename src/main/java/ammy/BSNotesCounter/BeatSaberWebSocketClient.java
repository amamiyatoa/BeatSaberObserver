package ammy.BSNotesCounter;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class BeatSaberWebSocketClient extends WebSocketClient {
	interface StatusUpdateListener {
		void onStatusUpdated(BeatSaberStatus status);
		void onConnected();
		void onDisconnected();
		void onError(String message);
	}
	
	private final Gson gson = new Gson();
	private final StatusUpdateListener listener;
	
	BeatSaberWebSocketClient(StatusUpdateListener listener) throws URISyntaxException {
		super(new URI("ws://localhost:6557/socket"));
		this.listener = listener;
	}
	
	@Override
	public void onOpen(ServerHandshake handshake) {
		javax.swing.SwingUtilities.invokeLater(listener::onConnected);
	}
	
	@Override
	public void onMessage(String message) {
		BeatSaberStatus status = gson.fromJson(message, BeatSaberStatus.class);
			
			javax.swing.SwingUtilities.invokeLater(() -> {
				listener.onStatusUpdated(status);
		});
	}
	@Override
	public void onClose(int code,String reason,boolean remote) {
		javax.swing.SwingUtilities.invokeLater(listener::onDisconnected);
	}
	@Override
	public void onError(Exception ex) {
		javax.swing.SwingUtilities.invokeLater(() -> 
			listener.onError(ex.getMessage())
		);
	}
}