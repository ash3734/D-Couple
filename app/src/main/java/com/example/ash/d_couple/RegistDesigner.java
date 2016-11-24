package com.example.ash.d_couple;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistDesigner extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    private Bitmap bitmap ;
    private Uri filePath;
    private String profileImgeURL;

    @BindView(R.id.PostingBtn) Button buttonPost;
    @BindView(R.id.imageView) ImageView userImage;
    @BindView(R.id.photoaddBtn) Button buttonPhoto;
    @BindView(R.id.editPName) EditText editTextName;
    @BindView(R.id.editPGender) EditText editTextGender;
    @BindView(R.id.editPCity) EditText editTextCity;
    @BindView(R.id.editPTown) EditText editTextTown;
    @BindView(R.id.editArea1) EditText editTextArea1;
    @BindView(R.id.editArea2) EditText editTextArea2;
    @BindView(R.id.designSample1) ImageButton imageButtonSample1;
    @BindView(R.id.designSample2) ImageButton imageButtonSample2;
    @BindView(R.id.editPDetail) EditText editTextDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_designer);
        ButterKnife.bind(this);
        //setInfoWithID();
    }

    //디비에 있는 정보는 자동으로 세팅
    //실행이 되지 않는다.....
    public void setInfoWithID(){
        DBManager dbManager = new DBManager();
        dbManager.setAParameter("UserID",User.ID);
        dbManager.db_connect("http://dcouple.dothome.co.kr/registerCopy.php"); //확인
        editTextName.setText(dbManager.getResult("UserName"));
        editTextGender.setText(dbManager.getResult("gender"));
        editTextCity.setText(dbManager.getResult("City"));
        editTextTown.setText(dbManager.getResult("Town"));
    }
    @OnClick(R.id.PostingBtn)
    public void postingDesigner(){
        //uploadMultipart(); 이미지 서버에 업로드 메소드 되지 않는다.
        //EditText에 입력받은 부분 디비에 Insert
        DBManager dbManager = new DBManager();
        dbManager.setAParameter("UserID",User.ID);
        dbManager.setAParameter("UserName",editTextName.getText().toString());
        dbManager.setAParameter("gender",editTextGender.getText().toString());
        dbManager.setAParameter("City",editTextCity.getText().toString()); 
        dbManager.setAParameter("Town",editTextTown.getText().toString());
        dbManager.setAParameter("Area1",editTextArea1.getText().toString());
        dbManager.setAParameter("Area2",editTextArea2.getText().toString());
        dbManager.setAParameter("Detail",editTextDetail.getText().toString());
        dbManager.setAParameter("designSample1","defalut"); //이미지 넘어가지 않아 defalut로 작성
        dbManager.setAParameter("designSample2","defalut");
        dbManager.setAParameter("Profile_image","defalut");
        dbManager.db_connect("http://dcouple.dothome.co.kr/designerRegister.php");

        Intent intent = new Intent(getApplicationContext(),DesignerBoard.class);
        startActivity(intent);
        finish();
    }
    //디자이너 사진 불러오기
    @OnClick(R.id.photoaddBtn)
   public void profileAdd(){
        showFileChooser();
    }

    @OnClick(R.id.designSample1)
    public void designSample1Add(){

    }

    @OnClick(R.id.designSample2)
    public void designSample2Add(){

    }

    //갤러리에서 사진 불러오기
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //사진 불러오기 완료 후 작업
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData(); //경로 저장
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap); //User이미지에 세팅
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   //서버에 이미지 올리기
    public void uploadMultipart( ) {

        //getting the actual path of the image
        profileImgeURL = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(profileImgeURL, "image") //Adding file
                    .addParameter("name", User.ID) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //서버 주소(상수) 클래스
    private class Constants {
        public static final String UPLOAD_URL = "http://192.168.43.124/upload.php";
        //public static final String IMAGES_URL = "http://ankim2son.dothome.co.kr/getImages.php";
    }
}
