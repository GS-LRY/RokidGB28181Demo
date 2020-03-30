package com.rokid.glass.gb28181;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.rokid.glass.gb28181.mediacodec.GBMediaParam;

import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.SipParam;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ConnectActivity extends Activity implements EasyPermissions.PermissionCallbacks {



    private final String[] all_perms = {
            Manifest.permission.CAMERA,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };


    private SharedPreferences sharedPref;
    private TextView mTips;

    final Context mContext = this;
    private SipParam mSipParam;
    private GBMediaParam mGBMediaParam;
    private GB28281Engine mGB28181Engine;
    private String sipTips = "注册中...";
    @Override
    public void onStart() {
        super.onStart();

    }

    private void startApp(){
        setContentView(R.layout.activity_connect);
        mTips = findViewById(R.id.video_info);

/*     Settring activity
        PreferenceManager.setDefaultValues(mContext,R.xml.preferences,false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPref.edit().putBoolean(Settings.PREF_WLAN,true).commit();
        String server = sharedPref.getString("server",null);
        boolean iswifi = sharedPref.getBoolean("wlan",false);
*/
        mGBMediaParam =  new GBMediaParam.Builder()
                .setAudioBitRate(64000)
                .setAudioChannel(2)
                .setAudioChannelMode(AudioFormat.CHANNEL_IN_MONO)
                .setAudioFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioSampleRate(44100)
                .setVideoWidth(1280)
                .setVideoHeight(720)
                .setVideoBitRate(500*1000)  //bps
                .setVideoFrameRate(15)
                .setVideoGOP(2)
                .build();

        mSipParam = new SipParam.Builder()
                .setLocalPort("5060")
                .setProtocol("UDP")
                .setServerID("34020000002000000001")
                .setDomain("34020000")
                .setServer("10.88.1.136")
                .setServerPort("5060")
                .setUserName("34020000002000000002")
                .setFromuser("")
                .setOpen(true)
                .setPassword("12345678")
                .setHeartbeat("60")
                .setMaxHeartbeatTimeout("3")
                .setValidofregistration("3600")
                .setWlan(true)
                .build();

        mGB28181Engine = GB28281Engine.getInstance();
        mGB28181Engine.init(mContext,mSipParam,mGBMediaParam);
        mGB28181Engine.setSipStateCallback(new Receiver.SipStateCallback() {
            @Override
            public void changeStatus(Receiver.SipState state) {
                Log.i("GB28181Engine", "SipState" + state);
                switch (state){
                    case AVAILABLE:
                        sipTips = "注册成功，已在线，等待连接...";
                        break;
                    case IN_CALL:
                        sipTips = "正在视频直播...";
                        break;
                    case AWAY:
                        sipTips = "离线状态";
                        break;
                    case OFF_LINE:
                        sipTips = "离线";
                        break;
                    case HOLD:
                        sipTips = "挂断成功";
                        break;
                    case IDLE:
                        sipTips = "空闲状态...";
                        break;
                    case UNKNOWN:
                    default:
                        sipTips = "UNknow state";
                        break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mTips != null){
                            mTips.setText(sipTips);
                        }

                    }
                });
            }
        });
        mGB28181Engine.start();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (EasyPermissions.hasPermissions(this, all_perms)) {
            startApp();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.boot_request_permission),
                    0, all_perms);
        }

    }


    @Override
    protected void onDestroy() {
        Log.d("gb28181"," onDestroy, bye bye!!!!!!!");
        super.onDestroy();
        if(mGB28181Engine != null){
            mGB28181Engine.stop();
            mGB28181Engine = null;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.connect_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() ==  R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("gb28181","onPermissionsGranted, perms.size=" + perms.size());
        if (EasyPermissions.hasPermissions(this, all_perms)) {
            startApp();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e("gb28181","onPermissionsDenied");
        // Some permissions have been denied
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //处理权限名字字符串
            StringBuffer sb = new StringBuffer();
            for (String str : perms){
                sb.append(str);
                sb.append("\n");
            }
            sb.replace(sb.length() - 2,sb.length(),"");
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle(R.string.boot_request_permission)
                    .setRationale(String.format(getString(R.string.boot_request_permission_tips), sb))
                    .setPositiveButton(R.string.common_btn_ok)
                    .setNegativeButton(R.string.common_btn_cancel)
                    .build()
                    .show();
        }
        else if (!EasyPermissions.hasPermissions(this, all_perms)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
