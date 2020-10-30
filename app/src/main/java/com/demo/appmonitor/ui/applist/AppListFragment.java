package com.demo.appmonitor.ui.applist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.appmonitor.R;

public class AppListFragment extends Fragment {

    private AppListViewModel appListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appListViewModel =
                ViewModelProviders.of(this).get(AppListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_applist, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        appListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}