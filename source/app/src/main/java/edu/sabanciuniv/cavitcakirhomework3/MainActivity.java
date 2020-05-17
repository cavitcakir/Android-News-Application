package edu.sabanciuniv.cavitcakirhomework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class MainActivity extends AppCompatActivity {

    RecyclerView newsRecView;
    List<NewsItem> data;
    ProgressDialog prgDialog;
    NewsAdapter adp;
    ArrayAdapter<String>  adp2;
    Spinner spCat;
    List<CategoryItem> catData;
    List<String> catDataStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<>();
        catData = new ArrayList<>();
        catDataStr = new ArrayList<>();
        spCat = findViewById(R.id.spcat);
        newsRecView = findViewById(R.id.commentsrec);
        adp = new NewsAdapter(data, this, new NewsAdapter.NewsItemClickListener() {
            @Override
            public void newItemClicked(NewsItem selectedNewsItem) {
                Intent i = new Intent(MainActivity.this,NewsDetailActivity.class);
                i.putExtra("selectednew", selectedNewsItem);
                startActivity(i);
            }
        });
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);



        //context -> running activity

        adp2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item ,catDataStr);
        spCat.setAdapter(adp2);

        NewsTask2 tsk2 = new NewsTask2();
        tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getallnewscategories");

        NewsTask tsk = new NewsTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");


        getSupportActionBar().setTitle("News");

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NewsTask tsk = new NewsTask();
                String selectedCat = spCat.getSelectedItem().toString();
                if(selectedCat != "All") {
                    int pos = 0;
                    for(CategoryItem myObj : catData) {
                        if(selectedCat.equals(myObj.toString()))
                            break;
                        pos++;
                    }
                    String cat_url = "http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/" + catData.get(pos).getId();
                    Log.d("elma",cat_url);
                    tsk.execute(cat_url);
                }else{
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class NewsTask2 extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
//            prgDialog = new ProgressDialog(MainActivity.this);
//            prgDialog.setTitle("Loading");
//            prgDialog.setMessage("Please wait...");
//            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            prgDialog.show();
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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            catData.clear();
            catDataStr.add("All");
            try {
                JSONObject obj = new JSONObject(s);

                if( obj.getInt("serviceMessageCode") == 1){
                    JSONArray arr =  obj.getJSONArray("items");

                    for (int i = 0; i < arr.length(); i++){
                        JSONObject current = (JSONObject) arr.get(i);

                        CategoryItem item = new CategoryItem(current.getString("name"),
                                current.getInt("id")
                        );
                        Log.d("elma",item.toString());
                        catData.add(item);
                        catDataStr.add(item.toString());
                    }
                }else{
                    // show alert dialog here
                }
                adp2.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.e("DEV",e.getMessage());

            }
//            prgDialog.dismiss();
        }
    }

class NewsTask extends AsyncTask<String, Void, String>{

    @Override
    protected void onPreExecute() {
        prgDialog = new ProgressDialog(MainActivity.this);
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

                    long date = current.getLong("date");
                    Date objDate = new Date(date);

                    NewsItem item = new NewsItem(current.getInt("id"),
                            current.getString("title"),
                            current.getString("text"),
                            current.getString("image"),
                            objDate
                    );

                    data.add(item);
                }
            }else{
                // show alert dialog here
            }
            adp.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.e("DEV",e.getMessage());

        }
        prgDialog.dismiss();
    }
}


}
