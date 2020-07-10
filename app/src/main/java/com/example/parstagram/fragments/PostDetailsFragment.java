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
import com.example.parstagram.databinding.ItemPostBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.parstagram.PostsAdapter.POST;

public class PostDetailsFragment extends Fragment {

    private ItemPostBinding mItemPostBinding;
    private final Post POST;

    public PostDetailsFragment(Post post) {
        POST = post;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set up view binding
        mItemPostBinding = ItemPostBinding.inflate(getLayoutInflater(), container, false);
        return mItemPostBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Post.displayPost(POST, mItemPostBinding, getContext());
    }
}