package com.example.music.util.downloadutil;

//定义一个回调接口，用于在服务中更新通知
public interface DownloadLintener {
    void onProgress(int progress);//表示正在下载
    void onSuccess();//下载成功
    void onFailed();//下载失败
    void onPause();//暂停下载
    void onCancle();//取消下载
}
