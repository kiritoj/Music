# Music
## 概述
* 项目整体采用MVVM架构以减少耦合度
* retrofit和rxjava配合网络请求
* eventbus作为各种状态的消息通知
* Glide加载图片，通过databinding注解@BindAdapter，自定义属性更方便的加载图片
* 使用leancloud作为后端支持，保存用户的个人信息，歌单，歌曲。下一次登录不会丢失数据
