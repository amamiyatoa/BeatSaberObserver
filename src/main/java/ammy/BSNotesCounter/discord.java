package ammy.BSNotesCounter;

import dev.firstdark.rpc.DiscordRpc;
import dev.firstdark.rpc.handlers.RPCEventHandler;
import dev.firstdark.rpc.models.User;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.exceptions.*;
import dev.firstdark.rpc.models.DiscordRichPresence;

public class discord {
	
	private static final String CLIENT_ID = "1536225167108214865";
	
	private final DiscordRpc rpc = new DiscordRpc();
    private long songStartTimestamp;
	
	public void connect() {
		rpc.setDebugMode(false);
		
		RPCEventHandler handler =  new RPCEventHandler() {
			@Override
			public void ready(User usr) {
				System.out.println("Discord RPC connected: " + usr.getUsername());
			}
			
			@Override
			public void disconnected(ErrorCode errCode, String msg) {
				System.out.println("Discord RPC disconnected: " + msg);
			}
			
			@Override
			public void errored(ErrorCode errCode, String msg) {
				System.out.println("Discord RPC error: " + msg);
			}
		};
		
		try {
			rpc.init(CLIENT_ID, handler, false);
		} catch (PipeAccessDenied e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOsType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onSongStart(String songName, String songAuthorName) {
		songStartTimestamp = System.currentTimeMillis() / 1000;
		updatePresence(songName, songAuthorName);
	}
	
	public void updatePresence(String songName, String songAuthorName) {
		DiscordRichPresence presence = DiscordRichPresence.builder()
				.state(songAuthorName)
				.details(songName)
				.startTimestamp(songStartTimestamp)
				.largeImageKey("beatsaber_icon")
				.build();
		
		rpc.updatePresence(presence);
	}
	
	public void inMenu() {
		updateInMenu();
	}
	public void updateInMenu() {
		DiscordRichPresence presence = DiscordRichPresence.builder()
				.state("No Playing.")
				.details("In Menu")
				.startTimestamp(songStartTimestamp)
				.largeImageKey("beatsaber_icon")
				.build();
		
		rpc.updatePresence(presence);
	}
	
	public void shutdown() {
		rpc.shutdown();
	}
}