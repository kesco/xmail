package com.kescoode.xmail.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.domain.LocalAttachment;
import com.kescoode.xmail.domain.MailBuilder;
import com.kescoode.xmail.ui.activity.internal.MailConnActivity;

public class MailOperationActivity extends MailConnActivity {
    private static final int OPERATION_ERROR = -1;
    public static final int OPERATION_WRITE = 0;

    public static void startMailOperation(Context context, int type) {
        Intent intent = new Intent(context, MailOperationActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_operation);

        int type = getIntent().getIntExtra("type", OPERATION_ERROR);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailBuilder builder = new MailBuilder();
                builder.setSubject("Test")
                        .setContent("test")
                        .setToList("bringwin808@foxmail.com")
                        .addAttachment("/sdcard/test.json");
                try {
                    mailService.sendMail(1, builder);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
//                LocalAttachment attachment = new LocalAttachment(MailOperationActivity.this,
//                        "/sdcard/test.json");
//                Logger.e("HAHA", attachment.toString());
            }
        });
    }

}
