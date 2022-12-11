package com.yangyuxuan29.flower_identify;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yangyuxuan29.flower_identify.PhotoPreview.CHANGE_UI;
import static com.yangyuxuan29.flower_identify.PhotoPreview.flower_name;
import static com.yangyuxuan29.flower_identify.PhotoPreview.handler;

import com.baidu.aip.utills.FileUtil;
import com.baidu.aip.utills.HttpUtil;
import java.net.URLEncoder;

class ImageUpload {


        private static final String IMGUR_CLIENT_ID = "123";
        private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        private String result;
        private final OkHttpClient client = new OkHttpClient();
        public Handler mHandler;

        public void run(File f) throws Exception {
            final File file=f;
            new Thread() {
                @Override
                public void run() {
                    //子线程需要做的工作
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("title", "Square Logo")
                            .addFormDataPart("file", UUID.randomUUID().toString()+".png",
                                    RequestBody.create(MEDIA_TYPE_PNG, file))
                            .build();
                    //设置为自己的ip地址
                    Request request = new Request.Builder()
                            .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                            .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/plant")
                            .post(requestBody)
                            .build();
                    //System.out.println("到这里了");
                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        //System.out.println(response.body().string());
                        flower_name = response.body().string();
                        //System.out.println(flower_name);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //将更改UI的消息发给主线程
                    //System.out.println("进入上传！！");
                    Message msg = new Message();
                    msg.what = CHANGE_UI;
                    //Handler handler = new PhotoPreview().handler();
                    //PhotoPreview photoPreview = new PhotoPreview();
                    handler.sendMessage(msg);

                }
            }.start();
            //PhotoPreview.Show();

            //return result;
        }
    }

