package edu.sabanciuniv.cavitcakirhomework3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddCommentActivity extends AppCompatActivity {
    EditText txtName;
    TextView txtMessage;
    String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomment);

        txtName = findViewById(R.id.txtname);
        txtMessage = findViewById(R.id.txtmessage);
        newsId = String.valueOf(getIntent().getSerializableExtra("selectednewid"));

        getSupportActionBar().setTitle("Post Commment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_media_previous);

    }


    public void toResultClicked(View v){
        JsonTask tsk = new JsonTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",
                txtName.getText().toString(),
                txtMessage.getText().toString(),
                newsId
        );

        Intent i = new Intent(this, CommentsActivity.class);
        setResult(RESULT_OK,i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            //onBackPressed();
        }

        return true;

    }


    class JsonTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {

            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String text = strings[2];
            String news_id = strings[3];

            JSONObject obj = new JSONObject();
            try {
                obj.put("name",name);
                obj.put("text",text);
                obj.put("news_id",news_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());


                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line ="";

                    while((line = reader.readLine())!=null){
                        strBuilder.append(line);
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject inputObj = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
