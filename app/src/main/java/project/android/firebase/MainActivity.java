package project.android.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    EditText edit_email, edit_pwd;
    Button signUp, signIn, add;
    DatabaseReference databaseRef;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance(); //FirebaseAuth의 인스턴스 가져옴
        databaseRef = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        edit_email = (EditText) findViewById(R.id.email);
        edit_pwd = (EditText) findViewById(R.id.pwd);
        signUp = (Button)findViewById(R.id.signUp);
        signIn = (Button)findViewById(R.id.signIn);
        add = (Button)findViewById(R.id.addButton);
    }

    public void onClick(View view){

        String email = edit_email.getText().toString();
        String pwd = edit_pwd.getText().toString();

        switch(view.getId()){
            case R.id.signUp :
                signUp(email, pwd);
                break;
            case R.id.signIn :
                signIn(email, pwd);
                break;
            case R.id.addButton :
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void signUp(String email, String pwd){

        auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser(); // signUp 성공한 유저는 데이터베이스에 저장
                            User userModel = new User(user.getEmail());
                            databaseRef.child("users").child(user.getUid()).setValue(userModel);
                            Toast.makeText(MainActivity.this, "signUp ok", Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "signUp failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signIn(String email, String pwd){
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            add.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "signIn ok ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "signIn failed ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
}


