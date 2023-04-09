package com.zhaoxiao.zhiying.adapter.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zhaoxiao.zhiying.fragment.test.QuestionFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyTestAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    public MyTestAdapter(@NonNull @NotNull FragmentManager fm, ArrayList<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
