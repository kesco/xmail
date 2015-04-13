package com.kescoode.xmail.service.aidl;

import com.kescoode.xmail.domain.MailBuilder;

/**
 * 与MailService交互的AIDL
 */
interface IRemoteMailService {

    /**
     * 邮件登录
     */
    void login(String email, String password);

    /**
     * 更新邮箱
     */
    void syncAll(long accountId);

    /**
     * 更新文件夹
     */
    void syncFolder(long accountId, String folder, int page);

    /**
     * 发送邮件
     */
    void sendMail(long accountId, in MailBuilder builder);

}
