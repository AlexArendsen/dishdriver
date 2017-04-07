package edu.ucf.cop4331c.dishdriver;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import xdroid.toaster.Toaster;

/**
 * Created by ash on 4/6/17.
 */

public class TestChartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chart);
        ButterKnife.bind(this);

        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(18.5f, "One"));
        entries.add(new PieEntry(26.7f, "Two"));
        entries.add(new PieEntry(24.0f, "Three"));
        entries.add(new PieEntry(30.8f, "Four"));
        entries.add(new PieEntry(10.8f, "Other"));

        PieDataSet set = new PieDataSet(entries, "Election Results");
        PieData data = new PieData(set);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.getLegend().setTextSize(20f);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(35f);
        pieChart.setCenterTextSize(500f);
        pieChart.setDrawSliceText(true);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
        pieChart.saveToGallery("report.png",100);
    }
    @Override
    public void onBackPressed() {

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = this.getExternalCacheDir() + "/report.png";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toaster.toast(mPath);
        } catch (Throwable e) {
            Toaster.toast("damn...");
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        super.onBackPressed();
    }

}
