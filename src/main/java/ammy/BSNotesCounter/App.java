package ammy.BSNotesCounter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.imageio.*;

public class App extends JFrame implements BeatSaberWebSocketClient.StatusUpdateListener{
	
	// ── Add UI Parts ──────────────────────

	//各種表示する物のラベル定義
	private JLabel status			= new JLabel("⚪ Disconnected");
	private JLabel songCover		= new JLabel("");
	private JLabel songName			= new JLabel("Song: -");
	private JLabel songAuthor		= new JLabel("Song Author: -");
	private JLabel songDifficulty	= new JLabel("[ - ]");
	private JLabel notesJumpSpeed	= new JLabel("Notes Speed: -");
	private JLabel songBPM			= new JLabel("Song BPM: -");
	private JLabel score			= new JLabel("Score: -");
	private JLabel combo			= new JLabel("Combo: -");
	private JLabel maxCombo			= new JLabel("Max Combo: -");
	private JLabel hitNotes			= new JLabel("Total Hit Notes: -");
	private JLabel miss				= new JLabel("Miss:  -");
	
	//メニューバー作成
	private JMenuBar windowMenuBar = new JMenuBar();
	private JMenu helpMenu = new JMenu("Help(_H)");
	private JMenuItem versionMenuItem = new JMenuItem("Version");
	private JMenuItem exitMenuItem = new JMenuItem("Exit");
	
	//WebSocketと接続するためのボタンを作成
	private JButton connectionButton = new JButton("Connect");
	
