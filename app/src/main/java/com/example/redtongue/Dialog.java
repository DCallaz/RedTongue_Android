package com.example.redtongue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

public class Dialog {

    public static void showMessageDialog(String title, String message, Context appContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showInputDialog(String title, String message, Context appContext, EditText input, int input_type, final Object lock) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle(title);

        input.setInputType(input_type);
        builder.setView(input);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        /*synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }*/
    }
}
