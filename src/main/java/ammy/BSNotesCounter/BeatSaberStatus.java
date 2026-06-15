package ammy.BSNotesCounter;

import com.google.gson.annotations.SerializedName;

public class BeatSaberStatus {
	String event;
	StatusData status;
	
	static class StatusData {
		// BSのstatusオブジェクトをマッピングして情報を保持する
		Performance performance;
		BeatmapData beatmap;
	}
	
	// Definition Performance
	// 曲のスコアやコンボ数をJSONから取得する
	static class Performance {
		int score;
		int combo;
		@SerializedName("missedNotes")
		int missedNotes;
		@SerializedName("hitNotes")
		int hitNotes;
		@SerializedName("maxCombo")
		int maxCombo;
	}
	static class BeatmapData {
		// 楽曲から曲名や作成者・難易度を取得する
		@SerializedName("songName")
		String songName;
		@SerializedName("songAuthorName")
		String songAuthorName;
		String difficulty;
		@SerializedName("noteJumpSpeed")
		int notesJumpSpeed;
		@SerializedName("songBPM")
		int songBPM;
		@SerializedName("songCover")
		String songCover;
	}
}