package com.example.torab.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CheckBox Yescheckbox;
    RatingBar rb;
    TextView value;
    EditText El_edit;
    Spinner spinner;
    private TextInputLayout textInputName;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputContact;
    private TextInputLayout textInputTechElab;
    private TextInputLayout textInputFeedback;

    TextInputLayout techElab;
    TextInputEditText errorEditText;

    EditText editName;
    EditText editEmail;
    EditText editContact;


    TextInputLayout inputFeedback;
    TextInputEditText errorFeedback;
    Button mSubmit;
    public String technology = "";
    public String ToNumber;
    public boolean sendSMS = false;
    public String senderName = "";
    private float rate = 0;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

            //.....Welcome Message.........//
            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);

            editName = (EditText) findViewById(R.id.editname);
            editEmail = (EditText) findViewById(R.id.editemail);
            editContact = (EditText) findViewById(R.id.editcontact);


            errorEditText = (TextInputEditText) findViewById(R.id.errorEditText);
            techElab = (TextInputLayout) findViewById(R.id.TechElab);
            mSubmit = (Button) findViewById(R.id.submit);


//            final Spinner spinner = (Spinner) findViewById(R.id.spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//            spinner.setOnItemSelectedListener(this);
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                    R.array.planets_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//            spinner.setAdapter(adapter);

        //.....Customized Spinner Adapter .........//
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

            //Getting the Ratingbar values
        rb = (RatingBar) findViewById(R.id.ratingbar);
        value = (TextView) findViewById(R.id.value);
