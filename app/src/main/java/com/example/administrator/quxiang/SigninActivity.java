package com.example.administrator.quxiang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SigninActivity extends AppCompatActivity {
public static final int SIGN_IN=1;
    @Bind(R.id.nickname)
    EditText nickname;
    @Bind(R.id.nicknameWrapper)
    TextInputLayout nicknameWrapper;
    @Bind(R.id.user_name)
    EditText username;
    @Bind(R.id.username_Wrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.password_)
    EditText password;
    @Bind(R.id.password_Wrapper)
    TextInputLayout passwordWrapper;
    @Bind(R.id.btn_zhuce)
    Button btnZhuce;
    boolean usernameBoolean;
    boolean passwordBoolean;
    boolean nicknameBoolean;
    boolean zhuceBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        UtilsStyle.statusBarLightMode(this);
    }
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SIGN_IN:
                if (zhuceBoolean){
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(((View)msg.obj).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
               break;
            default:
                break;
        }
    }
};
    @OnTextChanged(R.id.password_)
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

    @OnTextChanged(R.id.nickname)
    void onNicknameTextChanged(CharSequence text) {
        String account = username.getText().toString();
        if (account.length() > 7) {
            nicknameWrapper.setError("昵称长度大于7位");
            nicknameBoolean = false;
        } else {
            nicknameWrapper.setErrorEnabled(false);
            nicknameBoolean = true;
        }
    }

    @OnTextChanged(R.id.user_name)
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

    @OnClick(R.id.btn_zhuce)
    public void onViewClicked(final View view) {
        if (passwordBoolean && usernameBoolean && nicknameBoolean) {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 signUp(view);
                 Message message=new Message();
                 message.what=SIGN_IN;
                 message.obj=view;
                 handler.sendMessageDelayed(message,1000);
             }
         }).start();

        }
    }
    private void signUp(final View view) {
        final User user = new User();
        user.setNickname(nickname.getText().toString());
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    zhuceBoolean=true;
                    Snackbar.make(view, "注册成功", Snackbar.LENGTH_LONG).show();
                } else {
                    zhuceBoolean=false;
                    Snackbar.make(view, "注册失败：此用户名已注册",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public static class MPermissionsActivity extends AppCompatActivity {
        private final String TAG = "MPermissions";
        private int REQUEST_CODE_PERMISSION = 0x00099;

        /**
         * 请求权限
         *
         * @param permissions 请求的权限
         * @param requestCode 请求权限的请求码
         */
        public void requestPermission(String[] permissions, int requestCode) {
            this.REQUEST_CODE_PERMISSION = requestCode;
            if (checkPermissions(permissions)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                List<String> needPermissions = getDeniedPermissions(permissions);
                ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
            }
        }

        /**
         * 检测所有的权限是否都已授权
         *
         * @param permissions
         * @return
         */
        private boolean checkPermissions(String[] permissions) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true;
            }

            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 获取权限集中需要申请权限的列表
         *
         * @param permissions
         * @return
         */
        private List<String> getDeniedPermissions(String[] permissions) {
            List<String> needRequestPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) !=
                        PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    needRequestPermissionList.add(permission);
                }
            }
            return needRequestPermissionList;
        }


        /**
         * 系统请求权限回调
         *
         * @param requestCode
         * @param permissions
         * @param grantResults
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_PERMISSION) {
                if (verifyPermissions(grantResults)) {
                    permissionSuccess(REQUEST_CODE_PERMISSION);
                } else {
                    permissionFail(REQUEST_CODE_PERMISSION);
                    showTipsDialog();
                }
            }
        }

        /**
         * 确认所有的权限是否都已授权
         *
         * @param grantResults
         * @return
         */
        private boolean verifyPermissions(int[] grantResults) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 显示提示对话框
         */
        private void showTipsDialog() {
            new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    }).show();
        }

        /**
         * 启动当前应用设置页面
         */
        private void startAppSettings() {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        /**
         * 获取权限成功
         *
         * @param requestCode
         */
        public void permissionSuccess(int requestCode) {
            Log.d(TAG, "获取权限成功=" + requestCode);

        }

        /**
         * 权限获取失败
         * @param requestCode
         */
        public void permissionFail(int requestCode) {
            Log.d(TAG, "获取权限失败=" + requestCode);
        }
    }
}
