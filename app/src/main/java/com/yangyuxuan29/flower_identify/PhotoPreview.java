package com.yangyuxuan29.flower_identify;

import static com.yangyuxuan29.flower_identify.MainActivity.album;
import static com.yangyuxuan29.flower_identify.MainActivity.camera;
import static com.yangyuxuan29.flower_identify.MainActivity.flag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



//public class PhotoPreview extends AppCompatActivity {
public class PhotoPreview extends Activity {

    //private SmartImageView siv;
    private ImageView mPic;
    private Button mPicOk;
    //private Button mPicAgain;
    private Uri uri;
    private long firstPressedTime;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    public static Bitmap bitmap;
    public static String flower_name;
    public static final int CHANGE_UI = 1;
    public static Handler handler;
    //private LoadingView
    //图片路径
    //private String path;
    File file;


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            //super.onBackPressed();
            System.exit(0);
        } else {
            Toast.makeText(PhotoPreview.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initView();  //初始化布局
        initEvent(); //初始化事件

        //主线程设置消息处理机制
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                //super.handleMessage(msg);
                //System.out.println(msg.what);
                if (msg.what == CHANGE_UI) {
                    JSONObject result =(JSONObject) msg.obj;
                    try {
                        flower_name = result.getJSONArray("result").getJSONObject(0).getString("name");
                    }catch (Exception e){
                        System.out.println("数据格式错误："+e.getMessage());
                    }
                    //System.out.println(flower_name);
                    if (flower_name != null) {
                        Intent intent = new Intent(PhotoPreview.this, ShowAnswer.class);
                        intent.putExtra("flower_name", flower_name);
                        //avi.hide();  //关闭动画
                        //System.out.println(flower_name);
                        startActivity(intent);
                    }
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            uri = data.getData();
            //接收相册传递过来的图片
            mPic.setImageURI(uri);

            /**
             * uri转file
             */
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    private void initEvent() {

        //为“确定”按钮设置监听
        mPicOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果被点击，将图片信息上传到服务端，并且转至最后的结果显示界面
                /**
                 * 这一块还在测试阶段
                 */
                try {
                    //加载动画
                    //LoadingRenderer.Builder builder = new LoadingRenderer.Builder(context);
                    //LoadingView.setLoadingRenderer(builder.build());
                    //LoadingViewManager.with(PhotoPreview.this).setHintText("识别中…").build();
                    //上传图片
                    ImageUpload imageUpload = new ImageUpload();
                    imageUpload.run(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                //页面跳转
                //Intent intent = new Intent(PhotoPreview.this, ShowAnswer.class);
                //startActivity(intent);
                if (flower_name != null) {
                    Intent intent = new Intent(PhotoPreview.this, ShowAnswer.class);
                    intent.putExtra("flower_name", flower_name);
                    startActivity(intent);
                }

                 */


            }
        });

        /*
        //为“重拍”按钮设置监听
        mPicAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果按钮被点击，跳回拍照界面
                Intent intent = new Intent(PhotoPreview.this, TakePhoto.class);
                startActivity(intent);
                //finish();
            }
        });

         */
    }

    private void initView() {

        //绑定控件
        //siv = (SmartImageView) this.findViewById(R.id.siv_pic);
        mPic = (ImageView) this.findViewById(R.id.pic_imv);
        mPicOk = (Button) this.findViewById(R.id.picture_OK_btn);
        //avi = (AVLoadingIndicatorView) this.findViewById(R.id.avi);
        //mPicAgain = (Button) this.findViewById(R.id.picture_again_btn);

        //System.out.println(flag);
        //如果图片从相机处传来
        if (flag == camera) {
            //接收TakePhoto传递的图片
            Intent picIntent = getIntent();
            byte[] bytes = picIntent.getByteArrayExtra("pic_data");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                mPic.setImageBitmap(bitmap);
            }

        }
        //如果图片由相册传来
        else if (flag == album){

            System.out.println(uri);
            getPicFromAlbm();//调用相册
            System.out.println(uri);

        }

    }


    public void Show(){
        if (flower_name != null) {
            Intent intent = new Intent(PhotoPreview.this, ShowAnswer.class);
            intent.putExtra("flower_name", flower_name);
            startActivity(intent);
        }
    }




}
