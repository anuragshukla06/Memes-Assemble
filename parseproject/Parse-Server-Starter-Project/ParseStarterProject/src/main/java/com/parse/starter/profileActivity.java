package com.parse.starter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class profileActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText emailEditText;
    EditText locationEditText;
    EditText bdayEditText;
    String name;
    String email;
    String location;
    String bday;

    void buttonClicked(View view){
        Log.i("msg", "buttonClicked");
        if(emailEditText.length()!=0 && nameEditText.length()!=0 && locationEditText.length()!=0){

            name = nameEditText.getText().toString();
            email = emailEditText.getText().toString();
            location = locationEditText.getText().toString();
            bday = bdayEditText.getText().toString();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
            query.whereEqualTo("email", email);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){
                        if(objects.size()>0) {
                            Toast.makeText(getApplicationContext(), "E-Mail already exists.", Toast.LENGTH_SHORT).show();
                        } else{
                            ParseObject newUser = new ParseObject("Profile");
                            newUser.put("username", ParseUser.getCurrentUser().getUsername());
                            newUser.put("name", name);
                            newUser.put("location", location);
                            newUser.put("email", email);
                            newUser.put("bday", bday);
                            newUser.put("isProfileCompelete", true);
                            try {
                                newUser.save();
                            } catch (Exception exp){
                                Toast.makeText(getApplicationContext(), exp.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else{
            Toast.makeText(getApplicationContext(), "Field marked with asterisk are compulsory.", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        bdayEditText = (EditText) findViewById(R.id.bdayEditText);

    }
}
