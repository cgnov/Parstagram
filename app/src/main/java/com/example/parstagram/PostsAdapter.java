package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.fragments.HomeFragment;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    public static final String POST = "post";
    public static final String PROFILE_PIC = "profilePicture";

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler (used for SwipeRefresh)
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add list of posts (used for SwipeRefresh)
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivPostPic;
        private TextView tvCaption;
        private ImageView ivUserPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPostPic = itemView.findViewById(R.id.ivPostPic);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            ParseFile profilePicture = post.getUser().getParseFile(PROFILE_PIC);
            if(profilePicture == null) {
                Glide.with(context).load(R.drawable.instagram_user_filled_24).circleCrop().into(ivUserPic);
            } else {
                Glide.with(context).load(profilePicture.getUrl()).circleCrop().placeholder(R.drawable.instagram_user_filled_24).into(ivUserPic);
            }
            Glide.with(context).load(post.getImage().getUrl()).placeholder(R.color.colorPlaceholder).into(ivPostPic);
            ivPostPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostDetailsActivity.class);
                    i.putExtra(POST, post);
                    context.startActivity(i);
                }
            });
        }
    }
}
