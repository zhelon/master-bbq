package com.expert.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.expert.myapp.pojo.User;
import com.expert.myapp.util.Settings;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class LoginActivity extends AppCompatActivity {


    EditText userEditText, passwordEditText;
    Button login;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Settings.PREFERENCES.getString(Settings.LOGIN,null) == "true"){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }else{
            userEditText = (EditText) findViewById(R.id.username);
            passwordEditText = (EditText)findViewById(R.id.password);
            layout = (LinearLayout)findViewById(R.id.layoutbonito);
            login = findViewById(R.id.button);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   

                    new LoginTask().execute();

                }
            });
        }

    }

    public class LoginTask extends AsyncTask<Void, String, Void>{
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        User user;
        ResponseEntity<JsonNode> responseEntity;
        String response = "{\"id\":2,\"role_id\":\"1\",\"name\":\"test\",\"email\":\"test@test.com\",\"auth_token\":\"e91a0b4211c05ae1ec8937845a203d1591691d22a02b628e20665a097bb09029\",\"email_verified_at\":null,\"active\":\"1\",\"verify\":\"0\",\"created_at\":\"2019-05-07 04:23:50\",\"updated_at\":\"2019-05-10 15:43:11\",\"role\":{\"id\":2,\"role_name\":\"ADMIN\",\"created_at\":\"2019-05-07 04:23:37\",\"updated_at\":\"2019-05-07 04:23:37\"}}";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            login.setEnabled(false);
            userEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                /*String url = Settings.BASE_URL+Settings.BASE_PORT+Settings.API+Settings.SERVICE_LOGIN;
                String credentials = userEditText.getText().toString()+"/"+ passwordEditText.getText().toString();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(url+credentials, JsonNode.class);
                if(responseEntity.getStatusCode() == HttpStatus.OK){*/
                user = mapper.readValue(response, User.class);


            } catch (Exception  e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            login.setEnabled(true);
            userEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            if(user != null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Login.........", Toast.LENGTH_SHORT).show();

                Settings.PREFERENCES_EDITOR.putString(Settings.LOGIN, "true");
                Settings.PREFERENCES_EDITOR.putString(Settings.USER_TOKEN, user.getAuthToken());
                Settings.PREFERENCES_EDITOR.commit();

                startActivity(intent);
                finish();


            }else{
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
