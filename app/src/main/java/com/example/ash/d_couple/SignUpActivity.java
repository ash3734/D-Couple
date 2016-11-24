package com.example.ash.d_couple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    //버튼 연결
    @BindView(R.id.dupCheckBtn) Button buttonDuplicate;
    @BindView(R.id.submitBtn) Button buttonSignUp;
    @BindView(R.id.resetBtn) Button buttonReset;

    //text 연결
    @BindView(R.id.editId) EditText editTextID;
    @BindView(R.id.editPwd) EditText editTextPwd;
    @BindView(R.id.editPwdCheck) EditText editTextPwdCheck;
    @BindView(R.id.editName) EditText editTextName;
    @BindView(R.id.editphoneNumber) EditText editTextPhone;
    @BindView(R.id.editEmail) EditText editTextEmail;
    @BindView(R.id.editCityAddress) EditText editTextCityAddress;
    @BindView(R.id.editTownAddress) EditText editTextTownAddress;
    @BindView(R.id.editDetailAddress) EditText editTextDetailAddress;
    @BindView(R.id.radioGender) RadioGroup groupGender;

    RadioButton tempGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        tempGender = (RadioButton) findViewById(groupGender.getCheckedRadioButtonId());
    }
    //아이디를 디비에 접근에 중복체크한다.
    @OnClick(R.id.dupCheckBtn)
    public void duplicateCheck(){
        if(dbDuplicate()){
            Toast.makeText(getApplicationContext(),"아이디를 사용할 수 없습니다.",Toast.LENGTH_LONG).show();
            editTextID.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"아이디를 사용할 수 있습니다.",Toast.LENGTH_LONG).show();
        }
    }
    //디비에 쿼리를 날려 회원가입을 한다.
    @OnClick(R.id.submitBtn)
    public void submit(){
        if(!editTextPwd.getText().toString().equals(editTextPwdCheck.getText().toString())){
            Toast.makeText(getApplicationContext(),"비밀번호가 맞지 않습니다.",Toast.LENGTH_LONG).show();
            return ;
        }
        //필수사항에 미입력 예외처리
        //추가 구현사항**************************************************
        signUp();
        Toast.makeText(getApplicationContext(),"회원 가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
        User.ID = editTextID.getText().toString();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    //리셋 버튼을 누르면 editText를 초기화된다.
    @OnClick(R.id.resetBtn)
    public void reset(){
        editTextID.setText("");
        editTextPwd.setText("");
        editTextPwdCheck.setText("");
        editTextName.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");
        editTextCityAddress.setText("");
        editTextTownAddress.setText("");
        editTextDetailAddress.setText("");
        groupGender.clearCheck();
    }

    //중복되면 true 아니면 false
    public boolean dbDuplicate(){
        DBManager dbManager = new DBManager();
        dbManager.setAParameter("UserID",editTextID.getText().toString());
        dbManager.db_connect("http://dcouple.dothome.co.kr/join_id_check.php");
        if(dbManager.getResult().equals("no")){
            return true;
        }
        else{
            return false;
        }
    }
    public void signUp(){
        DBManager dbManager = new DBManager();
        dbManager.setAParameter("UserID",editTextID.getText().toString());
        dbManager.setAParameter("Password",editTextPwd.getText().toString());
        dbManager.setAParameter("UserName",editTextName.getText().toString());
        dbManager.setAParameter("Phone",editTextPhone.getText().toString());
        dbManager.setAParameter("Email",editTextEmail.getText().toString());
        dbManager.setAParameter("City",editTextCityAddress.getText().toString());
        dbManager.setAParameter("Town",editTextTownAddress.getText().toString());
        dbManager.setAParameter("DetailAddress",editTextDetailAddress.getText().toString());
        dbManager.setAParameter("gender",tempGender.getText().toString());
        dbManager.db_connect("http://dcouple.dothome.co.kr/join.php");
    }
}
