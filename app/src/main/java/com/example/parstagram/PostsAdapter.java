package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.databinding.ItemPostBinding;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    public static final String POST = "post";
    public static final String PROFILE_PIC = "profilePicture";
    private final boolean IS_PROFILE;
    private Context mContext;
    private List<Post> mPosts;

    public PostsAdapter(Context context, List<Post> posts, boolean isProfile) {
        mContext = context;
        mPosts = posts;
        IS_PROFILE = isProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // Clean all elements of the recycler (used for SwipeRefresh)
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add list of posts (used for SwipeRefresh)
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding mBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemPostBinding.bind(itemView);
        }

        public void bind(final Post post) {
            if(IS_PROFILE) {
                profileBind(post);
            } else {
                Post.displayPost(post, mBinding, mContext);
            }

            // Opens PostDetailsActivity of relevant post when picture is clkced
            mBinding.ivPostPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, PostDetailsActivity.class);
                    i.putExtra(POST, post);
                    mContext.startActivity(i);
                }
            });
        }

        private void profileBind(Post post) {
            // Simple square grid view, hide everything except post picture
            mBinding.ivUserPic.setVisibility(View.GONE);
            mBinding.tvCaption.setVisibility(View.GONE);
            mBinding.tvUsername.setVisibility(View.GONE);
            mBinding.tvTimestamp.setVisibility(View.GONE);

            Glide.with(mContext)
                    .load(post.getImage().getUrl())
                    .centerCrop() // center crops into square
                    .placeholder(R.color.colorPlaceholder)
                    .into(mBinding.ivPostPic);
        }

    }
}
