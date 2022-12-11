package com.yangyuxuan29.flower_identify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.yangyuxuan29.flower_identify.PhotoPreview.bitmap;

import static com.yangyuxuan29.flower_identify.PhotoPreview.flower_name;

public class ShowAnswer extends AppCompatActivity {

    private TextView mFlower;
    private ImageView mImgShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_answer);

        initView();  //初始化布局
        //initEvent(); //初始化事件
    }

    private void initView() {

        mFlower = (TextView) this.findViewById(R.id.text_view_flower_name);
        mImgShow = (ImageView) this.findViewById(R.id.img_show);

        mImgShow.setImageBitmap(bitmap);
        Intent intent = getIntent();
        String flower_name = intent.getStringExtra("flower_name");
        mFlower.setText(flower_name);
    }

    /*
    private void initEvent() {
    }
     */
}
