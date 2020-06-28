package com.hcycom.sso.service.permissions.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hcycom.sso.dao.permissions.LoginLogDao;
import com.hcycom.sso.domain.LoginLog;
import com.hcycom.sso.domain.OnlineUser;
/*import com.hcycom.sso.redis.RedisService;
import com.hcycom.sso.redis.UserKey;*/
import com.hcycom.sso.service.permissions.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Date:Created
 */

@Service
public class OnlineUserServiceImpl implements OnlineUserService{
   /* @Autowired
    private RedisService redisService;*/
    @Autowired
    private LoginLogDao loginLogDao;
    @Override
    public List<OnlineUser> findAll(String keyWord, Map<String, String> map) {
        Set<OnlineUser> set = new HashSet<>();

        //获取所有登陆日志id
        Set<String> values = new HashSet<>();
        for (Map.Entry<String, String> vo : map.entrySet()) {
            String value = vo.getValue();
            //若value为null,不保存
            if(!StringUtils.isEmpty(value)){
                values.add(value);
            }
        }

        Map<String, Date> loginTimeMap=null;
        //获取所有登录用户的登录时间
        if(values.size()>0){
            List<String> list = new ArrayList<>();
            list.addAll(values);
            List<LoginLog> loginTimeBatch = loginLogDao.findLoginTimeBatch(list);
            //将list 转换成map
            loginTimeMap = loginTimeBatch.stream().collect(Collectors.toMap(LoginLog::getId, LoginLog::getStartTime));
        }

        List<OnlineUser> users = new ArrayList<>();
        for (String token : map.keySet()) {
            //**********String s = redisService.get(UserKey.token, token);
        	String s = "aaaaaaaaaaa";
            OnlineUser onlineUser = JSONObject.parseObject(s, OnlineUser.class);
            if(onlineUser!=null){
                //获取登录时间
                Date startTime = null;
                if(loginTimeMap==null){
                    startTime = new Date();
                }else{
                    startTime = loginTimeMap.get( map.get(token));
                }
                long time = startTime.getTime();
                long time1 = new Date().getTime();
                //判断时间差
                String distanceTime = getDistanceTime(time, time1);

                onlineUser.setLoginTime(startTime);
                onlineUser.setOnlineTime(distanceTime);
                set.add(onlineUser);
            }
        }

        //关键字查询 判断是否包含关键字
        if (!StringUtils.isEmpty(keyWord)) {
            String[] split = keyWord.split("\\s+");
            List<String> keyWords = Arrays.asList(split);
            for (OnlineUser onlineUser : set) {
                boolean flag = false;
                for (String word : keyWords) {
                    if (isContainsKeyWord(onlineUser, word)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    users.add(onlineUser);
                }
            }
        } else {
            users.addAll(set);
        }
        return users;

    }

    //判断是否包含关键字
    private boolean isContainsKeyWord(OnlineUser onlineUser, String keyWord) {
        if (onlineUser.toString().contains(keyWord)) {
            return true;
        }
        return false;
    }

    public  String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天"+hour + "小时"+min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时"+ min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒" ;
        return "0秒";
    }
}
