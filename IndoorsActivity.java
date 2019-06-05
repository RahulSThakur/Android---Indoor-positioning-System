package com.example.dell.map5;

/**
 * Created by Dell on 08-04-2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Text;
/*
class used to find the indoor location of the user
This class include WIFI as the primary source to detect the presence of the user inside the building
It collects the values of 3 diffent WIFI access points and compare and tells the X and Y axis of the user according to those WIFI points

*/
public class IndoorsActivity extends AppCompatActivity
{

    // GLOBAL VARIABLE DECLARATION
    private WifiManager wifiManager;
    private WifiScanReceiver wifiScan;
    private String wifis[];
    //RSSID
    private String P1_RSSI[];
    private String P2_RSSI[];
    private String P3_RSSI[];
    private String CP_RSSI[];
    //BSSID
    private String P1_BSSID[];
    //Display
    private ListView lv;
    //To Test
    TextView displayIt;
    //Buttons
    private Button Point1;
    private Button Point2;
    private Button Point3;
    private Button DISPLAY;

    private Button Btn_home;
    //Toggle!
    private Boolean Start_Loop = Boolean.FALSE;

    public static int x;
    public static int y;


    //-------------INITIALIZE METHODS---------------------------------------------------------------

    // INITIALIZE INDOORS
    private void initializeIndoors()
    {
        /* variables and access will be given
        at the beginning of the program for Indoors part
         */

        //Initialize Wifi Manager
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //Initialize List View
        lv = (ListView)findViewById(R.id.ShownListView);
        //Instance of the class can be created and initialised.
        wifiScan = new WifiScanReceiver();

        //Initialize test displayText
        displayIt = (TextView)findViewById(R.id.shownText);

        // WiFi CHECK & ENABLE
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
        {
            // Checking the Wi-Fi state and turning the Wi-Fi on if it is disabled.
            wifiManager.setWifiEnabled(true);
        }

        //START SCAN
        wifiManager.startScan();

        //------------COLLECT DATA------------------------------------------------------------------

        //Initialize buttons
        Point1 = (Button) findViewById(R.id.P1);
        Point2 = (Button) findViewById(R.id.P2);
        Point3 = (Button) findViewById(R.id.P3);
        DISPLAY = (Button) findViewById(R.id.DISPLAY);
        //JUMP = (Button) findViewById(R.id.JumpShown);
        Btn_home = (Button) findViewById(R.id.button_home);
    }

    // ERROR MESSAGE
    private void showNotEnoughData()
    {
        Toast.makeText(this, "Not enough data, please redefine reference point",
                Toast.LENGTH_LONG).show();
    }


    // STORE P1
    private void storeP1()
    {
        //-->WiFi SCAN<--
        // WifiScanReceiver Definition

        //Get the list
        List<ScanResult> wifiScanList = wifiManager.getScanResults();
        /*
        //-->WiFi SORT<--
        //Here your list is sorted by level from strong to weak
        Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
            }
        };
        //Sort the received list in the comparator way
        Collections.sort(wifiScanList, comparator);
        */

        //-->WiFi STORAGE<--
        //P1 STORAGE (Reference button)
        P1_BSSID = new String[3];
        //Store it inside a vector
        for (int i = 0; i < 3; i++) {
            P1_BSSID[i] = wifiScanList.get(i).BSSID;
        }

        P1_RSSI = new String[3];
        //Store it inside a vector
        for (int i = 0; i < 3; i++)
        {
            P1_RSSI[i] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
        }
    }

    private void elseMethod()
    {
        Toast.makeText(this, "Data is saved",
                Toast.LENGTH_LONG).show();
    }

