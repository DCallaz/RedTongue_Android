package com.example.redtongue.ui.main;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.redtongue.MainActivity;
import com.example.redtongue.Progress;
import com.example.redtongue.R;

import java.util.List;

public class TransferFragment extends Fragment implements Progress {
    private MainActivity m;
    private int reps;
    private int currentRep = 0;
    private int fragment;
    private int prog = 0;
    private ProgressBar bar;

    public TransferFragment(MainActivity m) {
        this.m = m;
        this.reps = 1;
        this.fragment = 100;
        // Required empty public constructor
    }

    public static TransferFragment newInstance(MainActivity m) {
        TransferFragment fragment = new TransferFragment(m);
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfer, container, false);
        bar = root.findViewById(R.id.progress_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bar.setMin(0);
        }
        bar.setMax(100);
        // Inflate the layout for this fragment
        return root;
    }

    public void start(int reps) {
        this.reps = reps;
        this.fragment = 100/reps;
        bar.setProgress(0);
    }

    public void updateProgress(short percent)
    {
        if (currentRep == reps -1 && percent == 100) {
            bar.setProgress(100);
            prog = percent;
        } else {
            int tempProg = currentRep*fragment + (percent*fragment/100);
            if (tempProg != prog) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    bar.setProgress(tempProg, true);
                } else {
                    bar.setProgress(tempProg);
                }
                prog = tempProg;
            }
        }
    }

    public void incRep() {
        if (currentRep < reps - 1) {
            currentRep++;
        }
    }
}
