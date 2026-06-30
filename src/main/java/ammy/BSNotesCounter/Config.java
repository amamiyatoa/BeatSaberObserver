package ammy.BSNotesCounter;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

//メインクラス
public class Config {
	//ResultConfig型として内部データを保持する
	private ResultConfig result;
	
	//コンストラクタ（初期化処理）
	public Config() {
		this.result = new ResultConfig();
	}
	
	//フィールドの値を取り出す（ゲッター）
	public ResultConfig getResult() {
		return result;
	}
	
	//フィールドに新しく値をセットする（セッター）
	public void setResult(ResultConfig result) {
		this.result = result;
	}
	
	public static String configFilePath;
	
	//JSONを読み取ってJavaオブジェクトとして解釈する
	public static Config loadFromFile(String filePath) throws IOException{
		//読み取ったJSONをUTF-8でエンコードして読み取る
		try(Reader reader = new FileReader(filePath, StandardCharsets.UTF_8)){
			configFilePath = filePath;
			//Gsonを使う宣言
			Gson gson = new Gson();
			//JSONをConfigクラスのオブジェクトとして変換する
			return gson.fromJson(reader, Config.class);
		}
	}
	
	//JavaのオブジェクトをJSONファイルに変換して保存する
	public void saveToFile(String filePath) throws IOException{
		//受け取ったオブジェクトをファイルに書き込む
		try(Writer writer = new FileWriter(filePath, StandardCharsets.UTF_8)){
			//GsonでJSONをフォーマットして見やすくする
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//見やすくした文章をJSONに変換して書き込む
			gson.toJson(this, writer);
		}
	}
	
	public static Config createNewSetting(String filePath) {
		Config config = new Config();
		config.getResult().setSaveEnabled(true);
		config.getResult().setResultOutputPath("results/logs");
		try {
			Path configPath = Paths.get("results/logs");
			Files.createDirectories(configPath);
			config.saveToFile(filePath);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	//リザルト関係の設定データをResultConfigとしてカプセル化
	public static class ResultConfig {
		//保存の可否
		@SerializedName("saveEnabled")
		boolean saveEnabled;
		//保存先パスの指定
		@SerializedName("resultOutputPath")
		private String resultOutputPath;
		
		
		public ResultConfig() {
			this.saveEnabled = true;
			this.resultOutputPath = "results/logs";
		}
		
		//保存の可否設定を取り出す
		public boolean isSaveEnabled() {
			return saveEnabled;
		}
		
		//保存の可否設定を変更する
		public void setSaveEnabled(boolean saveEnabled) {
			this.saveEnabled = saveEnabled;
		}
		
		//保存先パスを取り出す
		public String getResultOutputPath() {
			return resultOutputPath;
		}
		
		//保存先パスを変更する
		public void setResultOutputPath(String resultOutputPath) {
			this.resultOutputPath = resultOutputPath;
		}
		
		@Override
		public String toString() {
			return "ResultConfig{" + "saveEnabled=" + saveEnabled + ", ResultOutputPath='" + resultOutputPath + '\'' + '}';
		}
	}
	
	@Override
	public String toString() {
		return "Config{" + "result=" + result + '}';
	}
}