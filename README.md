# 制作しようと思い立った経緯
BeatSaberのMODにてコンボ表示やカットノーツ数を表示するものがあったが、外部ソフトとして配布されているものが無く、自分としてはとても欲しいツールだなと思い立ったため制作開始。
本作品を機にJavaについてもっと知ることができればいいなと思い制作いたしました。

# 本作品のこだわり
基本的にはWebSocket経由で流れてきたデータをGsonを使ってString型やInt型・Number型などで定義してJFrameでGUIを作成し、JSONとして受け取った値をJLabelに落とすという単純な構造をしてますが、ソースコードを見た人が一目みて理解できるように変数を定義する順番や表示順を統一したり、変数名をなるべくどのような用途なのかを略語を使わずに記述したところがこだわりだと思います。
生の数値を使うマジックナンバーなどを極力使わずに一目見て理解できる変数名を使うことで可読性の向上、かつ自分自身で振り返ってみて理解できるという利点からこのようにしました。

# 本作品で培った知識
この作品では、変数をどれだけわかりやすく定義するか、どのように書けばより閲覧者にとって楽に解読できるかを考えながらコードを記述しました。そのおかげか、自分であとから見直して、ここはこういう動作のための変数だ、ここはこういう型じゃないと動かないな、という基本的な土台を築くことができました。

# 本作品制作時点での制作者の動作環境、および動作に使用したライブラリやBeatSaberのMOD
## 動作環境
- OS : Windows 11 Home 64bit
- CPU : Ryzen7 5700X 8C 16T
- GPU : AMD Radeon RX 7900XT 20GB OC
- RAM : DDR4 3200MHz 16GB × 2
- PSU : 850W 80 Plus Gold

## 使用ライブラリ
### java標準ライブラリ
- awt Font
-net URI
3, net URISyntaxException
### Javax
- swing JFrame
-swing JLabel
- swing JPanel
- swing BoxLayout
- swing SwingUtilities
### Gson
- com google gson
-com google gson annotations SerializedName
### WebSocket
- org java_websocket client WebSocketClient

- org java_websocket handshake ServerHandshake