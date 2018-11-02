package com.example.hara.learninguimusicapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        tabLayout = view.findViewById(R.id.music_tab);
        viewPager = view.findViewById(R.id.viewpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new ChatFragment(), "Chat");
        adapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}