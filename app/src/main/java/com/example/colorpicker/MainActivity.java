package com.example.colorpicker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MyRegulator2.OnRegulatorChangedListener {

    @BindView(R.id.vDemo)
    View vDemo;
    @BindView(R.id.r)
    MyRegulator2 r;
    @BindView(R.id.g)
    MyRegulator2 g;
    @BindView(R.id.b)
    MyRegulator2 b;
    @BindView(R.id.tvR)
    TextView tvR;
    @BindView(R.id.tvG)
    TextView tvG;
    @BindView(R.id.tvB)
    TextView tvB;

    private int red = 0, green = 0, blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        r.setInitialValue(0, 255, "");
        g.setInitialValue(0, 255, "");
        b.setInitialValue(0, 255, "");

        r.setListener(this);
        g.setListener(this);
        b.setListener(this);
    }

    @Override
    public void onRegulatorPercentageChanged(float percentage, MyRegulator2 regulator) {
        int temp = Math.round(255 * percentage);

        switch (regulator.getId()) {
            case R.id.r:
                red = temp;
                break;
            case R.id.g:
                green = temp;
                break;
            case R.id.b:
                blue = temp;
                break;
        }

        Loger.d("red=" + red + "     green=" + green + "     blue=" + blue);

        String redHex = int2Hex(red);
        String greenHex = int2Hex(green);
        String blueHex = int2Hex(blue);

        tvR.setText(redHex);
        tvG.setText(greenHex);
        tvB.setText(blueHex);

        vDemo.setBackgroundColor(Color.parseColor("#" + redHex + greenHex + blueHex));


    }


    public static String int2Hex(int v) {
        return String.format("%02X", v);
    }
}
