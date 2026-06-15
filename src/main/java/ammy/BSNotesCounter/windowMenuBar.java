package ammy.BSNotesCounter;

import javax.swing.*;

public class windowMenuBar{
	
	static class versionInfo extends JFrame{
		
		private final int versionWindowWidth = 250;
		private final int versionWindowHeight = 100;
		
		versionInfo(){
			setTitle("Version Info");
			setSize(versionWindowWidth, versionWindowHeight);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			setResizable(false);
			
			
			setVisible(true);
		}
	}
	
}