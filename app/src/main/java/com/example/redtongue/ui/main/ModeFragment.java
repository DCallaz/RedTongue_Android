package com.example.redtongue.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.redtongue.FileTransfer;
import com.example.redtongue.MainActivity;
import com.example.redtongue.Mode;
import com.example.redtongue.R;

import java.util.MissingFormatArgumentException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivity m;

    public ModeFragment(MainActivity m) {
        this.m = m;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     * @return A new instance of fragment ModeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModeFragment newInstance(MainActivity m) {
        ModeFragment fragment = new ModeFragment(m);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_mode, container, false);

        Button sendbtn = (Button)root.findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.amode = FileTransfer.SEND;
                m.changeMode(Mode.NAME);
                if (m.red != null) {
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            m.red.start(FileTransfer.SEND);
                        }
                    });
                    t.start();
                }
            }
        });
        Button recvbtn = root.findViewById(R.id.recvbtn);
        recvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.amode = FileTransfer.RECV;
                m.changeMode(Mode.WAIT);
                if (m.red != null) {
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            m.red.start(FileTransfer.RECV);
                        }
                    });
                    t.start();
                }
            }
        });

        // Inflate the layout for this fragment
        return root;
    }
}
