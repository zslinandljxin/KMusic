package zsl.zhaoqing.com.kmusic.interfaces;

/**
 * Created by Administrator on 2016/1/14.
 */
public interface HttpCallback {

    void onSuccess(String result);
    void onFailure(Exception e);
}
