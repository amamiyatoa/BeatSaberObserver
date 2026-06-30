package ammy.BSNotesCounter;

import com.google.gson.annotations.SerializedName;

public class BeatSaberStatus {
	public String event;
	public StatusData status;
	
	static class StatusData {
		// BSのstatusオブジェクトをマッピングして情報を保持する
		public Performance performance;
		public BeatmapData beatmap;
	}
	
	// Definition Performance
	// 曲のスコアやコンボ数をJSONから取得する
	static class Performance {
		public int score;
		public int combo;
		@SerializedName("missedNotes")
		public int missedNotes;
		@SerializedName("hitNotes")
		public int hitNotes;
		@SerializedName("maxCombo")
		public int maxCombo;
	}
	static class BeatmapData {
		// 楽曲から曲名や作成者・難易度を取得する
		@SerializedName("songName")
		public String songName;
		@SerializedName("songAuthorName")
		public String songAuthorName;
		public String difficulty;
		@SerializedName("noteJumpSpeed")
		public Number notesJumpSpeed;
		@SerializedName("songBPM")
		public Number songBPM;
		@SerializedName("songCover")
		public String songCover;
	}
}