package com.kescoode.xmail.service.aidl;

/**
 * 与MailService交互的AIDL
 */
interface IRemoteMailService {

    /**
     * 邮件登录
     */
    void login(String email, String password);

}
