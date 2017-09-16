import React,{ Component } from 'react';

function add0(m){
    return m<10?'0'+m:m;
}

export function formatDate(shijianchuo){
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

export function getDay(shijianchuo){
    var time = new Date(shijianchuo);
    var d = time.getDate();
    return d;
}

export function getYMD(shijianchuo){
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d);
}

export function getYMDW(shijianchuo, userName){
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var hour = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();

    var weekday = new Array(7);
    weekday[0] = "星期天";
    weekday[1] = "星期一";
    weekday[2] = "星期二";
    weekday[3] = "星期三";
    weekday[4] = "星期四";
    weekday[5] = "星期五";
    weekday[6] = "星期六";

    var wisper = '';
    if (hour < 6) { wisper = "凌晨好！"}
    else if (hour < 9) { wisper = "早上好！";}
    else if (hour < 12) {wisper = "上午好！";}
    else if (hour < 14) {wisper = "中午好！";}
    else if (hour < 17) {wisper = "下午好！";}
    else if (hour < 19) {wisper = "傍晚好！";}
    else if (hour < 22) {wisper = "晚上好！";}
    else {wisper = "深夜好！"}

    return y+'年'+add0(m)+'月'+add0(d)+'日' + ' ' + weekday[time.getDay()] + ' ' + userName + wisper;
}

export function formatDateZero(shijianchuo){
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d)+' 00:00:00';
}

export function formatDateEnd(shijianchuo){
    var time = new Date(shijianchuo);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d)+' 23:59:59';
}

export function fenToYuan(fen){
    if(isNaN(fen)){
        return '-';
    }
    return fen / 100;
}

export function fenToWan(fen){
    if(isNaN(fen)){
        return '-';
    }
    return fen / 100 / 10000;
}

