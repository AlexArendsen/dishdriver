package edu.ucf.cop4331c.dishdriver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.net.Uri;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class Admin_email_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_email_activity);

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
        Log.i("Send email", "");
        String[] TO = {R.id.emailToInput};
        //check if they even had a CC'd email
        if(R.id.ccEmailAddressInput.length() > 1) {
            String[] CC = {R.id.ccEmailAddressInput};
        }
        else{
            String[] CC = {""};
        }
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        //subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Monthly Report for ____");//TODO: add month, year entered (@id/startDateInput)
        //message
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Profit made: ____. Favorite dish this month was: ____.");//TODO: add profit made this month and pul most orered dish here

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Admin_email_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }//end of sendEmail()
}
