package com.yangyuxuan29.flower_identify;

import android.graphics.Bitmap;
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
import org.json.JSONObject;

import java.net.URLEncoder;

class ImageUpload {

    public void run(Bitmap img) throws Exception {
        final Bitmap bitmap = img;
        new Thread() {
            @Override
            public void run() {
                try {
                    //模拟数据
                    JSONObject result = new JSONObject("{\"result\":[{\"score\":0.06332461,\"name\":\"非植物\"}],\"log_id\":1602182395949811198}");
//                  //调用百度api的结果数据
//                  JSONObject result = PlantIdentification.lookup( bitmap);
                    System.out.println("运行成功：" + result);

                    //将更改UI的消息发给主线程
                    //System.out.println("进入上传！！");
                    Message msg = new Message();
                    msg.what = CHANGE_UI;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    //TODO 错误处理
                    System.out.println("运行识别：" + e.getMessage());
                    e.printStackTrace();
                }


            }
        }.start();
        //PhotoPreview.Show();

        //return result;
    }
}

