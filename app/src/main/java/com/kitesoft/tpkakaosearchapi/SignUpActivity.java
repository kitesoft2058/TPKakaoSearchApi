package com.kitesoft.tpkakaosearchapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //툴바에 업버튼 만들기
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        //[참고.]벡터 에셋으로 만든 이미지는 tint 가 있으니 직접 수정해야 검정색이 됨.
    }

    //업버튼 클릭시에 액티비티 종료
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void clickSignUp(View view) {
        //Firebase FireStore DB에 사용자 정보 저장하기

        EditText etEmail= findViewById(R.id.et_email);
        EditText etPassword= findViewById(R.id.et_password);
        EditText etPasswordConfirm= findViewById(R.id.et_password_confirm);

        String email= etEmail.getText().toString();
        String password= etPassword.getText().toString();
        String passwordConfirm= etPasswordConfirm.getText().toString();

        if(!password.equals(passwordConfirm)){
            new AlertDialog.Builder(this).setMessage("패스워드확인에 문제가 있습니다. 다시 확인하여 입력해주시기 바랍니다.").show();
            etPasswordConfirm.selectAll();//써있는 글씨를 모두 선택상태로 하여 손쉽게 새로 입력이 가능하도록...
            return;
        }

        FirebaseFirestore db= FirebaseFirestore.getInstance();

        //먼저 같은 이메일이 있는지 확인 [ 아래 저장작업을 먼저 작성 성공한 후에 본 코딩 작성. ]
        db.collection("emailUsers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot snapshot : queryDocumentSnapshots){

                    if( email.equals( snapshot.getData().get("email").toString() )){
                        new AlertDialog.Builder(SignUpActivity.this).setMessage("중복된 이메일이 있습니다. 다시 확인하여 입력해주시기 바랍니다.").show();
                        etEmail.requestFocus();
                        etEmail.selectAll();//써있는 글씨를 모두 선택상태로 하여 손쉽게 새로 입력이 가능하도록...
                        return;
                    }
                }

                //여기부터 코드 먼저 작성!
                //저장할 값(이메일,비밀번호)을 HashMap으로 저장
                Map<String, String> user= new HashMap<>();
                user.put("email", email);
                user.put("password", password);

                //.add()를 통해 document 명이 랜덤하게 만들어짐.- 이 랜덤값을 id로 사용하고자 함.(다른 간편로그인방식의 id,email정보와 같은 형태의 정보로 사용하고자.)
                db.collection("emailUsers").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        new AlertDialog.Builder(SignUpActivity.this)
                                .setMessage("축하합니다.\n회원가입이 완료되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "회원가입에 오류가 발생했습니다.\n다시 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }
}