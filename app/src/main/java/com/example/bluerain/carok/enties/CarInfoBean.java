package com.example.bluerain.carok.enties;

import android.text.TextUtils;

import com.example.bluerain.carok.utils.DateUtils;

import java.util.List;

/**
 * Created by bluerain on 17-2-8.
 */
public class CarInfoBean {
    /**
     * carid : 9959049
     * userid : 1DD36174392C4DBB95941CEBFA60ADC6
     * licenseno : 豫AFQ852
     * applyflag : 1
     * applyid : 066201701311010068905627
     * carapplyarr : [{"applyid":"066201701311010068905627","carid":"9959049","cartype":"02","engineno":"7073368","enterbjend":"2017-02-08","enterbjstart":"2017-02-02","existpaper":"","licenseno":"豫AFQ852","loadpapermethod":"","remark":"57671ea25be9bc98eab766dc803e3bc2","status":"1","syscode":"","syscodedesc":"","userid":"1DD36174392C4DBB95941CEBFA60ADC6"}]
     */

    public String carid;
    public String userid;
    public String licenseno;
    public String applyflag;
    public String applyid;
    public List<CarapplyarrBean> carapplyarr;

    public boolean canRequest() {
        return TextUtils.equals("1", applyflag);
    }

    public static class CarapplyarrBean {
        /**
         * applyid : 066201701311010068905627
         * carid : 9959049
         * cartype : 02
         * engineno : 7073368
         * enterbjend : 2017-02-08
         * enterbjstart : 2017-02-02
         * existpaper :
         * licenseno : 豫AFQ852
         * loadpapermethod :
         * remark : 57671ea25be9bc98eab766dc803e3bc2
         * status : 1
         * syscode :
         * syscodedesc :
         * userid : 1DD36174392C4DBB95941CEBFA60ADC6
         */

        public String applyid;
        public String carid;
        public String cartype;
        public String engineno;
        public String enterbjend;
        public String enterbjstart;
        public String existpaper;
        public String licenseno;
        public String loadpapermethod;
        public String remark;
        public String status; // 1 审核通过  2: 审核中
        public String syscode;
        public String syscodedesc;
        public String userid;


        public boolean isPass() {
            return TextUtils.equals(status, "1");
        }

        public boolean valid() {
            final long start = DateUtils.parseDate2Long(enterbjstart);
            final long end = DateUtils.parseDate2Long(enterbjend);
            return TextUtils.equals(status, "1") && System.currentTimeMillis() > start && System.currentTimeMillis() < end;
        }
    }
}
