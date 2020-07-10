package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.parstagram.databinding.ItemPostBinding;

import static com.example.parstagram.PostsAdapter.POST;

public class PostDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        // Set up View Binding
        ItemPostBinding binding = ItemPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Display post information from intent
        Post post = getIntent().getParcelableExtra(POST);
        Post.displayPost(post, binding, this);
    }
}