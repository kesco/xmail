package com.kescoode.xmail.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.kescoode.xmail.R;
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

    }

}
