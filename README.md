# 制作しようと思い立った経緯
BeatSaberのMODにてコンボ表示やカットノーツ数を表示するものがあったが、外部ソフトとして配布されているものが無く、自分としてはとても欲しいツールだなと思い立ったため制作開始。
本作品を機にJavaについてもっと知ることができればいいなと思い制作しました。

# 本作品の使い方
## 起動について
起動するにはフォルダ内の.exeファイル、あるいはjarファイルを開くことで起動できます。
起動時にどのフォルダにConfig.jsonがあるか、リザルトログの保存場所はどこに設定されているかがダイアログとして表示されるので、OKボタンを押すかEnterキーを押すことでメインのGUI画面を表示することができます。
BeatSaber起動後にソフトを起動した場合は直接接続されますが、ソフト起動後にBeatSaberを開いた場合は自動接続されませんので、Connectionボタンを押して接続してください。
接続されると接続ステータスのテキストがDisconnectからConnectに変更されます。
Connectと書かれている間はConfig.json通りの設定でリザルトログが保存されます。
メインGUIには現在プレイ中の楽曲情報がリアルタイムで表示されます。

GUI画面上部のメニューバーにある「File」にはリザルト保存フォルダを選択するメニューや設定確認ダイアログの表示メニュー、選択されている保存フォルダを開くメニューや終了ボタンがあります。
「Help」にはソフトのバージョン表示メニューがあります。

# 本作品のこだわり
Gson経由でのjson読み取り、その後の処理をクラスやメソッド分割でなるべく読みやすくすることを心掛けました。そして使用する人の目線となり、IOの例外や処理しきれないときの例外処理をなるべく漏れがないようにしまっした。
UX設計を意識し、ソフト起動時にどこにConfig.jsonの場所やリザルト出力パスの表示・フォルダ選択時にどのフォルダを選択したかを表示するように、フールプルーフの仕組みを心掛けました。

# 本作品制作時点での制作者の動作環境、および動作に使用したライブラリやBeatSaberのMOD
## 動作環境
- OS        : Windows 11 Home 64bit
- CPU       : Ryzen7 7800X3D 8C 16T
- GPU       : AMD Radeon RX 7900XT 20GB OC
- RAM       : DDR5 4800MHz 16GB × 2
- PSU       : 850W 80 Plus Gold
- HMD       : Meta Quest 3S
- Stream    : Virtual Desktop

## 使用ライブラリ
### java標準ライブラリ:
- awt Font
- net URI
- net URISyntaxException

### Javax:
- swing JFrame
- swing JLabel
- swing JPanel
- swing BoxLayout
- swing SwingUtilities

### Gson:
- com google gson
- com google gson annotations SerializedName

### WebSocket:
- org java_websocket client WebSocketClient
- org java_websocket handshake ServerHandshake

### BeatSaber MOD:
- HttpSiraStatus


### exe出力メモ
jpackage --input target/ --main-jar BSNotesCounter-0.0.1-SNAPSHOT-jar-with-dependencies.jar --main-class ammy.BSNotesCounter.App --type app-image --name BSNotesCounter-0.0.9 --app-version 0.0.9 --dest output/