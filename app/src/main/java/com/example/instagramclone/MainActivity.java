package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,View.OnKeyListener{
    TextView username;
    TextView password;
    Boolean signUpModeActive=true;
    TextView loginTextView;
    ImageView logoImageView;
    ConstraintLayout backgroudLayout;

   //function for user list
    public void showUserList(){
        Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }
   //handling the onclick
    public  void onClick(View view){

        if (view.getId()==R.id.loginTextView){
            Button signUpButton=findViewById(R.id.signUpButton);
            if(signUpModeActive){
                signUpModeActive=false;
                signUpButton.setText("Log In");
                loginTextView.setText("or, Sign Up");
            }else {
                signUpModeActive=true;
                signUpButton.setText("Sign Up");
                loginTextView.setText("or, Log In");
            }
        }else if(view.getId()==R.id.logoImageView || view.getId()==R.id.backgroudLayout) {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
    }
   //handling button signUPclick
    public void signUpClick(View view){
      if(username.getText().toString().matches("")||password.getText().toString().matches("")){
          Toast.makeText(this, "A username and a password is required", Toast.LENGTH_SHORT).show();
      }else{
          if (signUpModeActive) {
              ParseUser user = new ParseUser();
              user.setUsername(username.getText().toString());
              user.setPassword(password.getText().toString());
              user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {
                      if (e == null) {
                          Toast.makeText(MainActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                          showUserList();
                      } else {
                          Toast.makeText(MainActivity.this, "This Username is Already used", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }else {
              ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                      if(e==null &&  user!=null){
                          Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                          showUserList();

                      }else {
                          Toast.makeText(MainActivity.this, "Invalid Username and password", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }
      }
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        username=findViewById(R.id.usernameTextView);
        password=findViewById(R.id.passwordTextView);
        loginTextView=findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        password.setOnKeyListener(this);
        logoImageView=findViewById(R.id.logoImageView);
        backgroudLayout=findViewById(R.id.backgroudLayout);

        logoImageView.setOnClickListener(this);
        backgroudLayout.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            showUserList();
        }

        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

    //handling enter key pressed automatically sign in
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
           signUpClick(view);
        }
        return false;
    }
}