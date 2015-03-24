package com.kescoode.xmail.domain;

import android.test.AndroidTestCase;
import org.json.JSONObject;

/**
 * 关于{@link EmailConfig}的单元测试
 *
 * @author Kesco Lin
 */
public class EmailConfigTest extends AndroidTestCase {
    private static final String json = "{\n" +
            "    \"domain\": \"126.com\",\n" +
            "    \"send_server\": \"smtp.126.com\",\n" +
            "    \"send_port\": 465,\n" +
            "    \"receive_server\": \"pop.126.com\",\n" +
            "    \"receive_port\": 995,\n" +
            "    \"use_ssl\": true,\n" +
            "    \"use_suffix\": false\n" +
            "  }";

    public void testConstructor() throws Exception {
        JSONObject jObj = new JSONObject(json);
        EmailConfig config = new EmailConfig(jObj);
        assertNotNull("Mail Configure constructor", config);
    }
}
