package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bNavigation;
    private MenuItem miHome;
    private MenuItem miCompose;
    private MenuItem miProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bNavigation = findViewById(R.id.bNavigation);
        miHome = bNavigation.getMenu().findItem(R.id.action_home);
        miCompose = bNavigation.getMenu().findItem(R.id.action_compose);
        miProfile = bNavigation.getMenu().findItem(R.id.action_profile);

        setUpNavigationSelectedListeners(bNavigation);
        bNavigation.setSelectedItemId(R.id.action_home);
    }

    private void setUpNavigationSelectedListeners(BottomNavigationView bNavigation) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        bNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Reset icons to unclicked version
                miHome.setIcon(R.drawable.instagram_home_outline_24);
                miCompose.setIcon(R.drawable.instagram_new_post_outline_24);
                miProfile.setIcon(R.drawable.instagram_user_outline_24);

                // Check which item was clicked, change icon and start fragment
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        item.setIcon(R.drawable.instagram_home_filled_24);
                        fragment = new HomeFragment(false);
                        break;
                    case R.id.action_compose:
                        item.setIcon(R.drawable.instagram_new_post_filled_24);
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                        item.setIcon(R.drawable.instagram_user_filled_24);
                        fragment = new HomeFragment(true);
                        break;
                    default:
                        Log.e(TAG, "Did not recognize item clicked");
                        break;
                }
                if(fragment!=null) {
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
                return true;
            }
        });
    }

    public void onLogOutButtonClicked(MenuItem mi) {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}