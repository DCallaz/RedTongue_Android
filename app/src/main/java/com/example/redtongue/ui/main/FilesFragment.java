package com.example.redtongue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.redtongue.Dialog;
import com.example.redtongue.MainActivity;
import com.example.redtongue.R;
import com.example.redtongue.RedTongue;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class FilesFragment extends Fragment {
    public static int REQUEST_FILE = 1;
    private MainActivity m;
    public static volatile File[] selected;

    public FilesFragment(MainActivity m) {
        this.m = m;
        // Required empty public constructor
    }

    public static FilesFragment newInstance(MainActivity m) {
        FilesFragment fragment = new FilesFragment(m);
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_files, container, false);
        // Inflate the layout for this fragment
        return root;
    }
}
