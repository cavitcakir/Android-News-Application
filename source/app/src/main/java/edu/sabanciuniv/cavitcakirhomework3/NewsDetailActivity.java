package edu.sabanciuniv.cavitcakirhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class NewsDetailActivity extends AppCompatActivity {

    ImageView imgPlanet;
    TextView txtDescription;
    TextView txtTitle;
    TextView txtDate;
    NewsItem selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        imgPlanet = findViewById(R.id.imgnewsdetail);
        txtDescription = findViewById(R.id.txtnewsdesc);
        txtTitle = findViewById(R.id.txttitle);
        txtDate = findViewById(R.id.txtdate);

        //Selected planet object will be sent to this activity
        selected = (NewsItem) getIntent().getSerializableExtra("selectednew");

        imgPlanet.setImageBitmap(selected.getBitmap());

        if(selected.getBitmap() == null){
            new ImageDownloadTask(imgPlanet).execute(selected);
        }else{
            imgPlanet.setImageBitmap(selected.getBitmap());
        }


        txtDescription.setText(selected.getText());
        txtTitle.setText(selected.getTitle());
        txtDate.setText(new SimpleDateFormat("dd/MM/yyy").format(selected.getNewsDate()));

        getSupportActionBar().setTitle("News Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_media_previous);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== R.id.mn_addcomment){

            Intent i = new Intent(this, CommentsActivity.class);
            i.putExtra("selectednewid", selected.getId());
            startActivity(i);

        }
        else if(item.getItemId() == android.R.id.home){
            //finish();
            onBackPressed();
        }

        return true;

    }
}
