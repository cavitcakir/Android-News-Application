package edu.sabanciuniv.cavitcakirhomework3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView commentsRecView;
    CommentsAdapter adp;
    List<CommentItem> data;
    ProgressDialog prgDialog;
    String newsId;
    public static final int ADD_COMMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        data = new ArrayList<>();
        commentsRecView = findViewById(R.id.commentsrec);
        adp = new CommentsAdapter(data, this);
        commentsRecView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecView.setAdapter(adp);

        NewsTask tsk = new NewsTask();
        newsId = String.valueOf(getIntent().getSerializableExtra("selectednewid"));
        String my_url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + newsId;
        tsk.execute(my_url);


        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_media_previous);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.comments,menu);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_COMMENT){
            if(resultCode == RESULT_OK){
                NewsTask tsk = new NewsTask();
                newsId = String.valueOf(getIntent().getSerializableExtra("selectednewid"));
                String my_url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + newsId;
                tsk.execute(my_url);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId() == android.R.id.home){
            //finish();
            onBackPressed();
        }
        else if(item.getItemId()== R.id.mn_addcomment){

            Intent i = new Intent(this, AddCommentActivity.class);
            i.putExtra("selectednewid", newsId);
            startActivityForResult(i,ADD_COMMENT);

        }

        return true;

    }


    class NewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(CommentsActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                //Readers are for text reading
                //Streams are for binary data reading
                //Reader need streams to read characters

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line ="";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }


            } catch (MalformedURLException e) {
                Log.e("DEV",e.getMessage());
            } catch (IOException e) {
                Log.e("DEV",e.getMessage());
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();
            try {
                JSONObject obj = new JSONObject(s);

                if( obj.getInt("serviceMessageCode") == 1){
                    JSONArray arr =  obj.getJSONArray("items");

                    for (int i = 0; i < arr.length(); i++){
                        JSONObject current = (JSONObject) arr.get(i);

                        CommentItem item = new CommentItem(current.getInt("id"),
                                current.getString("text"),
                                current.getString("name")
                        );

                        data.add(item);
                    }
                }else{
                    // show alert dialog here
                }
                adp.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.e("DEV", String.valueOf(e));

            }
            prgDialog.dismiss();
        }
    }

}