	//フォントサイズを定数化(25px)
	private final int changedFontSize = 25;
	//ラベルをlblsで1次元配列化
	JLabel[] lbls = {
			status,songName, songAuthor, songDifficulty, notesJumpSpeed, songBPM, score, combo, maxCombo, miss, hitNotes
	};
	//各ラベルに定数化したフォントサイズを適応
	{
		for (JLabel label : lbls){
			Font currentFont = label.getFont();
		
			Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), changedFontSize);
			label.setFont(newFont);
		}
	}
	
	// ──────────────────────────────────────
	
	//── Window Setting ─────────────────────
	
	//ウィンドウの縦横サイズを定数化
	private final int winWidth	= 650;
	private final int winHeight	= 450;
	
	// ──────────────────────────────────────
	
	private BeatSaberWebSocketClient wsClient;
	
	public App() {
		init();
		guiDisplay();
		
		setVisible(true);
	}
	
	//起動時の初期設定
	public void init() {
		setTitle("BeatSaber Observer");						//ウィンドウタイトルを設定
		setSize(winWidth,winHeight);						//ウィンドウサイズの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//閉じるボタンの挙動を設定
		setLocationRelativeTo(null);						//起動時の初期位置を設定
		setResizable(false);								//ウィンドウ表示後のサイズ変更の無効化
		
		versionMenuItem.addActionListener(e -> {
			JOptionPane.showMessageDialog(
					null,
					"Version: 0.0.5-SNAPSHOT",
					"Version Information",
					JOptionPane.INFORMATION_MESSAGE
			);
		});
		exitMenuItem.addActionListener(e -> {
			int chooseConfirm = JOptionPane.showConfirmDialog(
					null,
					"終了しますか？",
					"Confirm",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
			);
			if (chooseConfirm == JOptionPane.YES_OPTION) {
				System.exit(0);
			} else if (chooseConfirm == JOptionPane.NO_OPTION) {
				return;
			}
		});
		
		helpMenu.add(versionMenuItem);
		helpMenu.add(exitMenuItem);
		windowMenuBar.add(helpMenu);
		setJMenuBar(windowMenuBar);
		
		connectWebSocket();									//WebSocketに接続
	}
	
	//WebSocket接続クラス
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
			songName.setText("♫ " + map.songName);									//現在の楽曲での曲名と難易度を取得して表示する
			notesJumpSpeed.setText("Notes Speed:" + map.notesJumpSpeed);			//現在の楽曲のNJSを取得して表示する
			songBPM.setText("Song BPM:" + map.songBPM);								//現在の楽曲のBPMを取得して表示する
			setSongImages(map.songCover, 128);										//現在の楽曲のジャケット画像を表示する
			songAuthor.setText("Song Author:" + map.songAuthorName);				//
			songDifficulty.setText("[ " + map.difficulty + " ]");					//
			
		}
		
		// score, combo, miss
		if (status.status.performance != null) {
			BeatSaberStatus.Performance perf = status.status.performance;
			score.setText("Score: " + perf.score);					//現在のスコアを取得して表示する
			combo.setText("Combo: " + perf.combo);					//現在のコンボ数を取得して表示する
			maxCombo.setText("Max Combo:" + perf.maxCombo);			//現在の最大コンボ数を所得して表示する
			hitNotes.setText("Total Hit Notes: " + perf.hitNotes);	//現在の総ヒットノーツ数を取得して表示する
			miss.setText("Miss: " + perf.missedNotes);				//現在の総ミス数を取得して表示する
		}
	}
	
	/**
	 * Base64文字列をデコードしてJLabelにセット
	 * @param base64Str		status.beatmap.songCoverの値
	 * @param size			表示サイズ
	 */
	private void setSongImages(String base64Str, int size) {
		if(base64Str == null || base64Str.isEmpty()) {
			songCover.setIcon(null);
			return;
		}
		
		try {
			//1, Base64 -> Byte Array
			byte[] songImageBytes = Base64.getDecoder().decode(base64Str);
			
			//2, Byte Array -> BufferedImage
			BufferedImage buffImage = ImageIO.read(new ByteArrayInputStream(songImageBytes));
			
			//3, Resize
			Image scaledImage = buffImage.getScaledInstance(size, size,  Image.SCALE_SMOOTH);
			
			//4, ImageIcon -> set JLabel
			songCover.setIcon(new ImageIcon(scaledImage));
		} catch (Exception e) {
			songCover.setIcon(null);
		}
	}
	
	// Reset Display
	private void resetLabels() {								//ラベルリセット時の表示テキスト
		songCover.setIcon(null);
		songName.setText("Song: -");
		songAuthor.setText("Song Author: -");
		songDifficulty.setText("[ - ]");
		
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
		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.X_AXIS));
		
		JPanel leftFrame = new JPanel();
		leftFrame.setLayout(new BoxLayout(leftFrame, BoxLayout.Y_AXIS));
		leftFrame.setPreferredSize(new Dimension(winWidth / 2, winHeight));//leftFrameの
		leftFrame.add(status);							//現在接続されているか切断しているかの表示をパネルに追加
		leftFrame.add(notesJumpSpeed);					//現在プレイ中の楽曲のNJSをパネルに追加
		leftFrame.add(songBPM);							//現在プレイ中の楽曲のBPMをパネルに追加
		leftFrame.add(score);							//現在のスコアをパネルに追加
		leftFrame.add(combo);							//現在のコンボ数をパネルに追加
		leftFrame.add(maxCombo);						//現在プレイ中の楽曲での最大コンボ数をパネルに追加
		leftFrame.add(hitNotes);						//現在プレイ中の楽曲でのヒットノーツ数をパネルに追加
		leftFrame.add(miss);							//現在プレイ中の楽曲でのミスカット・通過ノーツ数をパネルに追加
		leftFrame.add(connectionButton);				//BeatSaberのWebSocket接続をするボタンをパネルに追加
		
		JPanel rightFrame = new JPanel();
		rightFrame.setLayout(new BoxLayout(rightFrame, BoxLayout.Y_AXIS));
		rightFrame.setPreferredSize(new Dimension(winWidth / 2, winHeight));
		rightFrame.add(songCover);						//現在プレイ中の楽曲のジャケット画像をパネルに追加
		rightFrame.add(songName);						//現在プレイ中の楽曲名をパネルに追加
		rightFrame.add(songAuthor);						//現在プレイ中の楽曲の作者名をパネルに追加
		rightFrame.add(songDifficulty);					//現在プレイ中の楽曲の難易度をパネルに追加
		
		connectionButton.addActionListener(e -> {		//connectionButtonをクリックしたときのアクション
			connectWebSocket();
		});
		
		mainFrame.add(leftFrame);						//mainFrameにleftFrameｑを追加
		mainFrame.add(rightFrame);						//mainFrameにrightFrameを追加
		add(mainFrame);									//mainFrameをJFrameに追加
	}
	
	public static void main(String[] args) {			// 実行用mainメソッド
		SwingUtilities.invokeLater(App::new);
	}
}