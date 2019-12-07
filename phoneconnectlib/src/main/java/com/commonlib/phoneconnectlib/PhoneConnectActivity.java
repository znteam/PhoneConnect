package com.commonlib.phoneconnectlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.commonlib.phoneconnectlib.adapter.PhoneConnectRvAdapter;
import com.commonlib.phoneconnectlib.bean.PhoneConnectUserBean;
import com.commonlib.phoneconnectlib.itf.OnItemPosClickListener;
import com.commonlib.refreshdatalayoutlib.RefreshDataLayout;

import java.util.ArrayList;
import java.util.List;


public class PhoneConnectActivity extends Activity implements View.OnClickListener  {

    RefreshDataLayout refreshDataLayout;
    TextView countTv;
    TextView sendSmsTv;
    TextView permissionTv;
    LinearLayout contentLayout;
    LinearLayout permissionLayout;

    private PhoneConnectRvAdapter adapter;
    private String smsContent;

    private static final String INTENT_EXTRA_DATA = "data";

    public static void startActivity(Context context, String smsContent) {
        Intent intent = new Intent(context, PhoneConnectActivity.class);
        intent.putExtra(INTENT_EXTRA_DATA, smsContent);
        if (!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_connect);

        refreshDataLayout = findViewById(R.id.refresh_data_layout);
        countTv = findViewById(R.id.count_tv);
        sendSmsTv = findViewById(R.id.send_sms_tv);
        permissionTv = findViewById(R.id.permission_tv);
        contentLayout = findViewById(R.id.content_layout);
        permissionLayout = findViewById(R.id.permission_layout);

        smsContent = getIntent().getStringExtra(INTENT_EXTRA_DATA);
        getPermission();

        adapter = new PhoneConnectRvAdapter();
        adapter.setListener(new OnItemPosClickListener<PhoneConnectUserBean>() {
            @Override
            public void onClick(PhoneConnectUserBean data, int pos) {
                countTv.setText(adapter.getSelectedCount() + "");
            }
        });
        refreshDataLayout.setLayoutManager(new LinearLayoutManager(this));
        refreshDataLayout.setAdapter(adapter);
        refreshDataLayout.setFailInfo("无联系人");
        refreshDataLayout.setOnDataListener(new RefreshDataLayout.OnDataListener() {
            @Override
            public void refreshData() {
                getPhoneConnectUser();
            }

            @Override
            public void loadMoreData() {
            }
        });
    }

    private void getPhoneConnectUser() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            List<PhoneConnectUserBean> userList = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    userList.add(new PhoneConnectUserBean(displayName, number));
                }
            }
             setPhoneConnectUser(userList);
        } catch (Exception e) {
            e.printStackTrace();
            setPhoneConnectUser(null);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void setPhoneConnectUser(List<PhoneConnectUserBean> userList) {
        adapter.setData(userList);
        refreshDataLayout.setOverFlag(true);
        refreshDataLayout.notifyRefresh();
    }


    private void toSendSms() {
        List<PhoneConnectUserBean> userList = adapter.getSelectedData();
        if (userList == null || userList.size() == 0) {
            Toast.makeText(this, "请选择联系人", Toast.LENGTH_SHORT).show();
            return;
        }
        String number = "";
        for (PhoneConnectUserBean user : userList) {
            number += user.getPhone() + ";";
        }

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");//必须指定type
        smsIntent.putExtra("address", number);//address字段不能改
        smsIntent.putExtra("sms_body", smsContent);//sms_body 不能改
        startActivity(smsIntent);
    }

    private void getPermission() {
        /*Acp.getInstance(MyApplication.getInstance()).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_CONTACTS).build(), new AcpListener() {
            @Override
            public void onGranted() {
                permissionLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
                refreshDataL.startRefresh();
            }

            @Override
            public void onDenied(List<String> permissions) {
                permissionLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.permission_tv) {
            getPermission();
        } else if (i == R.id.send_sms_tv) {
            toSendSms();
        }
    }
}
