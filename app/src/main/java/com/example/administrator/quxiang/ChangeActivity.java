package com.example.administrator.quxiang;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.bean.User;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeActivity extends AppCompatActivity {

    @Bind(R.id.finish_set)
    Button finishSet;
    @Bind(R.id.complete_change)
    TextView completeChange;
    @Bind(R.id.edit_content)
    EditText editContent;
    int from;
    final int FROM_SEX = 0;
    final int FROM_EMAIL = 1;
    final int FROM_PHONE = 2;
    final int FROM_PLACE = 3;
    final int FROM_LIKE = 4;
    @Bind(R.id.change_error)
    TextView changeError;
    @Bind(R.id.sex_text)
    TextView sexText;
    @Bind(R.id.set_sex)
    ImageView setSex;
    @Bind(R.id.relative_one)
    RelativeLayout relativeOne;
    @Bind(R.id.like_text_one)
    TextView likeTextOne;
    @Bind(R.id.set_like_one)
    ImageView setLikeOne;
    @Bind(R.id.relative_two)
    RelativeLayout relativeTwo;
    @Bind(R.id.like_text_two)
    TextView likeTextTwo;
    @Bind(R.id.set_like_two)
    ImageView setLikeTwo;
    @Bind(R.id.relative_three)
    RelativeLayout relativeThree;
    Dialog sexDialog;
    Dialog likeDialogOne;
    Dialog likeDialogTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        UtilsStyle.statusBarLightMode(this);
        showSexDialog();
        showLikeDialogOne();
        showLikeDialogTwo();
        from = getIntent().getIntExtra("from", 0);
        changeParams();
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editContent.length() > 0) {
                    completeChange.setTextColor(Color.parseColor("#ed6d39"));
                    completeChange.setClickable(true);
                } else {
                    completeChange.setTextColor(Color.parseColor("#aaaaaa"));
                    completeChange.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void changeParams() {
        switch (from) {
            case FROM_SEX:
                if(!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
                    sexText.setText(getIntent().getStringExtra("data"));
                }
                editContent.setVisibility(View.GONE);
                changeError.setVisibility(View.GONE);
                relativeOne.setVisibility(View.VISIBLE);
                relativeTwo.setVisibility(View.GONE);
                relativeThree.setVisibility(View.GONE);
                break;
            case FROM_EMAIL:
                editContent.setVisibility(View.VISIBLE);
                changeError.setVisibility(View.VISIBLE);
                relativeOne.setVisibility(View.GONE);
                relativeTwo.setVisibility(View.GONE);
                relativeThree.setVisibility(View.GONE);
                break;
            case FROM_PHONE:
                editContent.setVisibility(View.VISIBLE);
                changeError.setVisibility(View.VISIBLE);
                relativeOne.setVisibility(View.GONE);
                relativeTwo.setVisibility(View.GONE);
                relativeThree.setVisibility(View.GONE);
                editContent.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case FROM_PLACE:
                editContent.setVisibility(View.VISIBLE);
                changeError.setVisibility(View.VISIBLE);
                relativeOne.setVisibility(View.GONE);
                relativeTwo.setVisibility(View.GONE);
                relativeThree.setVisibility(View.GONE);
                break;
            case FROM_LIKE:
                if(!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
                    String []data=getIntent().getStringExtra("data").split(",");
                    likeTextOne.setText(data[0]);
                    likeTextTwo.setText(data[1]);
                }
                editContent.setVisibility(View.GONE);
                changeError.setVisibility(View.GONE);
                relativeOne.setVisibility(View.GONE);
                relativeTwo.setVisibility(View.VISIBLE);
                relativeThree.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void showSexDialog() {
        sexDialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getApplicationContext(), R.layout.dialog_sex, null);
        sexDialog.setContentView(view);

        Window window = sexDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sexDialog.findViewById(R.id.nan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexDialog.dismiss();
                sexText.setText("男");
            }
        });
        sexDialog.findViewById(R.id.nv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexDialog.dismiss();
                sexText.setText("女");
            }
        });
        sexDialog.findViewById(R.id.sex_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexDialog.dismiss();
            }
        });
    }

    public void showLikeDialogOne() {
        likeDialogOne = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getApplicationContext(), R.layout.dialog_like, null);
        likeDialogOne.setContentView(view);

        Window window = likeDialogOne.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        likeDialogOne.findViewById(R.id.like_life).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("生活");
            }
        });
        likeDialogOne.findViewById(R.id.like_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("历史");
            }
        });
        likeDialogOne.findViewById(R.id.like_art).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("艺术");
            }
        });
        likeDialogOne.findViewById(R.id.like_science).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("科学");
            }
        });
        likeDialogOne.findViewById(R.id.like_natural).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("自然");
            }
        });
        likeDialogOne.findViewById(R.id.like_culture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("文化");
            }
        });
        likeDialogOne.findViewById(R.id.like_geography).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("地理");
            }
        });
        likeDialogOne.findViewById(R.id.like_social).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("社会");
            }
        });
        likeDialogOne.findViewById(R.id.like_character).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("人物");
            }
        });
        likeDialogOne.findViewById(R.id.like_economic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("经济");
            }
        });
        likeDialogOne.findViewById(R.id.like_sport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("体育");
            }
        });
        likeDialogOne.findViewById(R.id.like_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
                likeTextOne.setText("其他");
            }
        });
        likeDialogOne.findViewById(R.id.like_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogOne.dismiss();
            }
        });
    }

    public void showLikeDialogTwo() {
        likeDialogTwo = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getApplicationContext(), R.layout.dialog_like, null);
        likeDialogTwo.setContentView(view);

        Window window = likeDialogTwo.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        likeDialogTwo.findViewById(R.id.like_life).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("生活");
            }
        });
        likeDialogTwo.findViewById(R.id.like_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("历史");
            }
        });
        likeDialogTwo.findViewById(R.id.like_art).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("艺术");
            }
        });
        likeDialogTwo.findViewById(R.id.like_science).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("科学");
            }
        });
        likeDialogTwo.findViewById(R.id.like_natural).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("自然");
            }
        });
        likeDialogTwo.findViewById(R.id.like_culture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("文化");
            }
        });
        likeDialogTwo.findViewById(R.id.like_geography).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("地理");
            }
        });
        likeDialogTwo.findViewById(R.id.like_social).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("社会");
            }
        });
        likeDialogTwo.findViewById(R.id.like_character).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("人物");
            }
        });
        likeDialogTwo.findViewById(R.id.like_economic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("经济");
            }
        });
        likeDialogTwo.findViewById(R.id.like_sport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("体育");
            }
        });
        likeDialogTwo.findViewById(R.id.like_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
                likeTextTwo.setText("其他");
            }
        });
        likeDialogTwo.findViewById(R.id.like_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDialogTwo.dismiss();
            }
        });
    }
    private void updateUserSex(final View view, final String sex) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setGender(sex);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Intent intent=new Intent();
                    intent.putExtra("data",sex);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(ChangeActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserEmail(final View view, final String email) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setEmail(email);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    editContent.setText("");
                    Intent intent=new Intent();
                    intent.putExtra("data",email);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(ChangeActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserPhone(final View view, final String phone) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setMobilePhoneNumber(phone);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    editContent.setText("");
                    Intent intent=new Intent();
                    intent.putExtra("data",phone);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(ChangeActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserPlace(final View view, final String place) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setCountry(place);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    editContent.setText("");
                    Intent intent=new Intent();
                    intent.putExtra("data",place);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(ChangeActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserLike(final View view, final ArrayList<String> likethings) {
        final User user = BmobUser.getCurrentUser(User.class);
        user.setLikethings(likethings);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Intent intent=new Intent();
                    intent.putExtra("data",likethings.get(0)+","+likethings.get(1));
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(ChangeActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @OnClick({R.id.finish_set, R.id.complete_change,R.id.relative_one, R.id.relative_two, R.id.relative_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_set:
                finish();
                break;
            case R.id.complete_change:
                switch (from) {
                    case FROM_SEX:
updateUserSex(view,sexText.getText().toString());
                        break;
                    case FROM_EMAIL:
                        if (editContent.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && editContent.getText().toString().length()> 0) {
                           updateUserEmail(view,editContent.getText().toString().trim());
                        } else {
                            changeError.setText("邮箱无效！");
                            changeError.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    changeError.setText("");
                                }
                            },1000);
                        }
                        break;
                    case FROM_PHONE:
                        String phone_number = editContent.getText().toString().trim();
                        String num = "[1][358]\\d{9}";
                        if (editContent.getText().toString().trim().length() == 11 && phone_number.matches(num)) {
                            updateUserPhone(view,editContent.getText().toString().trim());
                        } else {
                            changeError.setText("电话号码格式错误！");
                            changeError.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    changeError.setText("");
                                }
                            },1000);
                        }
                        break;
                    case FROM_PLACE:
                       updateUserPlace(view,editContent.getText().toString().trim());
                        break;
                    case FROM_LIKE:
ArrayList<String>likethings=new ArrayList<>();
                        likethings.add(likeTextOne.getText().toString());
                        likethings.add(likeTextTwo.getText().toString());
                        updateUserLike(view,likethings);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.relative_one:
                sexDialog.show();
                break;
            case R.id.relative_two:
                likeDialogOne.show();
                break;
            case R.id.relative_three:
                likeDialogTwo.show();
                break;
        }
    }
}
