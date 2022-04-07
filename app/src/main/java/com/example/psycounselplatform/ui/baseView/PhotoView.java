package com.example.psycounselplatform.ui.baseView;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.psycounselplatform.R;
import com.example.psycounselplatform.util.LogUtil;

import java.io.File;
import java.io.IOException;

public class PhotoView extends androidx.appcompat.widget.AppCompatImageView {
    private Dialog dialog;
    private Activity activity;
    private Context context;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private String imagePath = null;
    private Uri imageUri;
    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LogUtil.e("PhotoView", context.toString());
        initDialog();
//        this.setOnClickListener(v -> {
//            dialog.show();
//        });
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(){
        dialog.show();
    }
    private void initDialog()
    {
        dialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_choose_image, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        Button dialogCameraBotton = view.findViewById(R.id.dialog_camera);
        Button dialogAlbumBotton = view.findViewById(R.id.dialog_album);
        Button dialogCancelBotton = view.findViewById(R.id.dialog_cancel);
        dialogCameraBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO);
                } else {
                    takePhoto();
                }
                dialog.cancel();
            }
        });

        dialogAlbumBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
                } else {
                    openAlbum();
                }
                dialog.cancel();
            }

        });
        dialogCancelBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
            }
        });
    }

    public void takePhoto(){
//        Toast.makeText(context, "start take photo", Toast.LENGTH_LONG).show();
        long time = System.currentTimeMillis();
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(context.getExternalCacheDir(), time+".jpeg");
        imagePath = outputImage.getAbsolutePath();
        try
        {
            if (outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24)
        {
            imageUri = Uri.fromFile(outputImage);
        } else
        {
            imageUri = FileProvider.getUriForFile(context,
                    "com.example.healthnice.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    public void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(context, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            case CHOOSE_PHOTO:
                LogUtil.e("AuthorizationFragment", "It's choose photo");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(context, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try
                    {
                        // 将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
                        Glide.with(this).load(imageUri).into(this);
//                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }else{
                    imagePath = null;
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        Uri uri = data.getData();
        LogUtil.e("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void displayImage(String imagePath)
    {
        LogUtil.e("album", "imagePath:" + imagePath);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Glide.with(this).load(imagePath).into(this);

//            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(context, "打开图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    public String getImagePath() {
        return imagePath;
    }

//    @BindingAdapter("imagePath")
//    public static void setImagePath(PhotoView photoView, String imagePath){
//        Context context = photoView.getContext();
//        Glide.with(context).load(imagePath).into(photoView);
//    }
//
//    @InverseBindingAdapter(attribute = "imagePath", event = "imagePathAttrChanged")
//    public static String getImagePath(PhotoView photoView){
//        return photoView.getImagePath();
//    }

//    @BindingAdapter("imagePathAttrChanged")
//    public static void setListeners(PhotoView photoView, final InverseBindingListener attrChange){
//
//    }

//    private InverseBindingListener imagePathAttrChanged = new InverseBindingListener() {
//        @Override
//        public void onChange() {
//            String imagePath_0 = getImagePath();
//        }
//    }
}