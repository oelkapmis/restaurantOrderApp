package com.example.restaurantorderapp;

import Model.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    MaterialEditText etPhone, etPassword, etName;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnSignIn = findViewById(R.id.btnSignIn);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etPhone.getText().toString().equals("") || etName.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(SignIn.this, "Giriş bilgileri boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                }
                else{
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Yükleniyor...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            mDialog.dismiss();

                            if(dataSnapshot.child(etPhone.getText().toString()).exists()){

                                User user = dataSnapshot.child(etPhone.getText().toString()).getValue(User.class);
                                assert user != null;
                                if (user.getPassword().equals(etPassword.getText().toString()) && user.getName().equals(etName.getText().toString())) {
                                    Toast.makeText(SignIn.this, "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignIn.this, "Yanlış isim ya da parola!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(SignIn.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
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
