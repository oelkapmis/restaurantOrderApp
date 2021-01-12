package com.example.restaurantorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText etPhone, etPassword, etName;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPhone.getText().toString().equals("") || etName.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "Kayıt bilgileri boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                }
                else{

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Yükleniyor...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDialog.dismiss();

                            if(dataSnapshot.child(etPhone.getText().toString()).exists()){
                                Toast.makeText(SignUp.this, "Bu telefon numarası zaten kayıtlı!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                User user = new User(etName.getText().toString(), etPassword.getText().toString());
                                table_user.child(etPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }
}
