package project.android.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    Button button;
    EditText editName, editValue;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseRef = FirebaseDatabase.getInstance().getReference();

        button = (Button)findViewById(R.id.add);
        editName = (EditText)findViewById(R.id.name);
        editValue = (EditText)findViewById(R.id.value);
    }

    public void onClick(View view){

        String name = editName.getText().toString();
        String value = editValue.getText().toString();

        switch(view.getId()){
            case R.id.add :
                databaseRef.child("test").child(name).setValue(value);
                Toast.makeText(AddActivity.this, "추가되었습니다", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
