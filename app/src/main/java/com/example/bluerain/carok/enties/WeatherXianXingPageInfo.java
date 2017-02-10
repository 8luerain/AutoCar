package com.example.bluerain.carok.enties;

import java.util.List;

/**
 * Created by bluerain on 17-2-9.
 */

public class WeatherXianXingPageInfo {


    /**
     * cityname : 北京市
     * date : 02月09日
     * week : 周四
     * daypicurl : http://api.map.baidu.com/images/weather/day/qing.png
     * nightpicurl : http://api.map.baidu.com/images/weather/night/qing.png
     * weathersection : 3 ~ -5℃
     * weatherdes : 晴
     * washcar : 较不宜
     * dayornight : 1
     * weathercode : 0
     * astrictno : 2和7
     * astrictmt : 3和8
     * instructionsurl : http://static.zhongchebaolian.com:80/photobase/photos/notice/instructions.html
     * graphicaldutyurl : http://static.zhongchebaolian.com:80/photobase/photos/notice/graphicalduty.html
     * tgResult : [{"dataurl":"https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15735","title":"关于海淀区韦伯豪家园北侧路（民族大学西路至韦伯时代中心西侧道路之间路段）采取交通管理措施的通告"},{"dataurl":"https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15734","title":"关于朝阳区团结湖中路采取交通管理措施的通告"},{"dataurl":"https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15733","title":"关于朝阳区团结湖南路采取交通管理措施的通告"},{"dataurl":"https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15731","title":"关于2017年春节期间对厂甸庙会大观园庙会周边道路采取临时交通管理措施的通告"},{"dataurl":"https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15730","title":"关于2017年春节期间对白云观周边道路采取临时交通管理措施的通告"}]
     * rescode : 200
     * resdes : 获取天气信息成功
     */

    public String cityname;
    public String date;
    public String week;
    public String daypicurl;
    public String nightpicurl;
    public String weathersection;
    public String weatherdes;
    public String washcar;
    public String dayornight;
    public String weathercode;
    public String astrictno; //今天限行
    public String astrictmt; //明日限行
    public String instructionsurl;
    public String graphicaldutyurl;
    public String rescode;
    public String resdes;
    public List<TgResultBean> tgResult;

    public static class TgResultBean {
        /**
         * dataurl : https://web.jiaoguandongtai.zhongchebaolian.com/ssmframe/jtadvisement/detailinfo.do?id=15735
         * title : 关于海淀区韦伯豪家园北侧路（民族大学西路至韦伯时代中心西侧道路之间路段）采取交通管理措施的通告
         */

        public String dataurl;
        public String title;
    }
}
