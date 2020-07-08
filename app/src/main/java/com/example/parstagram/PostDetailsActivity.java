package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.databinding.ActivityPostDetailsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.parstagram.PostsAdapter.POST;

public class PostDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // Set up View Binding
        ActivityPostDetailsBinding binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Display post information from intent
        Post post = getIntent().getParcelableExtra(POST);
        binding.tvUsername.setText(post.getUser().getUsername());
        Glide.with(view)
                .load(post.getImage().getUrl())
                .into(binding.ivPostPic);
        binding.tvCaption.setText(post.getCaption());
        binding.tvRelativeTime.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    // Returns something like "22 hours ago"
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}