package com.tinytiger.common.utils.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.image.cropview.CropUtil;
import com.tinytiger.common.utils.image.cropview.CropView;
import com.tinytiger.common.utils.toast.ToastUtils;


import java.io.File;
import java.io.IOException;


/**
 * @author zhw_luke
 * @date 2019/10/23 0023 下午 4:08
 * @Copyright 小虎互联科技
 * @doc 图片裁剪页
 * @since 5.0.0
 */
public class CropImageActivity extends Activity {

    public static void actionStart(Context context, int type, int requestCode) {
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void actionStart(Context context, int type, int requestCode, String txt) {
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("txt", txt);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 0 1:1裁剪
     * 1 125:100
     * 2 身份证158:100
     */
    private int type = 0;
    private int mAspectX = 1;
    private int mAspectY = 1;
    private String txt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corpimage);

        type = getIntent().getIntExtra("type", 0);
        txt = getIntent().getStringExtra("txt");

        if (type == 1) {
            mAspectX = 125;
            mAspectY = 100;
        }else if (type == 2){
            mAspectX = 158;
            mAspectY = 100;
        }

        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 9162);
        initData();
    }

    private CropView cropView;

    private void initData() {
        cropView = findViewById(R.id.cropView);
        if (!TextUtils.isEmpty(txt)) {
            TextView tvTxt = findViewById(R.id.tvTxt);
            tvTxt.setText(txt);
        }

        findViewById(R.id.doneBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        File file= new File(getCacheDir(), "cropped" + System.currentTimeMillis());
                        if(!file.exists()){
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Uri destination = Uri.fromFile(file);

                        if (destination == null) {
                            ToastUtils.show(CropImageActivity.this, "图片写入失败");
                            finish();
                        }
                        Bitmap bitmap = cropView.getOutput();
                        if (bitmap == null) {
                            ToastUtils.show(CropImageActivity.this, "图片写入失败");
                            finish();
                        }
                        CropUtil.saveOutput(CropImageActivity.this, destination, bitmap, 90);

                        Intent intent = new Intent();
                        intent.putExtra("path", destination.getPath());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.start();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 9162) {
            Uri source = data.getData();
            cropView.setVisibility(View.VISIBLE);
            cropView.of(source).asSquare().withAspect(mAspectX, mAspectY).initialize(this);
        } else {
            finish();
        }
    }
}
