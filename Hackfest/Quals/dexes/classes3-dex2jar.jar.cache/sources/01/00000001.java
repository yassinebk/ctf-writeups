package com.hackfest.carkeypro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes3-dex2jar.jar:com/hackfest/carkeypro/MainActivity.class */
public class MainActivity extends AppCompatActivity {
    public static final String flag = "aGFja2Zlc3R7ZGVmaW5pdGVseV9ub3RfdGhlX2ZsYWd9";
    public static final String pin = "rxU+PLGWKRScNsDZHRJVBw==\n";
    public CarKeyPro carKeyPro;

    public void onClick(View view) {
        TextView textView = (TextView) findViewById(R.id.flag);
        EditText editText = (EditText) findViewById(R.id.pin);
        if (this.carKeyPro.check(editText.getText().toString()).equals(pin)) {
            textView.setText("hackfest{" + editText.getText().toString() + "}");
        } else {
            textView.setText("Incorrect PIN !");
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.carKeyPro = new CarKeyPro(getApplicationContext());
    }
}