package com.e.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class logIn extends AppCompatActivity {
EditText number_auth;
Button login_auth,resend_auth,sign_up_auth;
    private String verificationId;
    private Button verify;
    String phonenumber;
    private DatabaseReference mDatabase;
    private Query query;
    user user1;
    String a="";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_log_in);
        sharedPreferences=getSharedPreferences("alreadylogged", Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        editor=sharedPreferences.edit();
        number_auth=(EditText)findViewById(R.id.number_edit);
        login_auth=(Button)findViewById(R.id.login_auth);
       query=FirebaseDatabase.getInstance().getReference("user");
        resend_auth=(Button)findViewById(R.id.resend_auth);

        login_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number_auth.getText().length()==10)
                {
                        phonenumber=number_auth.getText().toString();
                    sendVerificationCode(phonenumber);
                    AlertDialog.Builder mBuilder =new AlertDialog.Builder(logIn.this);
                    View mView=getLayoutInflater().inflate(R.layout.layout_dialog_otp,null);
                    final EditText otp_edit=(EditText)mView.findViewById(R.id.otp_edit);
                    Button otp_resend=(Button)mView.findViewById(R.id.otp_resend);
                    Button otp_verify=(Button)mView.findViewById(R.id.otp_verify);
                    TextView number_show_text=(TextView)mView.findViewById(R.id.text_phone_show);
                    TextView text_click_here=(TextView)mView.findViewById(R.id.text_click_here);
                    number_show_text.setText(phonenumber+" is this your number?");
                    otp_verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String code=otp_edit.getText().toString();
                            if (code.isEmpty()||code.length()<6)
                            {
                                otp_edit.setError("enter code");
                                otp_edit.requestFocus();
                                return;
                            }
                            verifyCode(code);
                        }
                    });

                    mBuilder.setView(mView);
                    final AlertDialog dialog=mBuilder.create();
                    dialog.show();
                    text_click_here.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
                        }
                    });
                   otp_resend.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           sendVerificationCode(phonenumber);
                       }
                   });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter the 10 digit number",Toast.LENGTH_SHORT).show();

                }
            }
        });

        resend_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(getApplicationContext(),"log out",Toast.LENGTH_SHORT).show();
            }
        });
        
    }


    private void verifyCode(String code)
    {
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
            signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                              /*  query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                        {
                                            user1=dataSnapshot1.getValue(user.class);
                                            if (user1.getMobile().equals(phonenumber))
                                            {
                                                Toast.makeText(logIn.this,"existing user",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(logIn.this,"new user",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });*/
                                FirebaseDatabase.getInstance().getReference("user").orderByChild("mobile").equalTo(phonenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 0)
                                        {
                                            Toast.makeText(getApplicationContext(),"new user",Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder mBuilder =new AlertDialog.Builder(logIn.this);
                                            View mView=getLayoutInflater().inflate(R.layout.layout_name_newuser,null);
                                            final EditText edit_name=(EditText)mView.findViewById(R.id.edit_name);
                                            Button submit_name=(Button)mView.findViewById(R.id.submit_name);
                                            submit_name.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String id=mDatabase.push().getKey();
                                                    mDatabase.child(id);
                                                    mDatabase.child(id).child("id").setValue(id);
                                                    mDatabase.child(id).child("name").setValue(edit_name.getText().toString());
                                                    mDatabase.child(id).child("mobile").setValue(phonenumber);
                                                    editor.putString("phonenumber",phonenumber);
                                                    mDatabase.child(id).child("mat_exp").setValue("0");
                                                    mDatabase.child(id).child("job_exp").setValue("0");
                                                    mDatabase.child(id).child("busss_exp").setValue("0");
                                                    editor.commit();
                                                    Intent i1=new Intent(logIn.this,Main2Activity.class);
                                                    startActivity(i1);
                                                }
                                            });
                                            mBuilder.setView(mView);
                                            AlertDialog dialog=mBuilder.create();
                                            dialog.show();
                                        }
                                        else
                                        {
                                            editor.putString("phonenumber",phonenumber);
                                            editor.commit();
                                            Toast.makeText(getApplicationContext(),"existing user",Toast.LENGTH_SHORT).show();
                                            Intent i1=new Intent(logIn.this,Main2Activity.class);
                                            startActivity(i1);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                 /*Intent i1=new Intent(logIn.this,Main2Activity.class);
                                    startActivity(i1);*/
                            }
                            else
                            {
                                    Toast.makeText(logIn.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                    }
                });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null)
            {

                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(logIn.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {

            Intent i1=new Intent(this,Main2Activity.class);
            startActivity(i1);
        }
    }
}
