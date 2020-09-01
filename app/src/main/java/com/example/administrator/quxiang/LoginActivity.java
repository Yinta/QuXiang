package com.example.administrator.quxiang;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.bean.User;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    public static final int LOGIN_IN = 1;
    boolean usernameBoolean;
    boolean passwordBoolean;
    boolean loginBoolean;
    @Bind(R.id.zhuce)
    TextView zhuce;
    @Bind(R.id.btn)
    CircularProgressButton btn;
    Titanic titanic;
    TitanicTextView titanicTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UtilsStyle.statusBarLightMode(this);
        if (BmobUser.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
titanicTextView=(TitanicTextView)findViewById(R.id.titanic_tv);
        titanic=new Titanic();
        titanic.start(titanicTextView);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_IN:
                    if (loginBoolean) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @OnTextChanged(R.id.password)
    void onPssswordTextChanged(CharSequence text) {
        String account = password.getText().toString();
        if (account.length() < 6) {
            passwordWrapper.setError("密码长度小于6位");
            passwordBoolean = false;
        } else if (account.length() >= 6 && account.length() <= 15) {
            passwordWrapper.setErrorEnabled(false);
            passwordBoolean = true;
        } else if (account.length() > 15) {
            passwordWrapper.setError("密码长度大于15位");
            passwordBoolean = false;
        }
    }

    @OnTextChanged(R.id.username)
    void onUsernameTextChanged(CharSequence text) {
        String account = username.getText().toString();
        if (account.length() > 15) {
            usernameWrapper.setError("账号长度大于15位");
            usernameBoolean = false;
        } else {
            usernameWrapper.setErrorEnabled(false);
            usernameBoolean = true;
        }
    }

    @OnClick(R.id.btn)
    public void onbtnClicked(final View view) {
        if (passwordBoolean && usernameBoolean) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loginByAccount(view);
                    Message message = new Message();
                    message.what = LOGIN_IN;
                    handler.sendMessageDelayed(message, 3000);
                }
            }).start();
        }
    }

    private void loginByAccount(final View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //此处替换为你的用户名密码
        BmobUser.loginByAccount(username.getText().toString(), password.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    loginBoolean = true;
                    simulateSuccessProgress(btn);
                } else {
                    loginBoolean = false;
                    simulateErrorProgress(btn);
                    btn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setMessage("用户名或密码错误,请重新输入");
                            dialog.setTitle("登录失败");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            password.setText("");
                                            btn.setProgress(0);
                                        }
                                    }
                            );
                            dialog.show();
                        }
                    }, 2000);
                }
            }
        });
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                }
            }
        });
        widthAnimation.start();
    }

    @OnClick(R.id.zhuce)
    public void onzhuceClicked() {
        startActivity(new Intent(LoginActivity.this, SigninActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        titanic.cancel();
    }
}
