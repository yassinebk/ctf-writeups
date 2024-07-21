package com.hackfest.carkeypro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class MainActivity extends AppCompatActivity {
    public static final String flag = "aGFja2Zlc3R7ZGVmaW5pdGVseV9ub3RfdGhlX2ZsYWd9";
    public static final String pin = "rxU+PLGWKRScNsDZHRJVBw==\n";
    public CarKeyPro carKeyPro;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.carKeyPro = new CarKeyPro(getApplicationContext());
    }

    public void onClick(View view) {
        TextView flagTextView = (TextView) findViewById(R.id.flag);
        EditText pinInputView = (EditText) findViewById(R.id.pin);
        String check = this.carKeyPro.m44check(pinInputView.getText().toString());
        if (check.equals(pin)) {
            flagTextView.setText("hackfest{" + pinInputView.getText().toString() + "}");
        } else {
            flagTextView.setText("Incorrect PIN !");
        }
    }
}