package ammy.BSNotesCounter;

import java.awt.Font;
import java.net.URISyntaxException;

import javax.swing.*;

public class App extends JFrame implements BeatSaberWebSocketClient.StatusUpdateListener{
	
	// ── Add UI Parts ──────────────────────

	private JLabel status			= new JLabel("⚪ Disconnected");
	private JLabel songName			= new JLabel("Song: -");
	private JLabel notesJumpSpeed	= new JLabel("Notes Speed: -");
	private JLabel songBPM			= new JLabel("Song BPM: -");
	private JLabel score			= new JLabel("Score: -");
	private JLabel combo			= new JLabel("Combo: -");
	private JLabel maxCombo			= new JLabel("Max Combo: -");
	private JLabel hitNotes			= new JLabel("Total Hit Notes: -");
	private JLabel miss				= new JLabel("Miss:  -");
	private JButton connectionButton = new JButton("Connect");
	
	private final int changedFontSize = 25;
	JLabel[] lbls = {status, songName, notesJumpSpeed, songBPM, score, combo, maxCombo, miss, hitNotes};
	{
		for (JLabel label : lbls){
			Font currentFont = label.getFont();
		
			Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), changedFontSize);
			label.setFont(newFont);
		}
	}
	
	// ──────────────────────────────────────
	
	//── Window Setting ─────────────────────
	
	private final int winWidth	= 550;
	private final int winHeight	= 400;
	
	// ──────────────────────────────────────
	
	private BeatSaberWebSocketClient wsClient;
	
	public App() {
		init();
		guiDisplay();
		
		setVisible(true);
	}
	
	public void init() {
		setTitle("BeatSaber Observer");
		setSize(winWidth,winHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		connectWebSocket();
	}
	private void connectWebSocket() {
		try {
			if (wsClient != null)
			{
				wsClient.close();
			}
			wsClient = new BeatSaberWebSocketClient(this);
			wsClient.connect();
		} catch (URISyntaxException e) {
			status.setText("❌ Invalid URI");
		}
	}
	
	// ──call back───────────────────────────
	
	@Override
	public void onConnected() {
		status.setText("🟢 Connected");
	}
	@Override
	public void onDisconnected() {
		status.setText("⚪ Disconnected");
		resetLabels();
	}
	@Override
	public void onError(String message) {
		status.setText("❌️ Error:" + message);
	}
	@Override
	public void onStatusUpdated(BeatSaberStatus status) {
		if (status.status == null) return;
		
		// song info
		if (status.status.beatmap != null) {
			BeatSaberStatus.BeatmapData map = status.status.beatmap;
			songName.setText("♫ " + map.songName + " [" + map.difficulty + "] ");
			notesJumpSpeed.setText("Notes Speed:" + map.notesJumpSpeed);
			songBPM.setText("Song BPM:" + map.songBPM);
		}
		
		// score, combo, miss
		if (status.status.performance != null) {
			BeatSaberStatus.Performance perf = status.status.performance;
			score.setText("Score: " + perf.score);
			combo.setText("Combo: " + perf.combo);
			maxCombo.setText("Max Combo:" + perf.maxCombo);
			hitNotes.setText("Total Hit Notes: " + perf.hitNotes);
			miss.setText("Miss: " + perf.missedNotes);
		}
	}
	
	// Reset Display
	private void resetLabels() {								// 
		songName.setText("Song: -");
		notesJumpSpeed.setText("Notes Speed: -");
		songBPM.setText("Song BPM: -");
		score.setText("Score: -");
		combo.setText("Combo: -");
		maxCombo.setText("Max Combo: -");
		hitNotes.setText("Total Hit Notes: -");
		miss.setText("Miss:  -");
	}
	
	public void guiDisplay() {
		JPanel mainFrame = new JPanel();
		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
		mainFrame.add(status);
		mainFrame.add(songName);
		mainFrame.add(notesJumpSpeed);
		mainFrame.add(songBPM);
		mainFrame.add(score);
		mainFrame.add(combo);
		mainFrame.add(maxCombo);
		mainFrame.add(hitNotes);
		mainFrame.add(miss);
		mainFrame.add(connectionButton);
		add(mainFrame);
		connectionButton.addActionListener(e -> {
			connectWebSocket();
		});
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(App::new);
	}
}