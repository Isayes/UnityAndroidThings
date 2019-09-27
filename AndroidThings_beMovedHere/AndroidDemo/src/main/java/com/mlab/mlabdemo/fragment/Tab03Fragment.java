package com.mlab.mlabdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mlab.mlabdemo.activity.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tab03Fragment extends Fragment {
    private TextView tv;
    private Button refresh;
    private View view;
    private Handler handler;
    private TextView tv_ip;
    private TextView tv_isp;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) view.findViewById(R.id.iptext);
        refresh = (Button) view.findViewById(R.id.id_btn_refresh);
        tv.setText(getPsdnIp());
        GetIP();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "---    请稍候    ---", Toast.LENGTH_LONG).show();
                tv.setText(getPsdnIp());
                GetIP();
            }
        });
        tv_ip = (TextView) view.findViewById(R.id.tv_ip);
        tv_isp = (TextView) view.findViewById(R.id.tv_isp);
        handler = new Handler() {
            //重写消息队列方法
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                tv_ip.setText(text.split("-")[0]);
                tv_isp.setText(text.split("-")[1]);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_tab03, null);
        return view;
    }

    public void GetIP() {
        final String path = "http://cmyip.com/index.php";
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //使用网址创建URL
                    URL url = new URL(path);
                    //获取连接对象做设置
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    conn.setRequestMethod("GET");
                    //设置超时时间
                    conn.setConnectTimeout(8000);
                    //设置读取超时的时间
                    conn.setReadTimeout(8000);
                    //连接
                    conn.connect();
                    //连接成功
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String text = StreamTool.getTextFromStream(is);
                        String ip = null;
                        String isp = null;
                        Pattern pattern = Pattern.
                                compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                        Matcher matcher = pattern.matcher(text);
                        if (matcher.find()) {
                            ip = matcher.group();
                        }

                        isp = text.substring(text.indexOf("ISP: "), text.indexOf("Corporation "));
                        text = ip + "-" + isp;
                        Message msg = handler.obtainMessage();
                        msg.obj = text;
                        handler.sendMessage(msg);

                    } else if (conn.getResponseCode() == 404) {

                    }
                } catch (MalformedURLException e) {
                    System.out.println("MalFormed Exception=========================");
                } catch (IOException e) {
                    System.out.println("IOException ================================");
                }
            }
        };
        thread.start();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    Log.d("DEBUG", inetAddress.getHostAddress().toString());
                    if (!inetAddress.isLoopbackAddress()) {
                        // return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     *
     * @return
     * @author SHANHY
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static class StreamTool {
        public static String getTextFromStream(InputStream is) {
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                //利用字节数组输出流转换成字节数组，然后用字节数组构造成为一个字符串
                String text = new String(bos.toByteArray());
                return text;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}