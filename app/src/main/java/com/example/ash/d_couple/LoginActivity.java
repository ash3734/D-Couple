package com.example.ash.d_couple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextID;
    private EditText editTextPW;
    @BindView(R.id.loginBtn) Button buttonLogin;
    @BindView(R.id.signUpBtn) Button buttonSignUP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        editTextID = (EditText)findViewById(R.id.inputIdEdit);
        editTextPW = (EditText)findViewById(R.id.inputPwdEdit);
    }
   @OnClick(R.id.loginBtn)
    public void login(){
       if(dbLogin()){
           Toast.makeText(getApplicationContext(),"로그인이 완료 되었습니다..",Toast.LENGTH_LONG).show();
           User.ID = editTextID.getText().toString();
           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
           startActivity(intent);
       }
       else{
           Toast.makeText(getApplicationContext(),"아이디와 패스워드가 맞지 않습니다.",Toast.LENGTH_LONG).show();
           editTextID.setText("");
           editTextPW.setText("");
       }
   }
    @OnClick(R.id.signUpBtn)
    public void signup(){
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);
    }

    //로그인 연동
    public boolean dbLogin(){
        DBManager dbManager = new DBManager();
        dbManager.setAParameter("UserID",editTextID.getText().toString());
        dbManager.setAParameter("Password",editTextPW.getText().toString());
        dbManager.db_connect("http://dcouple.dothome.co.kr/login2.php");
        if(dbManager.getResult().equals("failure")){
            return false;
        }
        else{
            return true;
        }
    }


}
