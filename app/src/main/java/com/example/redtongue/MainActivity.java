package com.example.redtongue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.redtongue.ui.main.FilesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redtongue.ui.main.SectionsPagerAdapter;

import org.w3c.dom.Attr;

import java.io.File;

public class MainActivity extends AppCompatActivity implements UI {

    public static Activity activity;
    private static final Object lock = new Object();
    public boolean amode;
    private Mode mode;
    public RedTongue red;
    private TabLayout tabs;
    private TextView selectPrint;
    private String selectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Permissions.verifyStoragePermissions(this);
        red = new RedTongue(this, "UnknownUser");

        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEND) || intent.getAction().equals(Intent.ACTION_SEND_MULTIPLE)) {
            amode = FileTransfer.SEND;
            System.out.println("HERE");
            //changeMode(Mode.NAME);
            if (red != null) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        red.start(FileTransfer.SEND);
                    }
                });
                t.start();
            }
            selectPrint = new TextView(getApplicationContext());
            onActivityResult(FilesFragment.REQUEST_FILE, 0, intent);
        }
        activity = this;
    }

    public static Activity getActivity() {
        return activity;
    }

    public void changeMode(Mode mode) {
        this.mode = mode;
        changeDisplay();
    }

    private void changeDisplay() {
        //TODO: paint GUI
        final Context c = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mode) {
                    case MODE:
                        break;
                    case NAME:
                        TabLayout.Tab tab = tabs.getTabAt(1);
                        tab.select();
                        TextView info = new TextView(getApplicationContext());
                        info.setText("Choose a device to pair to by typing one of " +
                                "the following names:\n");
                        LinearLayout pair_pane = findViewById(R.id.pair_pane);
                        pair_pane.removeAllViews();
                        pair_pane.addView(info);
                        break;
                    case WAIT:
                        tab = tabs.getTabAt(1);
                        tab.select();
                        info = new TextView(getApplicationContext());
                        info.setText("Searching for devices to connect to...");
                        pair_pane = findViewById(R.id.pair_pane);
                        pair_pane.removeAllViews();
                        pair_pane.addView(info);
                        break;
                    case FILE_S:
                        tab = tabs.getTabAt(2);
                        tab.select();
                        TextView file = new TextView(getApplicationContext());
                        file.setText("Choose the file you would like to send:");

                        Button fileChoose = new Button(getApplicationContext());
                        fileChoose.setText("Choose file");

                        if (selectPrint == null) {
                            selectPrint = new TextView(getApplicationContext());
                            selectPrint.setText("No file selected");
                        }
                        selectPrint.setMovementMethod(new ScrollingMovementMethod());
                        selectPrint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String oldText = selectPrint.getText().toString();
                                selectPrint.setText(selectText);
                                selectText = oldText;
                            }
                        });

                        Button send = new Button(getApplicationContext());
                        send.setText("send");
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (red != null && FilesFragment.selected != null) {
                                    Thread t = new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                for (int i = 0; i < FilesFragment.selected.length; i++) {
                                                    System.out.println("Transferring: "+FilesFragment.selected[i]);
                                                    red.transfer(FilesFragment.selected[i]);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    t.start();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Dialog.showMessageDialog("Warning - select a file to send", "You must select a file to send", c);
                                        }
                                    });
                                }
                            }
                        });

                        fileChoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                                chooseFile.setType("*/*");
                                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                                startActivityForResult(chooseFile, FilesFragment.REQUEST_FILE);
                            }
                        });

                        LinearLayout filesPanel = findViewById(R.id.files_pane);
                        filesPanel.removeAllViews();
                        filesPanel.addView(file);
                        filesPanel.addView(fileChoose);
                        filesPanel.addView(selectPrint);
                        filesPanel.addView(send);
                        break;
                    case FILE_R:
                        tab = tabs.getTabAt(2);
                        tab.select();

                        file = new TextView(getApplicationContext());
                        file.setText("Choose a location if you would like to change "+
                                "the save location\n\t(else default location will be used)");

                        fileChoose = new Button(getApplicationContext());
                        fileChoose.setText("Choose location");

                        selectPrint = new TextView(getApplicationContext());
                        if (red.getDefaultFile() != null) {
                            FilesFragment.selected = new File[1];
                            FilesFragment.selected[0] = red.getDefaultFile();
                            selectPrint.setText(red.getDefaultFile().toString());
                        } else {
                            selectPrint.setText("No location selected");
                        }

                        send = new Button(getApplicationContext());
                        send.setText("next");
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (red != null && FilesFragment.selected != null) {
                                    Thread t = new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                for (int i = 0; i < FilesFragment.selected.length; i++) {
                                                    System.out.println("Receiving: "+FilesFragment.selected[i]);
                                                    red.transfer(FilesFragment.selected[i]);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    t.start();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Dialog.showMessageDialog("Warning - select a file to send", "You must select a file to send", c);
                                        }
                                    });
                                }
                            }
                        });

                        fileChoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                                chooseFile.setType("*/*");
                                chooseFile = Intent.createChooser(chooseFile, "Choose a location");
                                startActivityForResult(chooseFile, FilesFragment.REQUEST_FILE);
                            }
                        });

                        filesPanel = findViewById(R.id.files_pane);
                        filesPanel.removeAllViews();
                        filesPanel.addView(file);
                        filesPanel.addView(fileChoose);
                        filesPanel.addView(selectPrint);
                        filesPanel.addView(send);
                        /*this.selected = null;
                        tabbedPane.setEnabledAt(2, true);
                        tabbedPane.setSelectedIndex(2);
                        file = new JLabel("Choose a location if you would like to change "+
                                "the save location\n\t(else default location will be used)");
                        if (red != null && red.getDefaultFile() != null) {
                            fileInput = new JFileChooser(red.getDefaultFile());
                        } else {
                            fileInput = new JFileChooser();
                        }
                        JButton folderChoose = new JButton("Choose location");
                        selectPrint = new JLabel("No file selected");
                        fileInput.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        folderChoose.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                fileInput.showDialog(null, "Select");
                                if (fileInput.getSelectedFile() != null) {
                                    selected = new File[1];
                                    selected[0] = fileInput.getSelectedFile();
                                    selectPrint.setText(printNames());
                                    filesPanel.repaint();
                                    filesPanel.revalidate();
                                }
                            }
                        });
                        send = new JButton("next");
                        send.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                if (red != null) {
                                    Thread t = new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                if (selected != null && selected.length > 0) {
                                                    red.transfer(selected[0]);
                                                } else {
                                                    red.transfer(null);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    t.start();
                                }
                            }
                        });
                        filesPanel.removeAll();
                        filesPanel.add(file);
                        filesPanel.add(folderChoose);
                        filesPanel.add(selectPrint);
                        filesPanel.add(send);*/
                        break;
                    case TRANSFER:
                        tab = tabs.getTabAt(3);
                        tab.select();
                /*tabbedPane.setEnabledAt(3, true);
                tabbedPane.setSelectedIndex(3);*/
                        break;

                    default:
                        //TODO: error
                }
            }
        });
    }

    public void display(char type, final String s) {
        switch (type) {
            case UI.INFO:
                System.out.println(s);
                break;
            case UI.MESSAGE:
                switch(mode) {
                    case NAME:
                        //To update UI on main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView name = new TextView(getApplicationContext());
                                name.setText(s);
                                final String n = s.split(" ")[0];
                                name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (red != null) {
                                            Thread t = new Thread(new Runnable() {
                                                public void run() {
                                                    red.pair(n);
                                                }
                                            });
                                            t.start();
                                        }
                                    }
                                });
                                LinearLayout pair_pane = findViewById(R.id.pair_pane);
                                pair_pane.addView(name);
                            }
                        });
                        break;
                    default:
                        System.out.println("MESSAGE: "+s);
                }
                break;
            case UI.WARNING: case UI.ERROR:
                System.err.println(s);
                break;
            case UI.POPUP:
                final Context c = this;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog.showMessageDialog("RedTongue - Pair number", s, c);
                    }
                });
                break;
            default:
                System.out.println(s);
        }
    }

    private String s = "";
    public String getInput(final String message) {
        final Context c = this;
        final EditText input = new EditText(c);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog.showInputDialog("Enter pair number", message, c, input, lock);
            }
        });


        System.out.println("Waiting");

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {}
        }

        s = input.getText().toString();
        System.out.println("Finished waiting: "+s);

        try {
            int i = Integer.parseInt(s);
            return s;
        } catch (Exception e) {
            return "-1";
        }
    }

    public Progress getProg() {
        return SectionsPagerAdapter.prog;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("CODE: "+requestCode);
        if (true || requestCode == FilesFragment.REQUEST_FILE) {
            selectText = "";
            if (null != data.getClipData()) {
                FilesFragment.selected = new File[data.getClipData().getItemCount()];
                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    String src = getPath(uri);
                    File source = new File(src);
                    if (mode == Mode.FILE_R) {
                        source = source.getParentFile();
                    }
                    FilesFragment.selected[i] = source;
                    selectText += FilesFragment.selected[i].getName()+", ";
                }
                selectText.substring(0, selectText.length()-3);
            } else {
                FilesFragment.selected = new File[1];
                Uri uri = data.getData();
                String src = getPath(uri);
                File source = new File(src);
                if (mode == Mode.FILE_R) {
                    source = source.getParentFile();
                }
                System.out.println(source.toString());
                FilesFragment.selected[0] = source;
                selectText = FilesFragment.selected[0].getName();
            }
            selectPrint.setText((selectText.length() > 35) ? selectText.substring(0, 35)+"..." : selectText);
        }
    }

    private String getPath(Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
}