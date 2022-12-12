package com.baidu.aip.utills;

import android.graphics.Bitmap;
import android.util.Base64;
import okhttp3.*;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

class PlantIdentification {
    private static final String CLIENT_ID = "DrU9opD00wuQHg146YIIcRjv";
    private static final String CLIENT_SECRET = "mIzKf9Ofr4wcP1GkFzaGiSSntEIU9zrp";
    private static final String BAIDU_API_URL = "https://aip.baidubce.com";
    private static final OkHttpClient client = new OkHttpClient();
    private static JSONObject accessToken = null;

    /**
     * 获取访问令牌
     * @param client_id
     * @param client_secret
     * @return
     * @throws Exception
     */
    private static String getAccessToken(String client_id, String client_secret) throws Exception {
        JSONObject jsonObject = getAccessTokenObj(client_id, client_secret);
        String accessToken = jsonObject.getString("access_token");
        return accessToken;
    }

    /**
     * 获取访问令牌
     * @param client_id
     * @param client_secret
     * @return
     * @throws Exception
     */
    private static JSONObject getAccessTokenObj(String client_id, String client_secret) throws Exception {
        if (accessToken == null) {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder()
                    .url(BAIDU_API_URL + "/oauth/2.0/token?client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=client_credentials")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                accessToken = new JSONObject(json);
                if(accessToken.has("error_code")){
                    throw new Exception("发生错误：" + accessToken.get("error_code") + ":" + accessToken.get("error_msg"));
                }
            } else {
                throw new Exception("HTTP错误：" + response.code() + ":" + response.message());
            }
        }
        return accessToken;
    }

    /**
     * 将位图转为base64编码的字符串
     * @param bitmap
     * @return
     */
    private static String getImgBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bb = bos.toByteArray();
        String image = Base64.encodeToString(bb, Base64.NO_WRAP);
        return image;
    }

    /**
     * 识别图片
     * @param bitmap
     * @return
     * @throws Exception
     */
    public static JSONObject lookup(Bitmap bitmap) throws Exception {
        String access_token = getAccessToken(CLIENT_ID, CLIENT_SECRET);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String base64image = getImgBase64(bitmap);
        System.out.println("base64image:" + base64image);
        base64image = URLEncoder.encode(base64image, "UTF-8");
        System.out.println("base64image:" + base64image);
        RequestBody body = RequestBody.create(mediaType, "image=" + base64image);
        Request request = new Request.Builder()
                .url(BAIDU_API_URL + "/rest/2.0/image-classify/v1/plant?access_token=" + access_token)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.has("error_code")){
                throw new Exception("发生错误：" + jsonObject.get("error_code") + ":" + jsonObject.get("error_msg"));
            }
            return jsonObject;
        } else {
            throw new Exception("HTTP错误：" + response.code() + ":" + response.message());
        }
    }

}

