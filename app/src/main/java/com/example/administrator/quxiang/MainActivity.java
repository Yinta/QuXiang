package com.example.administrator.quxiang;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.CacheUtil;
import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.adapter.FgAdapter;
import com.example.administrator.quxiang.bean.User;
import com.example.administrator.quxiang.fragment.IssueFragment;
import com.example.administrator.quxiang.fragment.MyFragment;
import com.example.administrator.quxiang.fragment.SquareFragment;
import com.example.administrator.quxiang.interFace.UploadListener;
import com.example.administrator.quxiang.service.UploadService;
import com.example.administrator.quxiang.viewpage.NoScrollViewPager;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.wx.goodview.GoodView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends MPermissionsActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @Bind(R.id.container)
    DrawerLayout container;
    @Bind(R.id.nav_view)
    NavigationView navView;
    Dialog dialog;
    @Bind(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @Bind(R.id.navigation)
    MagicIndicator magicIndicator;
    private FgAdapter fragmentPagerAdapter;
    public ArrayList<Fragment> fragments = new ArrayList<>();
    CircleImageView circleimageview;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private CropOptions cropOptions;    //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;       //图片保存路径
    private Bitmap bitmap;
    private String[] titles = new String[]{"发布", "广场", "我的"};
    private List<String> mDataList = Arrays.asList(titles);
    private Integer[] id = new Integer[]{R.drawable.ic_home_black_24dp,R.drawable.ic_dashboard_black_24dp,R.drawable.ic_notifications_black_24dp};
    private List<Integer> resouceid = Arrays.asList(id);
    public UploadService.UploadBinder uploadBinder;
     String iconPath;
    Timer timer;
    boolean isExit=false;
    public  UploadListener  uploadListener =new UploadListener() {
        @Override
        public int getProgress() {

            return  ((SquareFragment)fragments.get(1)).numberProgressBar.getProgress();
        }

        @Override
        public void setProgress(int progress) {
            ((SquareFragment)fragments.get(1)).numberProgressBar.setProgress(progress);
        }

        @Override
        public void setProgressBarVisable() {
            ((SquareFragment)fragments.get(1)).numberProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void setProgressBarGone() {
            ((SquareFragment)fragments.get(1)).numberProgressBar.setVisibility(View.GONE);
        }
    };
    public ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            uploadBinder=(UploadService.UploadBinder)service;
            uploadBinder.setProgressListener(uploadListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private void initMagicIndicator1() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.simple_pager_title_layout, null);
                final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.title_img);
                final TextView titleText = (TextView) customLayout.findViewById(R.id.title_text);
                titleImg.setImageResource(resouceid.get(index));
                titleText.setText(mDataList.get(index));
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(Color.parseColor("#555555"));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            viewpagerMain.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpagerMain);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setMessage();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.zhuxiao:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("是否确定注销账号");
                        dialog.setTitle("注销账号");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        BmobUser.logOut();
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                        ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                        break;
                    case R.id.set:
                        startActivity(new Intent(MainActivity.this,SetActivity.class));
                    default:
                        break;
                }
                return true;
            }
        });
    }


    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    public  void changeViewPage(int index){
        viewpagerMain.setCurrentItem(index);
        BitmapUtil.latestAdapter.notifyDataSetChanged();
        BitmapUtil.recyclerViewLatest.smoothScrollToPosition(0);
        BitmapUtil.myAdapter.notifyDataSetChanged();
        BitmapUtil.recyclerViewMy.smoothScrollToPosition(0);
    }
    public  void changeViewPageWithPhoto(int index){
        viewpagerMain.setCurrentItem(index);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    void init() {
        ButterKnife.bind(this);
       Intent intent=new Intent(MainActivity.this,UploadService.class);
       startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        initMagicIndicator1();
        UtilsStyle.statusBarLightMode(this);
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, 0x00099);
        fragments.add(new IssueFragment());
        fragments.add(new SquareFragment());
        fragments.add(new MyFragment());
        fragmentPagerAdapter = new FgAdapter(getSupportFragmentManager(), fragments);
        viewpagerMain.setOffscreenPageLimit(2);
        viewpagerMain.setAdapter(fragmentPagerAdapter);
        viewpagerMain.setCurrentItem(1);
        showBottomDialog();
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);    //设置为需要压缩
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_custom_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.findViewById(R.id.takephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                imageUri = getImageCropUri();
                //拍照并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
            }
        });
        dialog.findViewById(R.id.pictureselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                imageUri = getImageCropUri();
//                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
             /*   takePhoto.onPickMultipleWithCrop(4, cropOptions);*/
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        circleimageview = (CircleImageView) navView.getHeaderView(0).findViewById(R.id.icon_image);
        circleimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                takePhoto.onEnableCompress(compressConfig, true);    //设置为需要压缩
                dialog.show();
            }
        });
    }

    void setMessage() {
        final User user = BmobUser.getCurrentUser(User.class);
        final TextView text = (TextView) navView.getHeaderView(0).findViewById(R.id.nick);
        text.setText(user.getNickname());
        if (!TextUtils.isEmpty(user.getNativePicture())) {
            bitmap = BitmapFactory.decodeFile(user.getNativePicture());
        }
        if (bitmap != null) {
            circleimageview.setImageBitmap(bitmap);
        } else if (user.getPicture() != null) {
            user.getPicture().download(new DownloadFileListener() {
                @Override
                public void onProgress(Integer integer, long l) {

                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        //设置圆形头像并显示
                        circleimageview.setImageBitmap(bitmap);
                        saveBitmap(bitmap, user);
                    }
                }
            });
        }
    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bitmap, User user) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        user.setNativePicture(iconPath);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                container.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void takeSuccess(TResult result) {
         iconPath = result.getImage().getCompressPath();
        //Google Glide库 用于加载图片资源
        Glide.with(this).load(iconPath).into(circleimageview);
        final BmobFile bmobFile = new BmobFile(new File(iconPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    user.setPicture(bmobFile);
                    user.setNativePicture(iconPath);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "存取图片成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "存取失败3", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "存取失败2" + iconPath, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(MainActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {

    }

    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtil.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
        }
        return false;
    }
    void exit(){
        if(isExit==false){
            isExit=true;
            Toast.makeText(MainActivity.this,"再按一次退出趣享",Toast.LENGTH_SHORT).show();
            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;
                }
            },2000);
        }else {
            finish();
        }
    }
}


