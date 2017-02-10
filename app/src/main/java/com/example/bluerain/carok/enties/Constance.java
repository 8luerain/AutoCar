package com.example.bluerain.carok.enties;

/**
 * Created by bluerain on 17-2-8.
 */

public class Constance {

    //http://180.97.33.102/phpui2/?
    // c=limit&m=query&rp_format=pb&qt=carplatform&maptoken=9520927ee4aaa157cefd48c8b54223fd
    // &source=carplatform&mb=MI%20NOTE%20LTE&os=Android23&sv=9.7.1&net=1
    // &resid=01&cuid=2CEEA3F1696DD3AF04F1942089A345F9%7C0&bduid=&channel=1008585g
    // &oem=baidu&screen=%281080%2C1920%29&dpi=%28387%2C387%29&ver=1
    // &sinan=DQ83BJuBE_gzDgyNDZ6IE%2BuvW&co=&phonebrand=Xiaomi&patchver=
    // &isart=1&ctm=1486564644.571000&sign=5997d067499a200e29660848de872877;



    public static final String URL = "https://api.jinjingzheng.zhongchebaolian.com";

    public static final String URL_XIAN_XING = "https://api.accident.zhongchebaolian.com/app_web/mobile/standard/weather";

    public static final String PATH_GET_INFO = "/enterbj/platform/enterbj/entercarlist";

    public static final String CONFIG_NAME = "config";


    public static class USER {
        public static final String KEY_USER_ID = "userid";
        public static final String USER_ID = "1DD36174392C4DBB95941CEBFA60ADC6";
        public static final String KEY_APP_KEY = "appkey";
        public static final String APP_KEY = "kkk";
        public static final String KEY_DEVICE_ID = "deviceid";
        public static final String DEVICE_ID = "ddd";
        public static String KEY_TIME_STAMP = "timestamp";
        public static final long TIME_STAMP = 1486521100; //后三位000
        public static final String KEY_TOKEN = "token";
        public static final String TOKEN = "290EA66D9ECC4096D959C0BB4838B819";
    }

    public static class Location{
        public static final String KEY_LOCATION = "location";
        public static final String LOCATION = "1101";
        public static final String KEY_LATX = "latx";
        public static final String LATX = "40.032264";
        public static final String KEY_LNGY = "lngy";
        public static final String LNGY = "116.343895";
    }


}
