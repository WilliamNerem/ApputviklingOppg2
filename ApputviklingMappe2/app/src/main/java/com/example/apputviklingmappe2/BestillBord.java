package com.example.apputviklingmappe2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class BestillBord extends AppCompatActivity {

    private TimePickerDialog timePickerDialog;
    private Button timeButton;
    private Button friendsButton;
    private Spinner restaurantSpinner;
    private DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bestill_bord);
        initTimePicker();
        timeButton = findViewById(R.id.choseTime);
        timeButton.setText(getCurrentTime());
        friendsButton = findViewById(R.id.chooseFriend);
        restaurantSpinner = (Spinner) findViewById(R.id.chooseRestaurant);
        friendsButtonOnclick();
        db = new DBHandler(this);
        setSpinner();
    }

    public void addinDB(Venn venn) {
        Bestilling bestilling = new Bestilling(new Restaurant("test","hoho","hehe", "beste"),venn, timeButton.getText().toString());
        db.addBestilling(bestilling);
        Log.d("Legg inn: ", "legger til bestillinger");
        Toast.makeText(getBaseContext(),"Bestilling lagt til", Toast.LENGTH_SHORT).show();
    }

    public void deleteinDB(View v) {
        Long restaurantid = (Long.parseLong("1"));
        db.deleteBestilling(restaurantid);
    }

    private void friendsButtonOnclick() {
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsAlertDialog();
            }
        });
    }

    private void setSpinner() {
        List<Restaurant> restaurants;
        restaurants = db.findAllRestauranter();
        String[] items = new String[restaurants.size()+1];
        items[0] = "Legg til restaurant";
        for(int i=1 ; i <= restaurants.size();i++){
            items[i] = restaurants.get(i-1).getNavn();
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);

        restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void friendsAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BestillBord.this);
        alertDialog.setTitle("Velg venner");
        List<Venn> venner;
        venner = db.findAllVenner();
        String[] items = new String[venner.size()];
        boolean[] checkedItems = new boolean[venner.size()];
        for(int i=0 ; i< venner.size();i++){
            items[i] =venner.get(i).getNavn();
            checkedItems[i] = false;
        }
        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                 addinDB(venner.get(which));
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        return makeTimeString(hour, minute);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = makeTimeString(hour, minute);
                timeButton.setText(time);
            }
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
    }

    private String makeTimeString(int hour, int minute) {
        String strHour = Integer.toString(hour);
        String strMinute = Integer.toString(minute);
        if (hour < 10){
            strHour = "0" + strHour;
        }
        if (minute < 10){
            strMinute = "0" + strMinute;
        }
        return strHour + ":" + strMinute;
    }

    public void openTimePicker(View view) {
        timePickerDialog.show();
    }

    public void openFriendCheckList(View view) {
    }
}
