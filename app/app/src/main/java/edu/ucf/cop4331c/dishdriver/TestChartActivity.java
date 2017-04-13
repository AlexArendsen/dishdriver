package edu.ucf.cop4331c.dishdriver;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import edu.ucf.cop4331c.dishdriver.helpers.MoneyFormatter;
import edu.ucf.cop4331c.dishdriver.models.RankedDishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

        final Date start = new Date(0);
        final Date end   = new Date();


        // TODO -- Please fill in the correct date arguments below
        RankedDishModel
            .between(SessionModel.currentRestaurant(), start, end)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<RankedDishModel>>() {
                @Override
                public void onCompleted() { }

                @Override
                public void onError(Throwable e) { }

                @Override
                public void onNext(List<RankedDishModel> ranked) {

                    // We need only the first five, and then we'll need the total earned
                    ranked = ranked.subList(0, 4);
                    for(RankedDishModel r : ranked) entries.add(new PieEntry(
                            r.getProfitEarned() / 100, // Make this an actual dollar amount
                            r.getName() + " × " + r.getTimesOrdered() + "\n(" + MoneyFormatter.format(r.getProfitEarned()) + ")"
                    ));

                    // TODO -- Put the dates into this title
                    // TODO -- Would be best to do this by adding a new method to DateFormatter and using that
                    final String title = "Five most profitable dishes between (dates)";

                    PieDataSet set = new PieDataSet(entries, title);
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
            });
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
