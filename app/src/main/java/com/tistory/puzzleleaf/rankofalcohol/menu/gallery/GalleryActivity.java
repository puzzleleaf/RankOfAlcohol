package com.tistory.puzzleleaf.rankofalcohol.menu.gallery;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.tistory.puzzleleaf.rankofalcohol.R;


import butterknife.ButterKnife;


public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

    }

}
