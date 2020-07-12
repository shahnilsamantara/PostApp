package com.shahnil.post;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);









        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedfragment = new FragmentHome();
                            break;
                        case R.id.nav_categories:

                            selectedfragment = new FragmentCreatePost();
                            break;
                        case R.id.nav_profile:
                            selectedfragment = new FragmentProfile();
                            break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedfragment).commit();

                    return  true;

                }
    };
}

