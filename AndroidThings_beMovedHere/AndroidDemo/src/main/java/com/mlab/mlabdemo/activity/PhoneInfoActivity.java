package com.mlab.mlabdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhoneInfoActivity extends Activity implements View.OnClickListener {

    /* 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /* wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /* 2G网络 */
    public static final int NETWORKTYPE_2G = 2;
    /* 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 3;
    /* wifi网络 */
    public static final int NETWORKTYPE_WIFI = 4;

    private TextView showTxt;
    private Button btn_back;
    TelephonyManager tm;

    MyPhoneStateListener MyListener;
    String GSM; //蜂窝网络强度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_phoneinfo);

        /* Update the listener, and start it  */
        MyListener = new MyPhoneStateListener();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        Button btn = (Button) findViewById(R.id.id_btn_info);
        Button btn_back = (Button) findViewById(R.id.id_btn_back);
        showTxt = (TextView) findViewById(R.id.id_showTxt);
        btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    /* Called when the application is minimized */
    @Override
    protected void onPause() {
        super.onPause();
        tm.listen(MyListener, PhoneStateListener.LISTEN_NONE);
    }

    /* Called when the application resumes */
    @Override
    protected void onResume() {
        super.onResume();
        tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_info:
                String IMEI = tm.getDeviceId();
                String IMSI = tm.getSubscriberId();
                String netType;
                switch (getNetWorkType(getApplicationContext())) {
                    case 0:
                        netType = "没有网络";
                        break;
                    case 1:
                        netType = "wap网络";
                        break;
                    case 2:
                        netType = "2G网络";
                        break;
                    case 3:
                        netType = "3G和3G以上网络，或统称为快速网络";
                        break;
                    case 4:
                        netType = "wifi网络";
                        break;
                    default:
                        netType = "没有网络";
                        break;
                }
                String str = "\n获取 →\n\nIMEI: " + IMEI + "\n\nIMSI: " + IMSI + "\n\n蜂窝网络强度 " + GSM + "\n\n当前网络类型: " + netType + "\n";
                showTxt.setText(str);
                break;

            case R.id.id_btn_back:
//                Toast.makeText(this,"sss",Toast.LENGTH_LONG).show();
                PhoneInfoActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /* Start the PhoneState listener */
    private class MyPhoneStateListener extends PhoneStateListener {

        /* Get the Signal strength from the provider, each tiome there is an update  从得到的信号强度,每个tiome供应商有更新*/
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            GSM = "GSM Cinr = " + String.valueOf(signalStrength.getGsmSignalStrength());
        }
    }/* End of private Class */

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     * 实现判断是wifi还是移动网络
     *
     * @param context 上下文
     * @return int 网络状态
     * {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},
     * {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}
     * {@link #NETWORKTYPE_WIFI}
     */
    public static int getNetWorkType(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                return TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            return NETWORKTYPE_INVALID;
        }
        return NETWORKTYPE_INVALID;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }
}
