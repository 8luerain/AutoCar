package com.example.bluerain.carok.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.bluerain.carok.MainActivity;
import com.example.bluerain.carok.R;
import com.example.bluerain.carok.enties.CarInfoBean;
import com.example.bluerain.carok.enties.CarPageInfo;
import com.example.bluerain.carok.enties.Constance;
import com.example.bluerain.carok.utils.PollingUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bluerain on 17-2-10.
 */

public class LooperService extends Service {

    private static final String TAG = "LooperService";

    public static final String ACTION = "com.example.bluerain.carok.service.LooperService";

    private static final int RETRY_TIME = 5;

    public static final int STATE_SEND = 1;
    public static final int STATE_CHECK = 2;

    private int checkState = STATE_SEND; //当前循环是检查是否可申请 还是是否申请成功

    private int retry;
    private int check_retry;
    private long nextTime;

    private CarInfoBean mCurrentCarInfo;
    private NotificationManager mNotificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action = intent.getStringExtra("action");
        if (TextUtils.equals(action, "start")) {
            loadData();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        startService(new Intent(this, LooperService.class));
        super.onDestroy();
    }

    private void loadData() {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .build();
        //忽略证书

        FormBody formBody = new FormBody.Builder()
                .add(Constance.USER.KEY_APP_KEY, Constance.USER.APP_KEY)
                .add(Constance.USER.KEY_USER_ID, Constance.USER.USER_ID)
                .add(Constance.USER.KEY_DEVICE_ID, Constance.USER.DEVICE_ID)
                .add(Constance.USER.KEY_TOKEN, Constance.USER.TOKEN)
                .add(Constance.USER.KEY_TIME_STAMP, System.currentTimeMillis() + "000")
                .add("appsource", "")
                .build();
        final Request request = new Request.Builder().url(Constance.URL + Constance.PATH_GET_INFO)
                .post(formBody)
                .addHeader("Origin", Constance.URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", "https://api.jinjingzheng.zhongchebaolian.com/enterbj/jsp/enterbj/index.jsp")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onError();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final CarPageInfo carPageInfo = new Gson().fromJson(response.body().charStream(), CarPageInfo.class);
                if (response.isSuccessful()) {
                    try {
                        checkIfNeed(carPageInfo);
                    } catch (Exception e) {
                        onError();
                    }
                }
            }
        });
    }

    private void onError() {
        sendNotification("失败了!!!!");
    }

    private void checkIfNeed(CarPageInfo carPageInfo) {
        if (carPageInfo.isPageDataValid() && carPageInfo.isDataValid()) {
            for (CarInfoBean carInfoBean : carPageInfo.datalist) {
                if (carInfoBean.canRequest()) {//可申请
                    mCurrentCarInfo = carInfoBean;
                    sendRequest(carInfoBean);
                } else {
                    //暂时不可申请,1小时后重试
                    if (checkState == STATE_SEND) {
                        retrySend();
                    } else if (checkState == STATE_CHECK) {
                        retryCheck(carInfoBean);
                    }
                }
            }
        }
    }

    private void sendRequest(CarInfoBean carInfoBean) {
        //发送后续一系列请求
        retry = 0;
        // TODO: 17-2-10 add

        sendNotification("已提交申请!!");

        //检查是够申请成功
        checkState = STATE_CHECK;
        retryCheck(carInfoBean);

    }


    private void retrySend() {
        if (++retry > 5) {
            retry = 0;
            sendNotification("试了5次都失败了,快来手动申请吧");
            return; //最多重试5次
        }
        sendNotification("第(" + retry + ")次申请未开放,一小时后重试");
        nextTime = (System.currentTimeMillis() + (10 * 1000));//一小时后重试
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(nextTime));
        PollingUtils.startService(this, calendar, LooperService.class, ACTION);
    }

    private void retryCheck(CarInfoBean carInfoBean) {
        if (isSuccess(carInfoBean)) {
            sendNotification("恭喜恭喜,审核通过,开始嗨皮吧!!");
            check_retry = 0;
            checkState = STATE_SEND;
            return;
        }
        if (++check_retry > 5) {
            sendNotification("咋一直审核不通过呢??");
            check_retry = 0;
            checkState = STATE_SEND;
            return; //最多重试5次
        }
        sendNotification("第(" + check_retry + ")次检查是否审核通过");
        nextTime = (System.currentTimeMillis() + (20 * 1000));//一小时后重试
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(nextTime));
        PollingUtils.startService(this, calendar, LooperService.class, ACTION);
    }


    private void sendNotification(String message) {
        final Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.directions).setTicker("testNote")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentTitle("进京证申请")
                    .setContentText(message)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
            mNotificationManager.notify(1, notification);
        }
    }


    private boolean isSuccess(CarInfoBean bean) {
        for (CarInfoBean.CarapplyarrBean carapplyarrBean : bean.carapplyarr) {
            if (!carapplyarrBean.isPass()) return false;
        }
        return true;
    }
}
