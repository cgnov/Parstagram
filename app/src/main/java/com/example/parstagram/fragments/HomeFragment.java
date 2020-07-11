package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.databinding.FragmentHomeBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private static final String HOME = "Home";
    public final boolean IS_PROFILE;
    public final int NUM_REQUEST = 20;
    private final ParseUser USER;
    private FragmentHomeBinding mHomeBinding;
    private PostsAdapter mAdapter;

    public HomeFragment(boolean isProfile) {
        IS_PROFILE = isProfile;
        USER = ParseUser.getCurrentUser();
    }

    public HomeFragment(ParseUser user) {
        IS_PROFILE = true;
        USER = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set up view binding
        mHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return mHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView
        RecyclerView.LayoutManager layoutManager;
        EndlessRecyclerViewScrollListener scrollListener;
        if(IS_PROFILE) {
            // Displaying profile, want grid layout
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            mHomeBinding.rvPosts.setLayoutManager(gridLayoutManager);
            scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    queryPosts(page);
                }
            };
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mHomeBinding.rvPosts.setLayoutManager(linearLayoutManager);
            scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    queryPosts(page);
                }
            };
        }
        List<Post> allPosts = new ArrayList<>();
        mAdapter = new PostsAdapter(getContext(), allPosts, IS_PROFILE);
        mHomeBinding.rvPosts.setAdapter(mAdapter);

        // Set up endless scroll listener
        mHomeBinding.rvPosts.addOnScrollListener(scrollListener);

        mHomeBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                queryPosts();
            }
        });

        queryPosts();

        updateToolbar();
    }

    // Calling query posts for first time or refreshing. Don't skip posts
    private void queryPosts() {
        queryPosts(0);
    }

    protected void queryPosts(final int page) {
        mHomeBinding.tbProfile.getMenu().findItem(R.id.miActionProgress).setVisible(true);
        final ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CREATED);
        query.setLimit(NUM_REQUEST);
        query.setSkip(page*query.getLimit());
        if (IS_PROFILE) {
            query.whereEqualTo(Post.KEY_USER, USER);
        }
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                } else {
                    // Posts have been successfully queried, clear out old posts and replace
                    mAdapter.addAll(posts);
                    mHomeBinding.swipeContainer.setRefreshing(false);
                }
                mHomeBinding.tbProfile.getMenu().findItem(R.id.miActionProgress).setVisible(false);
            }
        });
    }

    protected void updateToolbar() {
        Toolbar toolbar = (Toolbar) mHomeBinding.tbProfile;
        toolbar.findViewById(R.id.logOutButton).setVisibility(View.GONE);
        if(IS_PROFILE) {
            toolbar.setTitle(USER.getUsername());
            if(USER.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                toolbar.findViewById(R.id.logOutButton).setVisibility(View.VISIBLE);
            }
        } else {
            toolbar.setTitle(HOME);
        }

    }
}