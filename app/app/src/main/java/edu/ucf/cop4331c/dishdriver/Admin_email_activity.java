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

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        String subject = "";
        Calendar calStart, calEnd;
        //calEnd.setTime(Calendar.getInstance().getTime());
        calEnd = Calendar.getInstance();
        calStart = calEnd;

        AbstractViewRenderer page = new AbstractViewRenderer(this, R.layout.activity_test_chart) {
            private String _text;

            public void setText(String text) {
                _text = text;
            }

            @Override
            protected void initView(View view) {
                TextView tv_hello = (TextView)view.findViewById(R.id.pieChart);
                tv_hello.setText(_text);
            }
        };

        if (subject.isEmpty()) {
            subject = "30 Day Report";
        } else {
            subject = "30 Day Report for " + subject;
            String inputString = "11-2012";
            DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            try {
                calStart.setTime(dateFormat.parse(inputString));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.getTimeInMillis();

        cal.set(Calendar.DAY_OF_MONTH, 1);

        //calEnd = cal;
        calStart.add(Calendar.DAY_OF_MONTH, -30);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        RankedDishModel.between(SessionModel.currentRestaurant(), calStart.getTime(), new Date() ).subscribe(new Subscriber<List<RankedDishModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<RankedDishModel> rankedDishModels) {
                for (RankedDishModel rankedDishModel : rankedDishModels) {
                    String msg = "Item " + rankedDishModel.getName() + " sold " + rankedDishModel.getTimesOrdered() + " for " + rankedDishModel.getProfitEarned();

                    emailIntent.putExtra(Intent.EXTRA_TEXT,msg);
                    Toaster.toast(msg);
                }
                Toaster.toast("Found "+ rankedDishModels.size() +" dishes to rate.");

                if (rankedDishModels.isEmpty())
                    emailIntent.putExtra(Intent.EXTRA_TEXT,"Sorry you had no sales so no profit or favorite dish, maybe next month!");
                else
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Profit made: "+ rankedDishModels.get(0).getProfitEarned() +". Favorite dish this month was: "+ rankedDishModels.get(0).getName() +".");//TODO: add profit made this month and pull most ordered dish here
                //emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Sent email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Admin_email_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }//end of sendEmail()

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
