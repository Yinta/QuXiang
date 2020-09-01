package com.example.administrator.quxiang.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.administrator.quxiang.MainActivity;
import com.example.administrator.quxiang.PhotoActivity;
import com.example.administrator.quxiang.R;
import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.adapter.GridAdapter;
import com.example.administrator.quxiang.bean.Photo;
import com.example.administrator.quxiang.bean.Post;
import com.example.administrator.quxiang.bean.User;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by Administrator on 2020/3/5.
 */

public class IssueFragment extends TakePhotoFragment {
    @Bind(R.id.text_content)
    EditText textContent;
    @Bind(R.id.gradview)
    GridView gradview;
    @Bind(R.id.content_Wrapper)
    TextInputLayout contentWrapper;
    @Bind(R.id.issue)
    TextView issue;
    @Bind(R.id.life)
    RadioButton life;
    @Bind(R.id.history)
    RadioButton history;
    @Bind(R.id.clear)
    TextView clear;
    @Bind(R.id.art)
    RadioButton art;
    @Bind(R.id.science)
    RadioButton science;
    @Bind(R.id.natural)
    RadioButton natural;
    @Bind(R.id.culture)
    RadioButton culture;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.geography)
    RadioButton geography;
    @Bind(R.id.society)
    RadioButton society;
    @Bind(R.id.character)
    RadioButton character;
    @Bind(R.id.economic)
    RadioButton economic;
    @Bind(R.id.sport)
    RadioButton sport;
    @Bind(R.id.other)
    RadioButton other;
    private RadioButton radioButton;
    private String selectText = "生活";
    private GridAdapter gridAdapter;
    private Dialog dialog;
    private Uri imageUri;       //图片保存路径
    private TakePhoto takePhoto;
    private CropOptions cropOptions;    //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private boolean textContentBool;
    private ArrayList<TImage> arrayList;
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fabu, container, false);
        ButterKnife.bind(this, view);
        changePosition();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void changePosition() {
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        final int width = outMetrics.widthPixels;
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 82, getActivity().getResources().getDisplayMetrics());
        final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) radioGroup.getLayoutParams();
        life.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lifeparams = (LinearLayout.LayoutParams) life.getLayoutParams();
                lifeparams.setMargins(0, 0, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                life.setLayoutParams(lifeparams);
            }
        });
        history.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams historyparams = (LinearLayout.LayoutParams) history.getLayoutParams();
                historyparams.setMargins(width / 2 - history.getMeasuredWidth() / 2 - marginLayoutParams.leftMargin, -height, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                history.setLayoutParams(historyparams);
            }
        });
        art.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams artparams = (LinearLayout.LayoutParams) art.getLayoutParams();
                artparams.setMargins(width - art.getMeasuredWidth() - marginLayoutParams.leftMargin * 2, -height, 0, 20);//宽度设置为屏幕的一半，高度为合适的高度值
                art.setLayoutParams(artparams);
            }
        });
        science.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams scienceparams = (LinearLayout.LayoutParams) science.getLayoutParams();
                scienceparams.setMargins(0, 0, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                science.setLayoutParams(scienceparams);
            }
        });
        natural.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams naturalparams = (LinearLayout.LayoutParams) natural.getLayoutParams();
                naturalparams.setMargins((width - natural.getMeasuredWidth()) / 2 - marginLayoutParams.leftMargin, -height, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                natural.setLayoutParams(naturalparams);
            }
        });
        culture.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams cultureparams = (LinearLayout.LayoutParams) culture.getLayoutParams();
                cultureparams.setMargins(width - culture.getMeasuredWidth() - marginLayoutParams.leftMargin * 2, -height, 0, 20);//宽度设置为屏幕的一半，高度为合适的高度值
                culture.setLayoutParams(cultureparams);
            }
        });
        geography.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams geographyparams = (LinearLayout.LayoutParams) geography.getLayoutParams();
                geographyparams.setMargins(0, 0, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                geography.setLayoutParams(geographyparams);
            }
        });
        society.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams societyparams = (LinearLayout.LayoutParams) society.getLayoutParams();
                societyparams.setMargins((width - society.getMeasuredWidth()) / 2 - marginLayoutParams.leftMargin, -height, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                society.setLayoutParams(societyparams);
            }
        });
        character.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams characterparams = (LinearLayout.LayoutParams) character.getLayoutParams();
                characterparams.setMargins(width - culture.getMeasuredWidth() - marginLayoutParams.leftMargin * 2, -height, 0, 20);//宽度设置为屏幕的一半，高度为合适的高度值
                character.setLayoutParams(characterparams);
            }
        });
        economic.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams economicparams = (LinearLayout.LayoutParams) economic.getLayoutParams();
                economicparams.setMargins(0, 0, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                economic.setLayoutParams(economicparams);
            }
        });
        sport.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams sportparams = (LinearLayout.LayoutParams) sport.getLayoutParams();
                sportparams.setMargins((width - sport.getMeasuredWidth()) / 2 - marginLayoutParams.leftMargin, -height, 0, 0);//宽度设置为屏幕的一半，高度为合适的高度值
                sport.setLayoutParams(sportparams);
            }
        });
        other.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams otherparams = (LinearLayout.LayoutParams) other.getLayoutParams();
                otherparams.setMargins(width - other.getMeasuredWidth() - marginLayoutParams.leftMargin * 2, -height, 0, 00);//宽度设置为屏幕的一半，高度为合适的高度值
                other.setLayoutParams(otherparams);
            }
        });
    }

    private void init() {
        takePhoto = getTakePhoto();
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        //设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);    //设置为需要压缩
        showBottomDialog();
        gradview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //初始化适配器
        gridAdapter = new GridAdapter(getActivity());
        BitmapUtil.gridAdapter = gridAdapter;
        gradview.setAdapter(gridAdapter);
        gradview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                //设置压缩参数
                compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
                takePhoto.onEnableCompress(compressConfig, true);    //设置为需要压缩
                //判断点击的是否是图片
                if (arg2 == BitmapUtil.bitmaps.size()) {
                    dialog.show();
                } else {
                    //进入图片预览页面
                    Intent intent = new Intent(getActivity().getApplicationContext(), PhotoActivity.class);
                    //传递图片标识
                    intent.putExtra("ID", arg2);
                    intent.putExtra("from",1);
                    startActivity(intent);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioButton = (RadioButton) getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
                selectText = radioButton.getText().toString();
            }
        });
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(getActivity(), R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getActivity(), R.layout.dialog_custom_layout, null);
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
                if (BitmapUtil.count > 0) {
                    takePhoto.onPickFromCapture(imageUri);
                } else {
                    Toast.makeText(getActivity(), "数量已达上限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.findViewById(R.id.pictureselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                imageUri = getImageCropUri();
                if (BitmapUtil.count > 0) {
                    takePhoto.onPickMultiple(BitmapUtil.count);
                } else {
                    Toast.makeText(getActivity(), "数量已达上限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        arrayList = result.getImages();
        for (int i = 0; i < arrayList.size(); i++) {
            BitmapUtil.bitmaps.add(BitmapFactory.decodeFile(arrayList.get(i).getCompressPath()));
            BitmapUtil.paths.add(arrayList.get(i).getCompressPath());
            BitmapUtil.count--;
        }
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @OnTextChanged(R.id.text_content)
    void onTextContentChanged(CharSequence text) {
        String account = textContent.getText().toString();
        if (account.length() > 150 && account.length() <= 200) {
            contentWrapper.setCounterEnabled(true);
        } else if (account.length() <= 150) {
            contentWrapper.setCounterEnabled(false);
        } else {
            contentWrapper.setCounterEnabled(true);
        }
        if (account.length() > 0) {
            issue.setTextColor(Color.parseColor("#ed6d39"));
        } else {
            issue.setTextColor(Color.parseColor("#aaaaaa"));
        }
    }

    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @OnClick(R.id.issue)
    public void onViewClicked(View view) {
        String account = textContent.getText().toString();

        if (account.length() == 0) {
            contentWrapper.setError("文本内容为空");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    contentWrapper.setError("");
                }
            }, 1000);
        } else if (account.length() > 200) {
            contentWrapper.setError("文本内容过多");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    contentWrapper.setError("");
                }
            }, 1000);
        } else {
           InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                savePost(view);
        }
    }

    public void insertObjectNoPicture(final Post post, final View view) {
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "发布帖子成功",Toast.LENGTH_SHORT).show();
                    PostUtil.arrayListPost.add(0,post);
                    PostUtil.arrayListPostMy.add(0,post);
                } else {
                    Toast.makeText(getActivity(), "发布失败",Toast.LENGTH_SHORT).show();
                }
                textContent.setText("");
                life.setChecked(true);
                BitmapUtil.bitmaps.clear();
                BitmapUtil.paths.clear();
                BitmapUtil.count = 4;
                BitmapUtil.gridAdapter.notifyDataSetChanged();
                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.changeViewPage(1);
            }
        });
    }
    private void savePost(final View view) {
        final Post post = new Post();
        post.setContent(textContent.getText().toString().trim());
        //添加一对一关联，用户关联帖子
        post.setAuthor(BmobUser.getCurrentUser(User.class));
        post.setCommentNumber(0);
        post.setPraseNumber(0);
        post.setPhotoNumber(4-BitmapUtil.count);
        post.setLike(false);
        post.setCanDownload(true);
        post.setAvatarBoolean(true);
        post.setType(selectText);
        if (BitmapUtil.count == 4) {
            insertObjectNoPicture(post, view);
        } else {
            MainActivity mainActivity=(MainActivity)getActivity();
            textContent.setText("");
            life.setChecked(true);
            BitmapUtil.bitmaps.clear();
            BitmapUtil.gridAdapter.notifyDataSetChanged();
            mainActivity.uploadBinder.startUpload(post,view);
            mainActivity.changeViewPageWithPhoto(1);
        }

    }

    @OnClick(R.id.clear)
    public void onViewClicked() {
        textContent.setText("");
    }
}
