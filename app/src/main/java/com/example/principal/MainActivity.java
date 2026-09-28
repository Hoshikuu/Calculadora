package com.example.principal;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt1;
    TextView print;

    double result = 0;
    double num = 0;
    boolean last_is_equal = false;
    String operation = "";

    Button[] btn_num = new Button[10];
    Button[] btn_op = new Button[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt1 = findViewById(R.id.textView);
        txt1.setText(String.valueOf(result));
        print = findViewById(R.id.textView3);

        for (int i = 0; i < 10; i++) {
            String test = "num" + Integer.toString(i);
            Resources res = getResources();
            String pak = getPackageName();
            int resId = res.getIdentifier(test, "id", pak);
            btn_num[i] = findViewById(resId);
            btn_num[i].setOnClickListener(this);
        }

        for (int i = 1; i < 5; i++) {
            String test = "op" + Integer.toString(i);
            Resources res = getResources();
            String pak = getPackageName();
            int resId = res.getIdentifier(test, "id", pak);
            btn_op[i] = findViewById(resId);
            btn_op[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Resources res = v.getResources();

        if (res.getResourceEntryName(btn.getId()).contains("num")) {
            if (last_is_equal) {
                txt1.setText("0.0");
                last_is_equal = false;
            }
            CharSequence tempTxt = txt1.getText();
            String str;
            if (tempTxt.equals("0.0")) {
                str = (String) btn.getText();
            }
            else {
                str = (String) tempTxt + (String) btn.getText();
            }
            txt1.setText(str);
        }

        if (res.getResourceEntryName(btn.getId()).contains("op")) {
            if (((String) btn.getText()).equals("C")) {
                print.setText(R.string.history);
                txt1.setText("0.0");
                num = 0;
                operation = "";
                last_is_equal = true;
                return;
            }

            try {
                num = Double.parseDouble((String) txt1.getText());
            }
            catch (Exception e) {
                num = 0;
            }
            print.setText(String.format("%s%s ", print.getText(), String.valueOf(num)));
            if (((String) btn.getText()).equals("=")) {
                if (operation.equals("/") && num == 0) {
                    txt1.setText(R.string.zero_division);
                    print.setText(String.format("%s = %s\n", print.getText(), "Syntax Error"));
                    result = 0;
                    num = 0;
                    operation = "";
                    last_is_equal = true;
                    return;
                }
                result = calc(result, num, operation);
                print.setText(String.format("%s = %s\n", print.getText(), String.valueOf(result)));
                txt1.setText(String.valueOf(result));
                num = 0;
                operation = "";
                last_is_equal = true;
                return;
            }

            if (!operation.isEmpty()) {
                if (operation.equals("/") && num == 0) {
                    txt1.setText(R.string.zero_division);
                    print.setText(String.format("%s = %s\n", print.getText(), "Syntax Error"));
                    result = 0;
                    num = 0;
                    operation = "";
                    last_is_equal = true;
                    return;
                }
                result = calc(result, num, operation);
                print.setText(String.format("%s = %s\n", print.getText(), String.valueOf(result)));
                txt1.setText("0.0");
                num = 0;
            }

            if (operation.isEmpty()) {
                result = num;
                txt1.setText("0.0");
                num = 0;
            }

            operation = (String) btn.getText();
            print.setText(String.format("%s%s ", print.getText(), operation));
        }
    }

    private double calc(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                return num1;
        }
    }
}