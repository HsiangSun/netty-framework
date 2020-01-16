package cn.hsiangsun.response;

/*
*
* 简陋响应
* todo 添加http状态
* */
public interface Response {

    void write(String responseMsg);

    void closeChannel();

}
