package ammy.BSNotesCounter;

import ammy.BSNotesCounter.App.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ResultFileOutput {
	
	public ResultFileOutput(String resultOutputFile, bsPerfInfo perfInfo, bsMapInfo mapInfo) {
		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();
		int year = nowDate.getYear();
		int month = nowDate.getMonthValue();
		int day = nowDate.getDayOfMonth();
		int hour = nowTime.getHour();
		int minute = nowTime.getMinute();
		int second = nowTime.getSecond();
		String dateTime = year + "-" + month + "-" + day + "-" + hour + "-" + minute + "-" + second + "-";
		
		String fileName = dateTime + mapInfo.songName + "[" + mapInfo.songDifficulty + "]" + ".log";
		File filePath = new File(fileName);
		String fileFullPath = resultOutputFile + File.separator + filePath;
		
		try(FileWriter writer = new FileWriter(fileFullPath)) {
			String writeData = 
					"Song Name: "		+ mapInfo.songName			+ "\n" +
					"Song Author: "		+ mapInfo.songAuthor		+ "\n" +
					"Song Difficulty: "	+ mapInfo.songDifficulty	+ "\n" +
					"NotesJumpSpeed: "	+ mapInfo.notesJumpSpeed	+ "\n" +
					"Song BPM: "		+ mapInfo.songBPM			+ "\n" +
					"Score: "			+ perfInfo.score			+ "\n" +
					"Combo: "			+ perfInfo.combo			+ "\n" +
					"Max Combo: "		+ perfInfo.maxCombo			+ "\n" +
					"Hit Notes: "		+ perfInfo.hitNotes			+ "\n" +
					"Miss: "			+ perfInfo.miss;
			writer.write(writeData);
			System.out.println("Result saved: [" + fileFullPath + " ]");
		} catch(IOException e) {
			System.err.println("File write error: [" + e.getMessage() + " ]");
		}
	}
}