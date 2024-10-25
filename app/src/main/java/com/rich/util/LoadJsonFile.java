package com.rich.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author rich
 * @date 2024/10/25
 * @description
 */
public class LoadJsonFile {

    public static JSONObject parseJson(Context context, int fileId) {
        try {
            // 获取资源
            Resources resources = context.getResources();
            // 打开 raw 资源
            InputStream inputStream = resources.openRawResource(fileId); // data.json
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            // 解析 JSON
            String jsonString = stringBuilder.toString();
            return new JSONObject(jsonString);

        } catch (IOException e) {
            Log.e("JsonParser", "Error reading JSON file", e);
        } catch (JSONException e) {
            Log.e("JsonParser", "Error parsing JSON", e);
        }
        return null;
    }
}
