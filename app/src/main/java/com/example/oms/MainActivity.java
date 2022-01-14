package com.example.oms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.oms.Prevalent.Prevalent;
import com.example.oms.adapter.SliderAdapter;
import com.example.oms.adapter.VideoAdapter;
import com.example.oms.admin.DashboardAdmin;
import com.example.oms.admin.DropshipList;
import com.example.oms.admin.model.SliderData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //variables
    ImageView menuIcon;
    FloatingActionButton fab;

    //drawermenu
    Button textView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    Vector<YoutubeVideo>youtubeVideos=new Vector<YoutubeVideo>();
    LinearLayoutManager HorizontalLayout;
    CircularImageView circularImageView;
    TextView points;
    private DatabaseReference mDatabase;


    String url1 = "https://i.postimg.cc/QMHhw1Lw/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner.png";
    String url2 = "https://i.postimg.cc/tCKcp6V8/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner-1.png";
    String url3 = "https://i.postimg.cc/VLC7GNHg/Red-and-Gold-Classy-and-Elegant-Business-Christmas-Banner-2.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        points = findViewById(R.id.pointcollect);
        textView = findViewById(R.id.viewreward);

        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        initView();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ViewOrders")
                .child(Prevalent.currentUser.getUsername())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int sum = 0;

                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Map<String,Object> map = (Map<String, Object>) snapshot1.getValue();
                            Object point = map.get("points");
                            int pValue = Integer.parseInt(String.valueOf(point));
                            sum += pValue;

                            points.setText(String.valueOf(sum) + "pts");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.nav_homee:
                        break;
                    case R.id.nav_rank:
                        Intent intent = new Intent(MainActivity.this, Leaderboard.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "Share is clicked",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_rate:
                        Toast.makeText(MainActivity.this, "Rate us is Clicked",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;

                }
                return true;
            }
        });

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

    }

    private void initView() {
        findViewById(R.id.viewreward).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewreward:
                openPopUpActivity();
                break;
        }
    }

    private void openPopUpActivity() {
        Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
        startActivity(intent);
    }

}