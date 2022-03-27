package com.example.redtongue.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.redtongue.MainActivity;
import com.example.redtongue.R;

import java.io.File;

public class FileChooser extends Fragment {
    private static int REQUEST_FILE = 1;
    private MainActivity m;

    //Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
    //                    chooseFile.setType("*/*");
    //chooseFile = Intent.createChooser(chooseFile, "Choose a file");
    //startActivityForResult(chooseFile, REQUEST_FILE);


    public FileChooser(MainActivity m) {
        this.m = m;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE && resultCode == m.RESULT_OK) {
            Uri uri = data.getData();
            String src = uri.getPath();
            File source = new File(src);
        }
    }
}