//        value2 = (TextView) findViewById(R.id.value2);
//        value3 = (TextView) findViewById(R.id.value3);
//        value4 = (TextView) findViewById(R.id.value4);
//        value5 = (TextView) findViewById(R.id.value5);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                    rate = rating;

                    if( rating <= 1)
                        value.setText("OK");

                    else if (rating <= 2)
                        value.setText("Average");
                    else if (rating <= 3)
                        value.setText("Good");
                    else if (rating <= 4)
                        value.setText("Very Good");
                    else if (rating <= 5)
                        value.setText("Excellent");
                }

            });


        /* Checking the Checkbox makes other elements visible */
        Yescheckbox = (CheckBox) findViewById(R.id.YesCheckBox);

        Yescheckbox.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view)
                {
                    if (Yescheckbox.isChecked())
                    {
                        TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                        TechElob.setVisibility(View.VISIBLE);

                        TextView techid = (TextView) findViewById(R.id.techid);
                        techid.setVisibility(View.VISIBLE);

                        TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                        name.setVisibility(View.VISIBLE);

                        TextInputLayout email = (TextInputLayout) findViewById(R.id.email);
                        email.setVisibility(View.VISIBLE);

                        TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                        contact.setVisibility(View.VISIBLE);

                        Spinner spinner = (Spinner) findViewById(R.id.spinner);
                        spinner.setVisibility(View.VISIBLE);

                    } else if (!Yescheckbox.isChecked())
                    {
                        TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                        TechElob.setVisibility(View.GONE);

                        TextView techid = (TextView) findViewById(R.id.techid);
                        techid.setVisibility(View.GONE);

                        TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                        name.setVisibility(View.GONE);

                        TextInputLayout email = (TextInputLayout) findViewById(R.id.email);
                        email.setVisibility(View.GONE);

                        TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                        contact.setVisibility(View.GONE);

                        Spinner spinner = (Spinner) findViewById(R.id.spinner);
                        spinner.setVisibility(View.GONE);
                    }
                }


            });



            //......Submit Button that checks internet connectivity, validates Email and number and call Async method......//
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //...Checks network connection and shows a dialog box if theres no internet
                    if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();

                    else {
                            if(rate == 0)
                            {
                                buildDialog2(MainActivity.this).show();
                            }
                            else {


                                String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                                        "\\@" +

                                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                                        "(" +

                                        "\\." +

                                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                                        ")+";

                                String validnumber = "^[+]?[0-9]{10,13}$";
                                String mobilenumber = "^(\\+91[\\-\\s]?)?[6789]\\d{9}$";

                                String number = editContact.getText().toString();
                                String email = editEmail.getText().toString();

                                Matcher emailmatcher = Pattern.compile(validemail).matcher(email);
                                Matcher numbermatcher = Pattern.compile(validnumber).matcher(number);
                                Matcher mobilematcher = Pattern.compile(mobilenumber).matcher(number);

                                if (email.length() == 0 && number.length() == 0) {
                                    new SyncData().execute(confirmInput());
                                    errorEditText.setText("");
                                    errorFeedback.setText("");
                                    editName.setText("");
                                    editEmail.setText("");
                                    editContact.setText("");
                                    Yescheckbox.setChecked(false);
                                    rb.setRating(0F);
                                    spinner.setSelection(0);
                                    value.setText("");

                                    TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                    TechElob.setVisibility(View.GONE);

                                    TextView techid = (TextView) findViewById(R.id.techid);
                                    techid.setVisibility(View.GONE);

                                    TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                    name.setVisibility(View.GONE);

                                    TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                    emailtextinput.setVisibility(View.GONE);

                                    TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                    contact.setVisibility(View.GONE);

                                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                    spinner.setVisibility(View.GONE);

                                } else if (email.length() == 0 && number.length() > 0) {
                                    if (!numbermatcher.matches()) {
                                        //Toast.makeText(getApplicationContext(),"Enter a valid Contact Number",Toast.LENGTH_LONG).show();
                                        editContact.setError("Enter a valid Contact number");
                                    } else {
                                            if(!mobilematcher.matches()){
                                            new SyncData().execute(confirmInput());
                                            errorEditText.setText("");
                                            errorFeedback.setText("");
                                            editName.setText("");
                                            editEmail.setText("");
                                            editContact.setText("");
                                            Yescheckbox.setChecked(false);
                                            rb.setRating(0F);
                                            spinner.setSelection(0);
                                            value.setText("");

                                            TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                            TechElob.setVisibility(View.GONE);

                                            TextView techid = (TextView) findViewById(R.id.techid);
                                            techid.setVisibility(View.GONE);

                                            TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                            name.setVisibility(View.GONE);

                                            TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                            emailtextinput.setVisibility(View.GONE);

                                            TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                            contact.setVisibility(View.GONE);

                                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                            spinner.setVisibility(View.GONE);

                                        } else{
                                                sendSMS = true;
                                                ToNumber = textInputContact.getEditText().getText().toString();
                                                senderName = textInputName.getEditText().getText().toString();

                                                new SyncData().execute(confirmInput());
                                                errorEditText.setText("");
                                                errorFeedback.setText("");
                                                editName.setText("");
                                                editEmail.setText("");
                                                editContact.setText("");
                                                Yescheckbox.setChecked(false);
                                                rb.setRating(0F);
                                                spinner.setSelection(0);
                                                value.setText("");

                                                TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                                TechElob.setVisibility(View.GONE);

                                                TextView techid = (TextView) findViewById(R.id.techid);
                                                techid.setVisibility(View.GONE);

                                                TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                                name.setVisibility(View.GONE);

                                                TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                                emailtextinput.setVisibility(View.GONE);

                                                TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                                contact.setVisibility(View.GONE);

                                                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                                spinner.setVisibility(View.GONE);

                                            }

                                    }
                                } else if (email.length() > 0 && number.length() == 0) {
                                    if (!emailmatcher.matches()) {
                                        //Toast.makeText(getApplicationContext(),"Enter a valid Email address",Toast.LENGTH_LONG).show();
                                        editEmail.setError("Enter a valid Email ID");
                                    } else {
                                        new SyncData().execute(confirmInput());
                                        errorEditText.setText("");
                                        errorFeedback.setText("");
                                        editName.setText("");
                                        editEmail.setText("");
                                        editContact.setText("");
                                        Yescheckbox.setChecked(false);
                                        rb.setRating(0F);
                                        spinner.setSelection(0);
                                        value.setText("");

                                        TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                        TechElob.setVisibility(View.GONE);

                                        TextView techid = (TextView) findViewById(R.id.techid);
                                        techid.setVisibility(View.GONE);

                                        TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                        name.setVisibility(View.GONE);

                                        TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                        emailtextinput.setVisibility(View.GONE);

                                        TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                        contact.setVisibility(View.GONE);

                                        Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                        spinner.setVisibility(View.GONE);
                                    }
                                } else if (email.length() > 0 && number.length() > 0) {
                                    if (!emailmatcher.matches()) {
                                        //Toast.makeText(getApplicationContext(),"Enter a valid Email address",Toast.LENGTH_LONG).show();
                                        editEmail.setError("Enter a valid Email ID");
                                    } else if (!numbermatcher.matches()) {
                                        //Toast.makeText(getApplicationContext(),"Enter a valid Contact Number",Toast.LENGTH_LONG).show();
                                        editContact.setError("Enter a valid Contact number");
                                    } else {

                                        if(!mobilematcher.matches())
                                        {

                                            new SyncData().execute(confirmInput());
                                            errorEditText.setText("");
                                            errorFeedback.setText("");
                                            editName.setText("");
                                            editEmail.setText("");
                                            editContact.setText("");
                                            Yescheckbox.setChecked(false);
                                            rb.setRating(0F);
                                            spinner.setSelection(0);
                                            value.setText("");

                                            TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                            TechElob.setVisibility(View.GONE);

                                            TextView techid = (TextView) findViewById(R.id.techid);
                                            techid.setVisibility(View.GONE);

                                            TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                            name.setVisibility(View.GONE);

                                            TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                            emailtextinput.setVisibility(View.GONE);

                                            TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                            contact.setVisibility(View.GONE);

                                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                            spinner.setVisibility(View.GONE);

                                        } else{

                                            sendSMS = true;
                                            ToNumber = textInputContact.getEditText().getText().toString();
                                            senderName = textInputName.getEditText().getText().toString();

                                            new SyncData().execute(confirmInput());
                                            errorEditText.setText("");
                                            errorFeedback.setText("");
                                            editName.setText("");
                                            editEmail.setText("");
                                            editContact.setText("");
                                            Yescheckbox.setChecked(false);
                                            rb.setRating(0F);
                                            spinner.setSelection(0);
                                            value.setText("");

                                            TextInputLayout TechElob = (TextInputLayout) findViewById(R.id.TechElab);
                                            TechElob.setVisibility(View.GONE);

                                            TextView techid = (TextView) findViewById(R.id.techid);
                                            techid.setVisibility(View.GONE);

                                            TextInputLayout name = (TextInputLayout) findViewById(R.id.name);
                                            name.setVisibility(View.GONE);

                                            TextInputLayout emailtextinput = (TextInputLayout) findViewById(R.id.email);
                                            emailtextinput.setVisibility(View.GONE);

                                            TextInputLayout contact = (TextInputLayout) findViewById(R.id.contact);
                                            contact.setVisibility(View.GONE);

                                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                            spinner.setVisibility(View.GONE);

                                        }
                                    }
                                }
                            }
                    }

                }
            });


            //.....Checks the overflow of Technology elaboration Edit Text.....//
            errorEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() > techElab.getCounterMaxLength())
                        techElab.setError("Max character length is " + techElab.getCounterMaxLength());
                    else
                        techElab.setError(null);

                }
            });


            //.....Character count overflow message for Suggestions/Feedback......//
            errorFeedback = (TextInputEditText) findViewById(R.id.errorFeedback);
            inputFeedback = (TextInputLayout) findViewById(R.id.inputFeedback);

            errorFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() > inputFeedback.getCounterMaxLength())
                        inputFeedback.setError("Max character length is " + inputFeedback.getCounterMaxLength());
                    else
                        inputFeedback.setError(null);

                }
            });

            textInputName = (TextInputLayout) findViewById(R.id.name);
            textInputEmail = (TextInputLayout) findViewById(R.id.email);
            textInputContact = (TextInputLayout) findViewById(R.id.contact);
            textInputTechElab = (TextInputLayout) findViewById(R.id.TechElab);
            textInputFeedback = (TextInputLayout) findViewById(R.id.inputFeedback);


        }


    //....Method to check network connectivity
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }


    //.....Method to show a Dialog box if there's no internet....//
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wi-fi to access this. Press ok to Exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }


    //.....Method to check poor internet Connection....//
    public AlertDialog.Builder buildDialog1(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setTitle("Network error");
        builder.setMessage("Network error");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }


    public AlertDialog.Builder buildDialog2(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Sorry!");
        builder.setMessage("Empty feedback is not accepted. Click OK and try again");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }

    public AlertDialog.Builder buildDialog3(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setTitle("Thank you!");
        builder.setMessage("Your feedback has been recorded successfully");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }




    //......Calling the JSON Objects........//
    public JSONObject confirmInput()
    {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("rating", String.valueOf(rate));
            jsonObject.put("technology", technology);
            jsonObject.put("name", textInputName.getEditText().getText().toString());
            jsonObject.put("mobile", textInputContact.getEditText().getText().toString());
            jsonObject.put("email", textInputEmail.getEditText().getText().toString());
            jsonObject.put("feedback", textInputFeedback.getEditText().getText().toString());
            jsonObject.put("aboutTech", textInputTechElab.getEditText().getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    //Get the value selected value from Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        technology = parent.getItemAtPosition(position).toString();
       //Toast.makeText(parent.getContext(), "Selected: " + technology, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        Toast.makeText(parent.getContext(), "Nothing Selected ", Toast.LENGTH_LONG).show();
    }


    //Hides Keyboard when tapped anywhere on the screen
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //////Async Task to connect services

    class SyncData extends AsyncTask<JSONObject, String, String> {


        /*Before starting background thread Show Progress Dialog*/
        ProgressDialog pDialog;

        JSONParser_with_httpURLConnection jsonParser_with_httpURLConnection = new JSONParser_with_httpURLConnection();
        String url = "http://leaveapps.titan.co.in/TechnologyDay/Service1.svc/JSON/Feedback";
        //172.50.6.53
        JSONObject json = new JSONObject();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Submitting your feedback....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /* Creating product
        */
        protected String doInBackground(JSONObject... args)
        {
            try {

                Log.d("JSON Request", "JSONObject" + args[0].toString());

                json = jsonParser_with_httpURLConnection.makeHttpRequest(url, "POST", args[0]);
                Log.d("JSONRESPONSE1", json.toString());

                /// Sending SMS

                if(sendSMS) {


                    String SMSURL = "http://193.105.74.159/api/v3/sendsms/plain?user=titantrans&password=TqfND8rx&sender=TeamIT&SMSText=Thanks%20"+senderName.trim()+"%20for%20your%20feedback.%20Have%20a%20nice%20day.&GSM=91"+ToNumber.trim();

                    if (json.getBoolean("Timeout")) {
                        sendSMS = false;
                        ToNumber = "";
                        //buildDialog1(MainActivity.this).show();
                        return null;

                    } else {
                     json = jsonParser_with_httpURLConnection.makeHttpRequest(SMSURL,"GET",null);
                            sendSMS = false;
                            ToNumber = "";
                            json.put("Timeout", false);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /*  After completing background task Dismiss the progress dialog*/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            try {
                if (pDialog != null && pDialog.isShowing()) {

                    pDialog.dismiss();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (json.getBoolean("Timeout")) {

                    buildDialog1(MainActivity.this).show();
                    return;

                } else {

                    buildDialog3(MainActivity.this).show();

                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // finish();

        }

    }

}





