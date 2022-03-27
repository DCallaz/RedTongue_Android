package com.example.redtongue.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.redtongue.MainActivity;
import com.example.redtongue.R;
public class PairFragment extends Fragment {
    private MainActivity m;

    public PairFragment(MainActivity m) {
        this.m = m;
        // Required empty public constructor
    }

    public static PairFragment newInstance(MainActivity m) {
        PairFragment fragment = new PairFragment(m);
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
        return inflater.inflate(R.layout.fragment_pair, container, false);
    }
}
