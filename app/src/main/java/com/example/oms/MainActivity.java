package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.oms.adapter.SliderAdapter;
import com.example.oms.adapter.VideoAdapter;
import com.example.oms.admin.model.SliderData;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.Vector;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //variables
    ImageView menuIcon;
    FloatingActionButton fab;

    //drawermenu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    Vector<YoutubeVideo>youtubeVideos=new Vector<YoutubeVideo>();
    LinearLayoutManager HorizontalLayout;
    CircularImageView circularImageView;


    String url1 = "https://i.postimg.cc/QMHhw1Lw/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner.png";
    String url2 = "https://i.postimg.cc/tCKcp6V8/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner-1.png";
    String url3 = "https://i.postimg.cc/VLC7GNHg/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner-2.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularImageView = findViewById(R.id.floatwsbtn);
        circularImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float xDown=0, yDown=0;
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        xDown=event.getX();
                        yDown=event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float moveX,moveY;
                        moveX=event.getX();
                        moveY=event.getY();

                        float distanceX=moveX-xDown;
                        float distanceY=moveY-yDown;

                        circularImageView.setX(circularImageView.getX()+distanceX);
                        circularImageView.setY(circularImageView.getY()+distanceY);

                        xDown=moveX;
                        yDown=moveY;
                        break;
                }
                return false;
            }
        });

        //youtube recycler
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //youtubeVideos.add(new YoutubeVideo());
        youtubeVideos.add(new YoutubeVideo("<iframe width=\"250\" height=\"200\" src=\"https://www.youtube.com/embed/KtfSq9Te2sU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideos.add(new YoutubeVideo("<iframe width=\"250\" height=\"200\" src=\"https://www.youtube.com/embed/hfReofDo2TI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideos.add(new YoutubeVideo("<iframe width=\"250\" height=\"200\" src=\"https://www.youtube.com/embed/3AkjzDHuWKM\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideos.add(new YoutubeVideo("<iframe width=\"250\" height=\"200\" src=\"https://www.youtube.com/embed/zNZzyVD0LzY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideos.add(new YoutubeVideo("<iframe width=\"250\" height=\"200\" src=\"https://www.youtube.com/embed/wxyGeUkPYFM\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(videoAdapter);



        //slider
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = findViewById(R.id.slider);
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        circularImageView = findViewById(R.id.floatwsbtn);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wpurl = "https://wa.me/+60197062404?text=Hello ShopShip...";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wpurl));
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationuser);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;

                    case R.id.nav_catalogue:
                        startActivity(new Intent(getApplicationContext(), CatalogueUser.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_track:
                        startActivity(new Intent(getApplicationContext(), TrackerUser.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), profileUser.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        menuIcon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nav_homee);


        navigationDrawer();

        }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_homee);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);

            }
        });
    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}