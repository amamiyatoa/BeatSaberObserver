package ammy.BSNotesCounter;

import ammy.BSNotesCounter.App.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ResultFileOutput {
	
	public ResultFileOutput(String resultOutputFile, bsPerfInfo perfInfo, bsMapInfo mapInfo,String version, String songFinishResult) {
		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();
		int year = nowDate.getYear();
		int month = nowDate.getMonthValue();
		int day = nowDate.getDayOfMonth();
		int hour = nowTime.getHour();
		int minute = nowTime.getMinute();
		int second = nowTime.getSecond();
		String currDate = year + "-" + month + "-" + day;
		String currFileTime = hour + "-" + minute + "-" + second;
		String currLogTime = hour + ":" + minute + ":" + second;
		String dateTime = currDate + "_" + currFileTime;
		
		String fileName = dateTime + mapInfo.songName + "[" + mapInfo.songDifficulty + "]" + ".log";
		String fileFullPath = resultOutputFile + File.separator;
		
		try(FileWriter writer = new FileWriter(fileFullPath + fileName)) {
			String writeData = 
					"\nBeatSaber Notes Counter\nVersions: "	+ version + "\n"			+ "\n" +
					"<====================>\n"											+ "\n" +
					"Beatmap Information\n"												+ "\n" +
					"<====================>\n"											+ "\n" +
					"Song Name: "							+ mapInfo.songName			+ "\n" +
					"Song Author: "							+ mapInfo.songAuthor		+ "\n" +
					"Song Difficulty: "						+ mapInfo.songDifficulty	+ "\n" +
					"Song BPM: "							+ mapInfo.songBPM			+ "\n" +
					"NJS: "									+ mapInfo.notesJumpSpeed	+ "\n" +
					"\n<====================>\n"										+ "\n" +
					"Player Grade\n"													+ "\n" +
					"Score: "								+ perfInfo.score			+ "\n" +
					"Combo: "								+ perfInfo.combo			+ "\n" +
					"Max Combo: "							+ perfInfo.maxCombo			+ "\n" +
					"Hit Notes: "							+ perfInfo.hitNotes			+ "\n" +
					"Miss: "								+ perfInfo.miss				+ "\n" +
					"Song Clear: "							+ songFinishResult			+ "\n" +
					"\n<====================>";
			writer.write(writeData);
			System.out.println(currLogTime + " | Result saved: [" + fileFullPath + fileName + " ]");
		} catch(IOException e) {
			System.err.println("File write error: [" + e.getMessage() + " ]");
		}
	}
}