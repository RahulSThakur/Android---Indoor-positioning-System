package com.example.dell.map5;

import android.app.Activity;

//This code is the basic module of a location application
//it shows the position of the user and the user can search other location
//it even tells the details about the location of the places to the user in the bar
//zoom feature is also there in the app




/**
 * Created by Dell on 06-04-2018.
 */
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK())
        {
            init();
        }
    }


    //Initializing the button
    private void init()
    {
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Button indoor = (Button) findViewById(R.id.btnIn);
        indoor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vieww)
            {
               // Intent intent = new Intent(MainActivity.this, IndoorsActivity.class);
               // startActivity(intent);
                launchActivity(IndoorsActivity.class);
            }
        });


    }

        private void launchActivity(Class<?> cls)
        {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        }

    //checking the google service version
    public boolean isServicesOK()
    {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS)
        {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
            {
            Toast.makeText(this, "You can't make map requests", LENGTH_SHORT).show();
            }
        return false;
    }

}