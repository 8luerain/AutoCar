package com.example.bluerain.carok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluerain.carok.base.CardListAdapter;
import com.example.bluerain.carok.enties.CarInfoBean;
import com.example.bluerain.carok.enties.CarPageInfo;
import com.example.bluerain.carok.enties.CardDateInfo;
import com.example.bluerain.carok.enties.Constance;
import com.example.bluerain.carok.enties.DetailCardInfo;
import com.example.bluerain.carok.enties.WeatherCardInfo;
import com.example.bluerain.carok.enties.WeatherXianXingPageInfo;
import com.example.bluerain.carok.event.OnItemClickEvent;
import com.example.bluerain.carok.service.LooperService;
import com.example.bluerain.carok.utils.CommonUtil;
import com.example.bluerain.carok.utils.DateUtils;
import com.example.bluerain.carok.utils.PollingUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

public class MainActivity extends AppCompatActivity {
    private RelativeLayout mActivityMain;

    private TextView mTextViewError;
    private ListView mLvMain;
    private CardListAdapter cardListAdapter;
    private WeatherCardInfo mWeatherCardInfo;

    private boolean isSetAlarm;

    /**
     * 限号
     */
    private String mLimitNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initData();
        initViews();
        startService(new Intent(this, LooperService.class));
    }

    private void initVariables() {
        mWeatherCardInfo = new WeatherCardInfo(null, null, null, null);
    }

    private void initData() {
        cardListAdapter = new CardListAdapter(this, null);
        cardListAdapter.setViewTypeCount(1);
    }

    private void initViews() {
        mActivityMain = (RelativeLayout) findViewById(R.id.activity_main);
        mTextViewError = (TextView) findViewById(R.id.txv_main_error);
        mLvMain = (ListView) findViewById(R.id.lv_main);
        mLvMain.setAdapter(cardListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeather();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onErrorUI();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final CarPageInfo carPageInfo = new Gson().fromJson(response.body().charStream(), CarPageInfo.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                refreshUI(carPageInfo);
                            } catch (Exception e) {
                                onErrorUI();
                            }
                        }
                    }
                });
            }
        });
    }


    private void loadWeather() {
        cardListAdapter.clear();
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
                .add(Constance.Location.KEY_LOCATION, Constance.Location.LOCATION)
                .add(Constance.Location.KEY_LATX, Constance.Location.LATX)
                .add(Constance.Location.KEY_LNGY, Constance.Location.LNGY)
                .build();
        final Request request = new Request.Builder().url(Constance.URL_XIAN_XING)
                .post(formBody)
                .addHeader("Origin", "https://api.accident.zhongchebaolian.com")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", "https://api.accident.zhongchebaolian.com/app_web/jsp/homepage.jsp")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final WeatherXianXingPageInfo weatherXianXingPageInfo = new Gson().fromJson(response.body().charStream(), WeatherXianXingPageInfo.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                refreshWeatherUI(weatherXianXingPageInfo);
                            } catch (Exception e) {
                            } finally {
                                loadData();
                            }
                        }
                    }
                });
            }
        });
    }

    private void refreshWeatherUI(WeatherXianXingPageInfo weatherXianXingPageInfo) {
        mLimitNum = weatherXianXingPageInfo.astrictno;
        mWeatherCardInfo.xianxing_today = weatherXianXingPageInfo.astrictno;
        mWeatherCardInfo.xianxing_tomorrow = weatherXianXingPageInfo.astrictmt;
        mWeatherCardInfo.weather = weatherXianXingPageInfo.weatherdes;
        mWeatherCardInfo.xiche = weatherXianXingPageInfo.washcar;
        cardListAdapter.add(0, mWeatherCardInfo);
        cardListAdapter.notifyDataSetChanged();
    }

    private void onErrorUI() {
        mTextViewError.setVisibility(View.VISIBLE);
        mTextViewError.setText("获取数据失败!");
    }


    private void refreshUI(CarPageInfo carPageInfo) {
        if (carPageInfo.isPageDataValid() && carPageInfo.isDataValid()) {
            for (CarInfoBean bean : carPageInfo.datalist) {
                boolean isValid = false;
                ArrayList<CardDateInfo> dateInfos = new ArrayList<>();
                for (CarInfoBean.CarapplyarrBean carapplyarr : bean.carapplyarr) {
                    final CardDateInfo cardDateInfo =
                            new CardDateInfo(carapplyarr.enterbjstart, carapplyarr.enterbjend, carapplyarr.status);
                    dateInfos.add(cardDateInfo);
                    isValid = isValid || cardDateInfo.isContainDate();
                }
                DetailCardInfo detailCardInfo = new DetailCardInfo(
                        bean.licenseno, isValid, CommonUtil.isXianXing(bean.licenseno, mLimitNum));// TODO: 17-2-8 undo
                detailCardInfo.timeGo = !isBusyTime();
                detailCardInfo.canSend = getSendString(bean);
                detailCardInfo.isAutoSend = getSharedPreferences(Constance.CONFIG_NAME, MODE_PRIVATE).getBoolean(DetailCardInfo.KEY_AUTO_SEND, false);
                setAutoSend(detailCardInfo.isAutoSend, dateInfos);
                detailCardInfo.mCardDateInfos = dateInfos;
                cardListAdapter.add(detailCardInfo);
            }
            cardListAdapter.notifyDataSetChanged();
        } else {
            mTextViewError.setVisibility(View.VISIBLE);
            mTextViewError.setText(carPageInfo.resdes);
        }
    }

    /**
     * 返回当前申请状态信息
     *
     * @param bean
     * @return
     */
    private String getSendString(CarInfoBean bean) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lastedTime = 0L;
        if (bean.canRequest()) {
            builder.append("当前可申请");
            return builder.toString();
        }
        for (CarInfoBean.CarapplyarrBean info : bean.carapplyarr) {
            try {
                if (!info.isPass()) {
                    builder.append("申请中..");
                    return builder.toString();
                }
                final long endTime = simpleDateFormat.parse(info.enterbjend).getTime();
                lastedTime = endTime > lastedTime ? endTime : lastedTime;
            } catch (ParseException e) {
                e.printStackTrace();
                builder.append("出错了");
                return builder.toString();
            }
        }

        int day = (int) ((lastedTime - System.currentTimeMillis()) / (1000 * 60 * 60 * 24));
        builder.append("[" + day + "]天后可再申请");
        return builder.toString();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnItemClickEvent event) {
        if (TextUtils.equals(getClass().getName(), event.key)) {
            DetailCardInfo info = (DetailCardInfo) event.data;
            setAutoSend(info.isAutoSend, info.mCardDateInfos);
        }
    }

    /**
     * 设定是够自动申请
     *
     * @param auto
     * @param dateInfos
     */
    private void setAutoSend(boolean auto, List<CardDateInfo> dateInfos) {
        if (auto) {
            setAlarm(dateInfos);
        } else {
            cancelAutoSend();
        }
    }

    /**
     * 设定自动申请
     *
     * @param dateInfos
     */
    private void setAlarm(List<CardDateInfo> dateInfos) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean ing = false;
        long lastedTime = 0L;
        for (CardDateInfo info : dateInfos) {
            try {
                final long endTime = simpleDateFormat.parse(info.endDate).getTime();
                lastedTime = endTime > lastedTime ? endTime : lastedTime;
                if (!info.isPass()) ing = true;
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "设定自动申请失败,服务器数据出错!", Toast.LENGTH_LONG).show();
            }
        }
        if (!ing) {
            long setTime = lastedTime + (1000 * 60 * 60 * 10); //截止当天,上午10点重新开始申请
            if (System.currentTimeMillis() < setTime) {
                final Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(setTime));
                PollingUtils.startService(this, instance, LooperService.class, LooperService.ACTION);
            }
        }

    }

    /**
     * 取消自动申请
     */
    private void cancelAutoSend() {
        PollingUtils.stopService(this, LooperService.class, LooperService.ACTION);
        isSetAlarm = false;
    }

    /**
     * 是否是高峰时段
     *
     * @return
     */
    private boolean isBusyTime() {
        return DateUtils.isBusyTime();
    }

}
