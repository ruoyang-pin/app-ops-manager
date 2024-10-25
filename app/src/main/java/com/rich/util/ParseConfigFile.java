package com.rich.util;

import android.content.Context;
import com.google.common.collect.Maps;
import com.rich.R;
import com.rich.domain.ConfigBean;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author rich
 * @date 2024/10/25
 * @description
 */

public class ParseConfigFile {

    public static volatile Map<String, ConfigBean> CONFIG_BEAN_MAP;

    public static Map<String, ConfigBean> getInstance(Context context) {
        if (CONFIG_BEAN_MAP == null) {
            synchronized (ParseConfigFile.class) {
                if (CONFIG_BEAN_MAP == null) {
                    CONFIG_BEAN_MAP = queryConfig(context);
                }
            }
        }
        return CONFIG_BEAN_MAP;
    }


    @SneakyThrows
    public static Map<String, ConfigBean> queryConfig(Context context) {
        JSONObject fileJsonObject = LoadJsonFile.parseJson(context, R.raw.config);
        if (fileJsonObject == null) {
            return null;
        }
        JSONArray jsonArray = fileJsonObject.getJSONArray("apps");
        Map<String, ConfigBean> result = Maps.newHashMap();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i); // 使用 optJSONObject 可以避免异常
            if (jsonObject != null) {
                ConfigBean configBean = new ConfigBean();
                configBean.setPackageName(jsonObject.optString("packageName"));
                configBean.setViewId(jsonObject.optString("viewId"));
                ConfigBean.PixelCoordinates pixelCoordinates = new ConfigBean.PixelCoordinates();
                JSONObject coordinates = jsonObject.getJSONObject("coordinates");
                pixelCoordinates.setX(coordinates.optInt("x"));
                pixelCoordinates.setY(coordinates.optInt("y"));
                configBean.setLocation(pixelCoordinates);
                result.put(configBean.getPackageName(), configBean);
            }
        }
        return result;
    }


}
