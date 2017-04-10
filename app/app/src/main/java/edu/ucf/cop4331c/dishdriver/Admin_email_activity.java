package edu.ucf.cop4331c.dishdriver;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.net.Uri;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ucf.cop4331c.dishdriver.models.RankedDishModel;
import edu.ucf.cop4331c.dishdriver.models.SessionModel;
import rx.Subscriber;
import xdroid.toaster.Toaster;

public class Admin_email_activity extends AppCompatActivity {
    private static AppCompatActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_email_activity);
        instance = this;

        //button onclick
        final Button button = (Button) findViewById(R.id.SendEmail);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //performing action to send email to
                sendEmail();

            }
        });
    }

    protected void sendEmail() {

        EditText emailToInput = (EditText) findViewById(R.id.emailToInput);
        EditText emailCC      = (EditText) findViewById(R.id.ccEmailAddressInput);

        Log.i("Send email", "");
        String[] TO = { emailToInput.getText().toString() };
        String[] CC = { emailCC.getText().toString() };

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        //subject
        String subject = "30 Day Report";
        Calendar calStart, calEnd;
        //calEnd.setTime(Calendar.getInstance().getTime());
        calEnd = Calendar.getInstance();
        calStart = calEnd;

        AbstractViewRenderer page = new AbstractViewRenderer(getBaseContext(), R.layout.activity_test_chart) {
            private String _text;

            public void setText(String text) {
                _text = text;
            }

            @Override
            protected void initView(View view) {
                PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);

                List<PieEntry> entries = new ArrayList<>();
                int pennies[] = { 71771, 12543, 11454, 9999, 40000 };
                String names[]= { "Buffalo Wings", "Nachos (w/ Cream)", "A Burger!", "A Slice of Pie", "Viv's Vejan Tiramisu" };


                entries.add(new PieEntry((float) pennies[0], names[0]));
                entries.add(new PieEntry((float) pennies[1], names[1]));
                entries.add(new PieEntry((float) pennies[2], names[2]));
                entries.add(new PieEntry((float) pennies[3], names[3]));
                entries.add(new PieEntry((float) pennies[4], names[4]));


                PieDataSet set = new PieDataSet(entries, "");
                PieData data = new PieData(set);
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(40f);

                Legend l = pieChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setTextSize(40f);
                l.setDrawInside(false);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);
                pieChart.setUsePercentValues(true);
                pieChart.setEntryLabelTextSize(0f);
                pieChart.setCenterTextSize(500f);
                //pieChart.setDrawSliceText(true);
                pieChart.setHoleRadius(0f);
                pieChart.setTransparentCircleRadius(0f);
                pieChart.setData(data);
                pieChart.invalidate(); // refresh
            }
        };


        new PdfDocument.Builder(getBaseContext()).addPage(page).filename("report").orientation(PdfDocument.A4_MODE.LANDSCAPE)
                .progressMessage(R.string.app_name).progressTitle(R.string.app_name).renderWidth(1000).renderHeight(600)
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                        emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                            Log.i("Sent email...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Admin_email_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                    }
                }).create().createPdf(this);

        //calEnd = cal;
        calStart.add(Calendar.DAY_OF_MONTH, -30);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);


//        RankedDishModel.between(SessionModel.currentRestaurant(), calStart.getTime(), new Date() ).subscribe(new Subscriber<List<RankedDishModel>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<RankedDishModel> rankedDishModels) {
//                for (RankedDishModel rankedDishModel : rankedDishModels) {
//                    String msg = "Item " + rankedDishModel.getName() + " sold " + rankedDishModel.getTimesOrdered() + " for " + rankedDishModel.getProfitEarned();
//
//                    Toaster.toast(msg);
//                }
//
//                if (rankedDishModels.isEmpty())
//                    emailIntent.putExtra(Intent.EXTRA_TEXT,"Sorry you had no sales so no profit or favorite dish, maybe next month!");
//                else
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Profit made: "+ rankedDishModels.get(0).getProfitEarned() +". Favorite dish this month was: "+ rankedDishModels.get(0).getName() +".");//TODO: add profit made this month and pull most ordered dish here
//                //emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
//                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//
//                try {
//                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
////                    finish();
//                    Log.i("Sent email...", "");
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(Admin_email_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
    }//end of sendEmail()
}