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
            "    \"settings\": [\n" +
            "      {\n" +
            "        \"send\": {\n" +
            "          \"type\": \"SMTP\",\n" +
            "          \"server\": \"smtp.126.com\",\n" +
            "          \"port\": 465,\n" +
            "          \"security\": \"ssl\"\n" +
            "        },\n" +
            "        \"receive\": {\n" +
            "          \"type\": \"POP3\",\n" +
            "          \"server\": \"pop.126.com\",\n" +
            "          \"port\": 995,\n" +
            "          \"security\": \"ssl\"\n" +
            "        },\n" +
            "        \"use_suffix\": false,\n" +
            "        \"default\": 1\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    private EmailConfig config;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        JSONObject jObj = new JSONObject(json);
        config = new EmailConfig(jObj);
    }

    public void testConstructor() throws Exception {
        assertNotNull("Mail Configure constructor", config);
    }

    public void testSubDomain() throws Exception {
//        String smtp = config.
    }
}
