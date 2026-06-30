# 制作しようと思い立った経緯
BeatSaberのMODにてコンボ表示やカットノーツ数を表示するものがあったが、外部ソフトとして配布されているものが無く、自分としてはとても欲しいツールだなと思い立ったため制作開始。
本作品を機にJavaについてもっと知ることができればいいなと思い制作いたしました。

# 本作品のこだわり


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
- net URI
3, net URISyntaxException
### Javax
- swing JFrame
- swing JLabel
- swing JPanel
- swing BoxLayout
- swing SwingUtilities
### Gson
- com google gson
- com google gson annotations SerializedName
### WebSocket
- org java_websocket client WebSocketClient
- org java_websocket handshake ServerHandshake

### BeatSaber MOD
- HttpSiraStatus