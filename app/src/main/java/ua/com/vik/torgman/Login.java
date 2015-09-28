package ua.com.vik.torgman;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity {
    final String LOG_TAG = "myLogs";
    DataBase dbHelper;
    Cursor cursor;
    EditText etPass;
    Spinner spinner;
    String name[] = {"Китай", "США", "Бразилия", "Россия", "Япония",
            "Германия", "Египет", "Италия", "Франция", "Канада"};
    String region[] = {"123", "124", "125", "126", "127",
            "128", "129", "321", "322", "333"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        spinner = (Spinner) findViewById(R.id.spinner);
        etPass = (EditText) findViewById(R.id.etPass);
        dbHelper = new DataBase(this);
        dbHelper.open();
        cursor = dbHelper.getAllData();
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else Log.d(LOG_TAG, "pysto");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        cursor.close();
        dbHelper.close();
    }

    public void LoginOk(View view) {
        dbHelper.open();
        cursor = dbHelper.getAllData();
        if (cursor.moveToFirst()) {
            do {
                if ((spinner.getSelectedItem().toString().equals(cursor.getString(1)))
                        & (etPass.getText().toString().equals(cursor.getString(2)))) {
                    cursor.moveToLast();
                    Intent intent = new Intent();
                    intent.putExtra("name", spinner.getSelectedItem().toString());
                    setResult(RESULT_OK, intent);
                    cursor.close();
                    dbHelper.close();
                    finish();
                }
            } while (cursor.moveToNext());
        }
        noPass();
        cursor.close();
        dbHelper.close();
    }

    private void noPass() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("TorgMan");
        alertDialogBuilder
                .setMessage("Введіть правильний пароль")
                .setCancelable(false)
                .setPositiveButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}