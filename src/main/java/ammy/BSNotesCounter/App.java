package ammy.BSNotesCounter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.*;

public class App extends JFrame implements BeatSaberWebSocketClient.StatusUpdateListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ── Add UI Parts ──────────────────────

	//definitions label
	private JLabel connectStatus					= new JLabel("⚪ Disconnected");
	private JLabel songCover				= new JLabel("");
	private int songCoverImageSize			= 200;
	private int songCoverBorderSize			= 3;
	private final ImageIcon alternativeIcon = new ImageIcon("src/main/java/ammy/BSNotesCounter/img/alternativeNoImage.png");
	Border songCoverBorder					= BorderFactory.createLineBorder(Color.BLACK, songCoverBorderSize);
	private JLabel songName					= new JLabel("Song: -");
	private JLabel songAuthor				= new JLabel("Song Author: -");
	private JLabel songDifficulty			= new JLabel("[ - ]");
	private JLabel notesJumpSpeed			= new JLabel("Notes Speed: -");
	private JLabel songBPM					= new JLabel("Song BPM: -");
	private JLabel score					= new JLabel("Score: -");
	private JLabel combo					= new JLabel("Combo: -");
	private JLabel maxCombo					= new JLabel("Max Combo: -");
	private JLabel hitNotes					= new JLabel("Total Hit Notes: -");
	private JLabel miss						= new JLabel("Miss:  -");
	
	JLabel[] leftLabels = {
			connectStatus, notesJumpSpeed, songBPM, score, combo, maxCombo, hitNotes, miss
	};
	JLabel[] rightLabels = {
			songCover, songName, songAuthor, songDifficulty
	};
	
	//Application version constant
	private String versions					= "0.0.8-SNAPSHOT";
	
	//Create menu bar
	private JMenuBar windowMenuBar			= new JMenuBar();
	private JMenu fileMenu					= new JMenu("File(_F)");
	private JMenu helpMenu					= new JMenu("Help(_H)");
	private JMenuItem openFolder			= new JMenuItem("Open Folder");
	private JMenuItem settingView			= new JMenuItem("View Setting");
	private JMenuItem exitMenuItem			= new JMenuItem("Exit");
	private JMenuItem versionMenuItem		= new JMenuItem("Version");
	
	//Create WebSocket connection button
	private JButton connectionButton 		= new JButton("Connect");
	
	private JPanel createLeftPanel() {
		JPanel leftFrame = new JPanel();										//create leftFrame
		leftFrame.setLayout(new BoxLayout(leftFrame, BoxLayout.Y_AXIS));		//setting layout of leftFrame
		leftFrame.setAlignmentX(Component.CENTER_ALIGNMENT);					//Setting align the leftFrame
		leftFrame.setPreferredSize(new Dimension(winWidth / 2, winHeight));		//Setting Prefer Size of leftFrame
		leftFrame.setMinimumSize(new Dimension(winWidth / 2, winHeight));		//Setting Minimum Size of leftFrame
		leftFrame.setMaximumSize(new Dimension(winWidth / 2, winHeight));		//Setting Maximum Size of leftFrame
		leftFrame.add(Box.createVerticalGlue());								//Add a margin to the top of leftFrame
		for(JLabel leftLbls : leftLabels) {										//Add labels of leftFrame
			leftFrame.add(leftLbls);
		};
		leftFrame.add(connectionButton);
		connectionButton.addActionListener(e -> connectWebSocket());
		leftFrame.add(Box.createVerticalGlue());								//Add a margin to the bottom of leftFrame
		return leftFrame;														//Return a value to createLeftFrame
	}
	private JPanel createRightPanel() {
		JPanel rightFrame = new JPanel();						//create rightFrame
		rightFrame.setLayout(new BoxLayout(rightFrame, BoxLayout.Y_AXIS));		//setting layout of rigthFrame
		rightFrame.setPreferredSize(new Dimension(winWidth / 2, winHeight));	//Setting Prefer Size of rightFrame
		rightFrame.setMinimumSize(new Dimension(winWidth / 2,winHeight));		//Setting Minimum Size of rightFrame
		rightFrame.setMaximumSize(new Dimension(winWidth / 2, winHeight));		//setting Maximum Size of rightFrame
		rightFrame.add(Box.createVerticalGlue());								//Add a margin to the top of rightFrame
		songCover.setBorder(songCoverBorder);									//Add borders of songCover
		for(JLabel rightLbls : rightLabels) {									//Add labels of rightFrame
			rightFrame.add(rightLbls);
		}
		rightFrame.add(Box.createVerticalGlue());								//Add a margin to the bottom of rightFrame
		return rightFrame;														//Return a value to createRightFrame
	}
	
	//Constants for Font Sizes(25px)
	private final int changedFontSize 		= 20;
	
	// ──────────────────────────────────────
	
	//── Window Setting ─────────────────────
	
	//Set the window's width and height to constants
	private final int winWidth				= 650;
	private final int winHeight				= 450;
	
	// ──────────────────────────────────────
	
	private BeatSaberWebSocketClient wsClient;
	private Config config = new Config();
	private static final String ConfigDir	= System.getProperty("user.home") + File.separator + ".BSNotesCounter";
	private static final String ConfigFile	= "Config.json";
	public bsPerfInfo bsPerfInfo;
	public bsMapInfo bsMapInfo;
	
	public App() {
		init();						//Application initialization
		guiDisplay();				//Set GUI for Application
		
		setVisible(true);
	}
	
	//Initialization at startup
	public void init() {
		setTitle("BeatSaber Observer");						//Setting Window Title
		setSize(winWidth,winHeight);						//Setting Window Size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//Setting default Close Button
		setLocationRelativeTo(null);						//Setting at startup location
		setResizable(false);								//Disable window size resizable
		try {
			File configDir = new File(ConfigDir);
			String configPath = ConfigDir + File.separator + ConfigFile;
			File configFileFullPath = new File(configPath);
			if (!configDir.exists()) {
				configDir.mkdirs();
			}
			
			if(!configFileFullPath.exists()) {
				this.config = Config.createNewSetting(configPath);
				JOptionPane.showMessageDialog(
						null,
						"新しい設定ファイルを作成しました！\n設定ファイルの場所: " + configFileFullPath.getAbsolutePath(),
						"Create new setting file",
						JOptionPane.INFORMATION_MESSAGE
					);
			} else if(configFileFullPath.exists()) {
				this.config = Config.loadFromFile(configPath);
				File resultFileFullPath = new File(config.getResult().getResultOutputPath());
				JOptionPane.showMessageDialog(
						null,
						"既存設定を読み込みました！\n設定ファイルの場所: " + configFileFullPath.getAbsolutePath() + "\n" +
						"リザルトの保存場所: " + resultFileFullPath.getAbsolutePath(),
						"Config File Exists",
						JOptionPane.INFORMATION_MESSAGE
					);
			}
			
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					null,
					"予期せぬエラーが発生しました\nErr: " + e.getMessage(),
					"Exception",
					JOptionPane.ERROR_MESSAGE
				);
			e.printStackTrace();
			System.exit(0);
		}

		//Apply font size to labels
		for (JLabel label : leftLabels){
			applyFontSize(label, changedFontSize);
		}
		for (JLabel label : rightLabels) {
			applyFontSize(label, changedFontSize);
		}
		
		//File Selector
		openFolder.addActionListener(e -> {
			try {
				String configPath = ConfigDir + File.separator + ConfigFile;
				this.config = Config.loadFromFile(configPath);
				JFileChooser dirChooser = new JFileChooser(this.config.getResult().getResultOutputPath());
				dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int directorySelectedResult = dirChooser.showOpenDialog(null);
				if(directorySelectedResult == JFileChooser.APPROVE_OPTION) {
					File selectedDir = dirChooser.getSelectedFile();
					String selectedDirPath = selectedDir.getPath();
					try {
						config.getResult().setResultOutputPath(selectedDirPath);
						config.saveToFile(Config.configFilePath);
						JOptionPane.showMessageDialog(
								null,
								"Selected Directories:" + selectedDir,
								"What's selected Directory",
								JOptionPane.INFORMATION_MESSAGE
							);
						System.out.println("Changed log directory: " + selectedDir);
					} catch(IOException changeDirErr) {
						changeDirErr.printStackTrace();
					}
				} else if(directorySelectedResult == JFileChooser.CANCEL_OPTION) {
					return;
				}
			} catch (IOException configErr) {
				configErr.printStackTrace();
			}
		});
		
		settingView.addActionListener(e -> {
			boolean saving = this.config.getResult().isSaveEnabled();
			String resultPath = this.config.getResult().getResultOutputPath();
			File resultOutputFullPath = new File(resultPath);
			String configFilePath = ConfigDir + File.separator + ConfigFile;
			JOptionPane.showMessageDialog(
					null,
					"Save Enabled: " + saving + "\nOutput Path: " + resultOutputFullPath.getAbsolutePath() + "\nSetting File Directories: " + configFilePath,
					"Setting Viewer",
					JOptionPane.INFORMATION_MESSAGE
			);
		});
		
		//Display Version Information
		versionMenuItem.addActionListener(e -> {
			JOptionPane.showMessageDialog(
					null,
					"Version: " + versions,
					"Version Information",
					JOptionPane.INFORMATION_MESSAGE
			);
		});
		//Exit Application
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

		//Display Menu Items
		fileMenu.add(openFolder);
		fileMenu.add(settingView);
		fileMenu.add(exitMenuItem);
		helpMenu.add(versionMenuItem);
		windowMenuBar.add(fileMenu);
		windowMenuBar.add(helpMenu);
		setJMenuBar(windowMenuBar);
		
		connectWebSocket();									//Connect WebSocket
	}
	
	//apply GUI font size
	private void applyFontSize(JLabel label, int size) {
		Font currentFont = label.getFont();
		Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), size);
		label.setFont(newFont);
	}
	
	//WebSocket connection class
	private void connectWebSocket() {
		try {
			if (wsClient != null)
			{
				wsClient.close();
			}
			wsClient = new BeatSaberWebSocketClient(this);
			wsClient.connect();
		} catch (URISyntaxException e) {
			connectStatus.setText("❌ Invalid URI");
		}
	}
	
	// ──call back───────────────────────────
	
	@Override
	public void onConnected() {
		connectStatus.setText("🟢 Connected");
	}
	@Override
	public void onDisconnected() {
		connectStatus.setText("⚪ Disconnected");
		resetLabels();
	}
	@Override
	public void onError(String message) {
		connectStatus.setText("❌️ Error:" + message);
	}

	//for ResultFileOutput
	public static class bsPerfInfo{
		
		public int score;
		public int combo;
		public int maxCombo;
		public int hitNotes;
		public int miss;
		
		public bsPerfInfo(BeatSaberStatus.Performance perf) {
			this.score			= perf.score;
			this.combo			= perf.combo;
			this.maxCombo		= perf.maxCombo;
			this.hitNotes		= perf.hitNotes;
			this.miss			= perf.missedNotes;
		}
	}
	public static class bsMapInfo{
		
		public String songName;
		public String songAuthor;
		public String songDifficulty;
		public Number notesJumpSpeed;
		public Number songBPM;
		
		public bsMapInfo(BeatSaberStatus.BeatmapData map) {
			this.songName		= map.songName;
			this.songAuthor		= map.songAuthorName;
			this.songDifficulty	= map.difficulty;
			this.notesJumpSpeed = map.notesJumpSpeed;
			this.songBPM		= map.songBPM;
		}
	}
	
	@Override
	public void onStatusUpdated(BeatSaberStatus status) {
		if (status.status == null) return;
		
		// song info
		SwingUtilities.invokeLater(() -> {
			
			BeatSaberStatus.BeatmapData map = status.status.beatmap;
			BeatSaberStatus.Performance perf = status.status.performance;
			
			if (map != null) {
				songName.setText("♫ " + map.songName);									//Display the current song name
				int njs = map.notesJumpSpeed.intValue();								//Convert njs into the int type
				notesJumpSpeed.setText("Notes Speed:" + njs);							//Display the current notes speed
				songBPM.setText("Song BPM:" + map.songBPM);								//Display the current song BPM
				setSongImages(map.songCover);											//Display the current song jacket images
				songAuthor.setText("🖋️" + map.songAuthorName);							//Display the current song author
				songDifficulty.setText("[ " + map.difficulty + " ]");					//Display the current song difficulty
				
			}
			
			// score, combo, miss
			if (perf != null) {
				score.setText("Score: " + perf.score);					//get and display the current score
				combo.setText("Combo: " + perf.combo);					//get and display the current combo
				maxCombo.setText("Max Combo:" + perf.maxCombo);			//get and display the max combo
				hitNotes.setText("Total Hit Notes: " + perf.hitNotes);	//get and display the total hit notes
				miss.setText("Miss: " + perf.missedNotes);				//get and display the total miss notes
				
			}
			//save the log file when finished
			if("songStart".equals(status.event)) {
				this.bsMapInfo		= new bsMapInfo(map);
			}
			if("finished".equals(status.event) || "failed".equals(status.event)) {
				this.bsPerfInfo		= new bsPerfInfo(perf);
				finished(bsPerfInfo,bsMapInfo);
			}
		});
	}
	
	//finished method
	private void finished(bsPerfInfo bsPerfInfo, bsMapInfo bsMapInfo) {
		try {
			String configPath = ConfigDir + File.separator + ConfigFile;
			config = Config.loadFromFile(configPath);
			File resultOutputFullPath = new File(config.getResult().getResultOutputPath());
			String resultOutputFullPaths = resultOutputFullPath.getAbsolutePath();
			if(config.getResult().isSaveEnabled()) {
				new ResultFileOutput(resultOutputFullPaths, bsPerfInfo, bsMapInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Base64str decode and set into the label
	private void setSongImages(String base64Str) {
		if(base64Str == null || base64Str.isEmpty()) {
			songCover.setIcon(alternativeIcon);
			return;
		}
		
		try {
			//1, Base64 -> Byte Array
			byte[] songImageBytes = Base64.getDecoder().decode(base64Str);
			
			//2, Byte Array -> BufferedImage
			BufferedImage buffImage = ImageIO.read(new ByteArrayInputStream(songImageBytes));
			
			//3, Resize
			Image scaledImage = buffImage.getScaledInstance(songCoverImageSize, songCoverImageSize,  Image.SCALE_SMOOTH);
			
			//4, ImageIcon -> set JLabel
			songCover.setIcon(new ImageIcon(scaledImage));
		} catch (IOException e) {
			System.err.println("Failed to read image: " + e.getMessage());
			songCover.setIcon(alternativeIcon);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid Base64 string: " + e.getMessage());
			songCover.setIcon(alternativeIcon);
		}
	}
	
	// Reset Display
	private void resetLabels() {								//Reset labels
		songCover.setIcon(alternativeIcon);
		songName.setText("Song: -");
		songAuthor.setText("Song Author: -");
		songDifficulty.setText("[ Difficulty: - ]");
		
		notesJumpSpeed.setText("Notes Speed: -");
		songBPM.setText("Song BPM: -");
		score.setText("Score: -");
		combo.setText("Combo: -");
		maxCombo.setText("Max Combo: -");
		hitNotes.setText("Total Hit Notes: -");
		miss.setText("Miss:  -");
	}
	
	
	public void guiDisplay() {
		JPanel mainFrame = new JPanel();									//create mainFrame
		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.X_AXIS));	//setting layout of mainFrame
		
		mainFrame.add(createLeftPanel());						//add createLeftPanel to mainFrame
		mainFrame.add(createRightPanel());						//add createRightPanel to mainFrame
		add(mainFrame);											//add mainFrame
	}
	
	public static void main(String[] args) {					//run main method
		SwingUtilities.invokeLater(App::new);
	}
}