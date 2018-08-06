package cn.leo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            //Logger.d("from:" + json);
            return JSON.parseObject(json, classOfT);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object src) {
        String s = JSON.toJSONString(src, SerializerFeature.DisableCircularReferenceDetect);
        //Logger.d("to:" + s);
        return s;
    }
}
