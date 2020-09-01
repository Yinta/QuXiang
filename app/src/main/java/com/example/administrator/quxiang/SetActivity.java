package com.example.administrator.quxiang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class SetActivity extends AppCompatActivity {

    @Bind(R.id.finish_xinxi)
    Button finishXinxi;
    @Bind(R.id.sex_Imgeview)
    ImageView sexImgeview;
    @Bind(R.id.sex_content)
    TextView sexContent;
    @Bind(R.id.change_sex)
    ImageView changeSex;
    @Bind(R.id.email_Imgeview)
    ImageView emailImgeview;
    @Bind(R.id.eamil_content)
    TextView eamilContent;
    @Bind(R.id.change_email)
    ImageView changeEmail;
    @Bind(R.id.phone_Imgeview)
    ImageView phoneImgeview;
    @Bind(R.id.phone_content)
    TextView phoneContent;
    @Bind(R.id.change_phone)
    ImageView changePhone;
    @Bind(R.id.place_Imgeview)
    ImageView placeImgeview;
    @Bind(R.id.place_content)
    TextView placeContent;
    @Bind(R.id.change_place)
    ImageView changePlace;
    @Bind(R.id.like_Imgeview)
    ImageView likeImgeview;
    @Bind(R.id.like_content)
    TextView likeContent;
    @Bind(R.id.change_like)
    ImageView changeLike;
    final int FROM_SEX = 0;
    final int FROM_EMAIL = 1;
    final int FROM_PHONE = 2;
    final int FROM_PLACE = 3;
    final int FROM_LIKE = 4;
    @Bind(R.id.relative_sex)
    RelativeLayout relativeSex;
    @Bind(R.id.relative_email)
    RelativeLayout relativeEmail;
    @Bind(R.id.relative_phone)
    RelativeLayout relativePhone;
    @Bind(R.id.relative_place)
    RelativeLayout relativePlace;
    @Bind(R.id.relative_like)
    RelativeLayout relativeLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        UtilsStyle.statusBarLightMode(this);
        initData();
    }
    public void initData(){
        User user= BmobUser.getCurrentUser(User.class);
        if(!TextUtils.isEmpty(user.getGender())){
            sexContent.setText(user.getGender());
        }
        if(!TextUtils.isEmpty(user.getEmail())){
            eamilContent.setText(user.getEmail());
        }
        if(!TextUtils.isEmpty(user.getMobilePhoneNumber())){
            phoneContent.setText(user.getMobilePhoneNumber());
        }
        if(!TextUtils.isEmpty(user.getCountry())){
            placeContent.setText(user.getCountry());
        }
        if(user.getLikethings().size()!=0){
            likeContent.setText(user.getLikethings().get(0)+","+user.getLikethings().get(1));
        }
    }
    @OnClick({R.id.finish_xinxi,R.id.relative_sex, R.id.relative_email, R.id.relative_phone, R.id.relative_place, R.id.relative_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_xinxi:
                finish();
                break;
            case R.id.relative_sex:
                Intent intentSex = new Intent(SetActivity.this, ChangeActivity.class);
                intentSex.putExtra("from", FROM_SEX);
                if(!TextUtils.isEmpty(sexContent.getText())){
                    intentSex.putExtra("data",sexContent.getText());
                }
                startActivityForResult(intentSex,FROM_SEX);
                break;
            case R.id.relative_email:
                Intent intentEmail = new Intent(SetActivity.this, ChangeActivity.class);
                intentEmail.putExtra("from", FROM_EMAIL);
                if(!TextUtils.isEmpty(eamilContent.getText())){
                    intentEmail.putExtra("data",eamilContent.getText());
                }
                startActivityForResult(intentEmail,FROM_EMAIL);
                break;
            case R.id.relative_phone:
                Intent intentPhone = new Intent(SetActivity.this, ChangeActivity.class);
                if(!TextUtils.isEmpty(phoneContent.getText())){
                    intentPhone.putExtra("data",phoneContent.getText());
                }
                intentPhone.putExtra("from", FROM_PHONE);
                startActivityForResult(intentPhone,FROM_PHONE);
                break;
            case R.id.relative_place:
                Intent intentPlace = new Intent(SetActivity.this, ChangeActivity.class);
                intentPlace.putExtra("from", FROM_PLACE);
                if(!TextUtils.isEmpty(placeContent.getText())){
                    intentPlace.putExtra("data",placeContent.getText());
                }
                startActivityForResult(intentPlace,FROM_PLACE);
                break;
            case R.id.relative_like:
                Intent intentLike = new Intent(SetActivity.this, ChangeActivity.class);
                intentLike.putExtra("from", FROM_LIKE);
                if(!TextUtils.isEmpty(likeContent.getText())){
                    intentLike.putExtra("data",likeContent.getText());
                }
                startActivityForResult(intentLike,FROM_LIKE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode==RESULT_OK){
                    sexContent.setText(data.getStringExtra("data"));
                }
                break;
            case 1:
                if(resultCode==RESULT_OK){
                    eamilContent.setText(data.getStringExtra("data"));
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    phoneContent.setText(data.getStringExtra("data"));
                }
                break;
            case 3:
                if(resultCode==RESULT_OK){
                    placeContent.setText(data.getStringExtra("data"));
                }
                break;
            case 4:
                if(resultCode==RESULT_OK){
                    likeContent.setText(data.getStringExtra("data"));
                }
                break;
            default:
                break;
        }
    }
}
