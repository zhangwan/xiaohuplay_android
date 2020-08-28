package com.tinytiger.common.http.downloadFile;

/**
 * Created By zwy
 * On 2020/6/5
 */
public interface JsDownloadListener {
    void onStartDownload(long length);

    void onProgress(int progress);

    void onFail(String errorInfo);

    void onFinishDownload();
}
