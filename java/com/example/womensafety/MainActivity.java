package com.example.womensafety;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN =3000;

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView safety_key,slogan;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image=findViewById(R.id.image_tv);
        safety_key=findViewById(R.id.safety_tv);
        slogan=findViewById(R.id.slogan_tv);

        image.setAnimation(topAnim);
        safety_key.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);






      new Handler().postDelayed(() -> {
          Intent i=new Intent(MainActivity.this,Login.class);
          startActivity(i);
          finish();

      },SPLASH_SCREEN);

    }
}