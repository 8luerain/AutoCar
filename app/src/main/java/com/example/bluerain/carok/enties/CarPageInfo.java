package com.example.bluerain.carok.enties;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by bluerain on 17-2-8.
 */

public class CarPageInfo {


    /**
     * datalist : [{"carid":"9959049","userid":"1DD36174392C4DBB95941CEBFA60ADC6","licenseno":"豫AFQ852","applyflag":"1","applyid":"066201701311010068905627","carapplyarr":[{"applyid":"066201701311010068905627","carid":"9959049","cartype":"02","engineno":"7073368","enterbjend":"2017-02-08","enterbjstart":"2017-02-02","existpaper":"","licenseno":"豫AFQ852","loadpapermethod":"","remark":"57671ea25be9bc98eab766dc803e3bc2","status":"1","syscode":"","syscodedesc":"","userid":"1DD36174392C4DBB95941CEBFA60ADC6"}]}]
     * rescode : 200
     * resdes : 获取申请信息列表成功
     */

    public String rescode;
    public String resdes;
    public List<CarInfoBean> datalist;

    public boolean isPageDataValid() {
        return TextUtils.equals(rescode, "200");
    }

    public boolean isDataValid() {
        return datalist != null && datalist.size() != 0;
    }

}
