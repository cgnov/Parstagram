package com.example.parstagram;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.parstagram.databinding.ItemPostBinding;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.parstagram.PostsAdapter.PROFILE_PIC;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_CAPTION = "caption";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED = "createdAt";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) { put(KEY_USER, user); }
    
    public static void displayPost(Post post, ItemPostBinding binding, Context context) {
        binding.tvUsername.setText(post.getUser().getUsername());
        if(post.getCaption().isEmpty()) {
            binding.tvCaption.setVisibility(View.GONE);
        } else {
            binding.tvCaption.setText(Html.fromHtml("<b>" + post.getUser().getUsername() + "</b> " + post.getCaption()));
        }
        binding.tvTimestamp.setText(getSimpleDateTime(post.getCreatedAt().toString()));

        // Display user-uploaded avatar or default avatar
        ParseFile profilePicture = post.getUser().getParseFile(PROFILE_PIC);
        if(profilePicture == null) {
            binding.ivUserPic.setImageResource(R.drawable.instagram_user_filled_24);
        } else {
            Glide.with(context)
                    .load(profilePicture.getUrl())
                    .circleCrop()
                    .placeholder(R.drawable.instagram_user_filled_24)
                    .into(binding.ivUserPic);
        }

        // Display post image in original proportions
        Glide.with(context)
                .load(post.getImage().getUrl())
                .placeholder(R.color.colorPlaceholder)
                .into(binding.ivPostPic);
    }

    // Takes datetime string like "Mon Apr 01 21:16:23 +0000 2014" and returns  "April 1, 2014 9:16 PM"
    public static String getSimpleDateTime(String oldFormatDate) {
        String oldFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String newFormat = "MMMM d, yyyy h:mm aa";
        SimpleDateFormat sf = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String simpleDate = "";
        try {
            Date date = sf.parse(oldFormatDate);
            sf.applyPattern(newFormat);
            simpleDate = sf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return simpleDate;
    }
}
