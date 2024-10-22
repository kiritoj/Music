# Music
## 概述
* 项目整体采用MVVM架构以减少耦合度
* retrofit和rxjava配合网络请求
* eventbus作为各种状态的消息通知
* Glide加载图片，通过databinding注解@BindAdapter，自定义属性更方便的加载图片
* 使用leancloud作为后端支持，保存用户的个人信息，歌单，歌曲。下一次登录不会丢失数据
## 图片
<div align="center">
<img src="http://ww1.sinaimg.cn/mw690/006nwaiFly1g6b8vjl7t2g30a00i6kjl.gif" height="400" width="210" >
<img src="http://ww1.sinaimg.cn/mw690/006nwaiFly1g6b9ldi8mmg30a00i6b2a.gif" height="400" width="210" >
<img src="https://i.loli.net/2019/08/27/neursR2y4iKBhY8.png" height="400" width="210" >
<img src="https://i.loli.net/2019/08/30/U5fQegZyW8GzouT.png" height="400" width="210">

<img src="http://ww1.sinaimg.cn/large/006nB4gFly1g6pmkpeip6j30u01o048d.jpg" height="400" width="210">
<img src="http://ww1.sinaimg.cn/large/006nB4gFly1g6q9w0va2nj30u01o0doy.jpg" height="400" width="210">
<img src="http://ww1.sinaimg.cn/large/006nB4gFly1g6ufkgnh9tj30u01o0k4c.jpg" height="400" width="210">
<img src="http://ww1.sinaimg.cn/large/006nB4gFly1g6ufltuw02j30u01o0wmi.jpg" height="400" width="210">

<img src="http://ww1.sinaimg.cn/mw690/006nB4gFly1g71hyky2x0j30de0m5gr9.jpg" height="400" width="210">
<img src="http://ww1.sinaimg.cn/mw690/006nB4gFly1g71i3otgz4j30u01o0tjc.jpg" height="400" width="210">
</div>




## 鸣谢
### API
* [messapi](https://github.com/messoer)
* [NeteaseCloudMusicApi](https://binaryify.github.io/NeteaseCloudMusicApi/#/?id=neteasecloudmusicapi)

## 更新日志
### 8.29
  由于前一个Api的作者被人举报（心疼1秒），接口已失效，接下来开始苦逼的换接口
### 8.30
  新增滚动歌词显示
### 8.31
* 调整数据库歌曲与歌单表的关系，多对多关系。实现同一首歌可收藏至多个歌单
### 9.1
* 优化顶部banner点击事件，根据banner种类可选择：跳转至网页/播放音乐/打开歌单 <br>
* toolbal显示webview加载的地址和进度
### 9.2
* 接口全面废弃。。。
### 9.4
* 更换banner接口，更换新歌速递接口。优化播放种类来源，解决歌曲到歌单的添加与删除bug<br>
* 更换歌单接口，歌词接口暂时无法使用。修复切换播放队列时播放图标仍然一致的问题
### 9.6
* 新增歌曲/歌单/专辑评论列表，歌单接口又坏了，手动(*￣︶￣)
### 9.7
* 新增弹出播放列表，精简部分代码
### 9.9
* 新增排行榜
### 9.10
* 解决了快速二次点击歌曲进入播放界面无法播放的问题
* 首页增加加载失败提示
* 点击歌曲底部播放栏立即出现、切换音乐立即归零总进度和当前进度，不再等待player准备好以后
* 增加音乐缓冲提示，缓冲期间禁止滑动seekbar
### 9.13
* 更换歌词接口，恢复歌词显示
### 9.15
* 新增mv播放
### 9.16
* 新增前台播放服务
* mv评论接口又挂了
### 9.23
* 下载单曲
* 收藏mv
* 更换baseurl