    // STORE P2
    private void storeP2()
    {
        //-->WiFi SCAN<--
        List<ScanResult> wifiScanList = wifiManager.getScanResults();

        //displayIt.setText(wifiScanList.get(0).BSSID);

        P2_RSSI = new String[3];

        //-->WiFi SEARCH & STORE<--
        for(int i=0; i<wifiScanList.size(); i++)
        {
            if(wifiScanList.get(i).BSSID.equals((P1_BSSID[0])))
            {
                P2_RSSI[0] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            }
            else if(wifiScanList.get(i).BSSID.equals(P1_BSSID[1]))
            {
                P2_RSSI[1] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            }
            else if(wifiScanList.get(i).BSSID.equals(P1_BSSID[2]))
            {
                P2_RSSI[2] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            }
        }

        if((P2_RSSI[0].equals(null))||(P2_RSSI[1].equals(null))||(P2_RSSI[2].equals(null)))
        {
            showNotEnoughData();
        }
        else{
            elseMethod();
        }
    }

    // STORE P3
    private void storeP3()
    {
       //-->WiFi SCAN<--
        List<ScanResult> wifiScanList = wifiManager.getScanResults();

        P3_RSSI = new String[3];


        //-->WiFi SEARCH & STORE<--
        for(int i=0; i<wifiScanList.size(); i++){
            if(wifiScanList.get(i).BSSID.equals(P1_BSSID[0]))
            {
                P3_RSSI[0] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 25));
            }
            else if(wifiScanList.get(i).BSSID.equals(P1_BSSID[1]))
            {
                P3_RSSI[1] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 25));
            }
            else if(wifiScanList.get(i).BSSID.equals(P1_BSSID[2]))
            {
                P3_RSSI[2] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 25));
            }
        }

        if((P3_RSSI[0].equals(null))||(P3_RSSI[1].equals(null))||(P3_RSSI[2].equals(null)))
        {
            showNotEnoughData();
        }
        else{
            elseMethod();
        }
    }

    private void displayInformation()
    {

        wifis = new String[3];

        String X_R1 = String.valueOf(Position_R1[0]);
        String Y_R1 = String.valueOf(Position_R1[1]);
        String X_R2 = String.valueOf(Position_R2[0]);
        String Y_R2 = String.valueOf(Position_R2[1]);
        String X_R3 = String.valueOf(Position_R3[0]);
        String Y_R3 = String.valueOf(Position_R3[1]);

        // Store it inside a List of ScanResult
        String P1_Rout1 = String.valueOf(P1_RSSI[0]);
        String P2_Rout1 = String.valueOf(P2_RSSI[0]);
        String P3_Rout1 = String.valueOf(P3_RSSI[0]);

        String P1_Rout2 = String.valueOf(P1_RSSI[1]);
        String P2_Rout2 = String.valueOf(P2_RSSI[1]);
        String P3_Rout2 = String.valueOf(P3_RSSI[1]);

        String P1_Rout3 = String.valueOf(P1_RSSI[2]);
        String P2_Rout3 = String.valueOf(P2_RSSI[2]);
        String P3_Rout3 = String.valueOf(P3_RSSI[2]);


        wifis[0] = X_R1 + " , " + Y_R1;
        wifis[1] = X_R2 + " , " + Y_R2;
        wifis[2] = X_R3 + " , " + Y_R3;

        // Display List
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,wifis));

    }


    //-------------INDOORS IMPLEMENTED METHODS------------------------------------------------------
    public class WifiScanReceiver extends BroadcastReceiver
    {

        // Class receives the results from the Wi-Fi scan.
        public void onReceive(Context c, Intent intent)
        {

            Point1.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view)
                {

                    // START SCAN
                    wifiManager.startScan();
                    storeP1();
                    //unregisterReceiver(wifiScanReceiver);
                }
            });

            Point2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    // START SCAN
                    wifiManager.startScan();
                    storeP2();
                    //unregisterReceiver(wifiScanReceiver);
                }
            });

            Point3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    // START SCAN
                    wifiManager.startScan();
                    storeP3();
                    processData();
                    displayInformation();
                    //unregisterReceiver(wifiScanReceiver);
                }
            });

            DISPLAY.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    // START SCAN
                    wifiManager.startScan();
                    UpdateCurrentPosition();
                    //unregisterReceiver(wifiScanReceiver);
                }
            });

        }

    }

    //--------------PROCESS DATA--------------------------------------------------------------------

    // GET DISTANCE (Training & Position Phase)
    //return the disance between two points base on their coordinates
    public float getDistance(float X_1, float Y_1, float X_2, float Y_2)
    {
        //define the output distance
        float Distance;
        Distance = (float) Math.sqrt(Math.pow((X_1 - X_2),2) + Math.pow((Y_1 - Y_2),2));

        return Distance;
    }

    // GET ROUTER POSITION (Training Phase)
    //return the location of one router base on RSSI received at three points
    public float[] getRouterPosition(int P1, int P2, int P3, int Width, int Length)
    {
        //define the coordinates of the router position
        float YR_pos, YR_neg, XR, YR;
        XR = (float) ((Math.pow(P1,2) - Math.pow(P2,2) + Math.pow(Width,2))/(2*Width));
        YR_pos = (float) Math.sqrt(Math.abs(Math.pow(P1,2)  - Math.pow(XR,2)));
        YR_neg = -YR_pos;

        //define  distance
        float D_0, D_1;
        D_0 = getDistance(XR, YR_pos, Width, Length);
        D_1 = getDistance(XR, YR_neg, Width, Length);
        if((D_0 - P3) < (D_1 - P3))
        {
            YR = YR_pos;
        }
        else
        {
            YR = YR_neg;
        }

        return new float[] {XR, YR};
    }

    // GET USER POSITION (Positioning Phase)
    //return the location of the user base on the location of three routers and the signal the user received
    public int[] getuserPosition(float XR_1, float YR_1, float XR_2, float YR_2,
                                 float XR_3, float YR_3, int R1, int R2, int R3)
    {
        //define final outputs
        int XU_1 = 0, YU_1 = 0, XU_2 = 0, YU_2 = 0, XU, YU;
        //define arrays that needed
        float D0[][] = new float[17][50];
        float D1[][] = new float[17][50];
        float f[][] = new float[17][50];
        //define minimun standard deviation
        float fmin_1 = 0;
        float fmin_2 = 0;
        //define columns and rows
        int i, j;
        for(j=0; j<=49; j++){
            for(i=0; i<=16; i++){
                D0[i][j] = (float) (Math.pow((i-XR_1),2) + Math.pow((j-YR_1),2) - Math.pow(R1,2));
                D1[i][j] = (float) (Math.pow((i-XR_2),2) + Math.pow((j-YR_2),2) - Math.pow(R2,2));
                f[i][j] = (float) Math.sqrt(Math.pow(D0[i][j], 2) + Math.pow(D1[i][j], 2));
                //get two user position where f[i][j] are the minimum
                //initialise the minimum two positions
                if(i==0 & j==0)
                {
                    fmin_1 = f[i][j];
                    XU_1 = i;
                    YU_1 = j;
                }
                else if(j==0 & i==1)
                {
                    if(f[i][j] < fmin_1)
                    {
                        fmin_2 = fmin_1;
                        fmin_1 = f[i][j];
                        XU_2 = XU_1;
                        XU_1 = i;
                        YU_2 = YU_1;
                        YU_1 = j;
                    }
                    else
                    {
                        fmin_2 = f[i][j];
                        XU_2 = i;
                        YU_2 = j;
                    }
                }
                else
                    {
                    if(f[i][j] < fmin_1)
                    {
                        fmin_2 = fmin_1;
                        fmin_1 = f[i][j];
                        XU_2 = XU_1;
                        XU_1 = i;
                        YU_2 = YU_1;
                        YU_1 = j;
                    }
                    else if(f[i][j] < fmin_2)
                    {
                        fmin_2 = f[i][j];
                        XU_2 = i;
                        YU_2 = j;
                    }
                }

            }
        }

        //define distance from two possible location to the third router location
        float PossibleD_0, PossibleD_1;
        //get the possible distances
        PossibleD_0 = getDistance(XU_1, YU_1, XR_3, YR_3);
        PossibleD_1 = getDistance(XU_2, YU_2, XR_3, YR_3);
        //compare the distance with the wifi signal strength
        if((PossibleD_0 - R3) < (PossibleD_1 - R3))
        {
            XU = XU_1;
            YU = YU_1;
        }
        else{
            XU = XU_2;
            YU = YU_2;
        }
        return new int[] {XU, YU};
    }

    //--->TRAINING PHASE<----
    //DEFINE VARIABLES
    float[] Position_R1;
    float[] Position_R2;
    float[] Position_R3;

    // GET ROUTERS POSITIONS
    private void processData()
    {

        //ROUTER 1
        // DEFINE POSITION OF ROUTER 1
        int R1_P1 = Integer.parseInt(P1_RSSI[0]);
        int R1_P2 = Integer.parseInt(P2_RSSI[0]);
        int R1_P3 = Integer.parseInt(P3_RSSI[0]);

        // GET X and Y OF ROUTER 1
        Position_R1 = getRouterPosition(R1_P1, R1_P2, R1_P3, 16, 49);

        //ROUTER 2
        // DEFINE POSITION OF ROUTER 2
        int R2_P1 = Integer.parseInt(P1_RSSI[1]);
        int R2_P2 = Integer.parseInt(P2_RSSI[1]);
        int R2_P3 = Integer.parseInt(P3_RSSI[1]);

        // GET X and Y OF ROUTER 2
        Position_R2 = getRouterPosition(R2_P1, R2_P2, R2_P3, 16, 49);

        //ROUTER 3
        // DEFINE POSITION OF ROUTER 1
        int R3_P1 = Integer.parseInt(P1_RSSI[2]);
        int R3_P2 = Integer.parseInt(P2_RSSI[2]);
        int R3_P3 = Integer.parseInt(P3_RSSI[2]);

        // GET X and Y OF ROUTER 1
        Position_R3 = getRouterPosition(R3_P1, R3_P2, R3_P3, 16, 49);
    }


    public void UpdateCurrentPosition()
    {
        // DEFINE VARIABLES
        int[] Your_Current_Position;

        // GET USER POSITION
        //-->WiFi SCAN<--
        List<ScanResult> wifiScanList = wifiManager.getScanResults();

        CP_RSSI = new String[3];
        //-->WiFi SEARCH & STORE<--
        for (int i = 0; i < wifiScanList.size(); i++)
        {
            if (wifiScanList.get(i).BSSID.equals((P1_BSSID[0])))
            {
                CP_RSSI[0] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            } else if (wifiScanList.get(i).BSSID.equals(P1_BSSID[1]))
            {
                CP_RSSI[1] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            } else if (wifiScanList.get(i).BSSID.equals(P1_BSSID[2]))
            {
                CP_RSSI[2] = String.valueOf(wifiManager.calculateSignalLevel(wifiScanList.get(i).level, 64));
            }
        }


        if ((CP_RSSI[0].equals(null)) || (CP_RSSI[1].equals(null)) || (CP_RSSI[2].equals(null)))
        {
            showNotEnoughData();
        } else
        {
            elseMethod();
        }

        int R1_CP = Integer.parseInt(CP_RSSI[0]);
        int R2_CP = Integer.parseInt(CP_RSSI[1]);
        int R3_CP = Integer.parseInt(CP_RSSI[2]);

        // GET YOUR X and Y
        Your_Current_Position = getuserPosition(Position_R1[0], Position_R1[1], Position_R2[0], Position_R2[1], Position_R3[0], Position_R3[1], R1_CP, R2_CP, R3_CP);
        x = Your_Current_Position[0];
        y = Your_Current_Position[1];

        // Display X and Y
        displayIt.setText("x: " + String.valueOf(Your_Current_Position[0]) + " , y: " + String.valueOf(Your_Current_Position[1]));
    }


    private void launchMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Home button function so that it can go back to the home screen
    private void goHome()
    {
        Btn_home = (Button) findViewById(R.id.button_home);

        Btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchMain();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoors);

        registerReceiver(wifiScan, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        initializeIndoors();
        //Home button function so that it can go back to the home screen
        goHome();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Home button function so that it can go back to the home screen
        goHome();
        //WifiScanReceiver need to be registered to start receiving the scan results.
        registerReceiver(wifiScan,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //Disable WifiScanReceiver to save battery life
        unregisterReceiver(wifiScan);
    }
}
