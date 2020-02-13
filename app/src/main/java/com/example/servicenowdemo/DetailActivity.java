package com.example.servicenowdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.servicenowdemo.MainActivity.EXTRA_CREATOR;
import static com.example.servicenowdemo.MainActivity.EXTRA_LIKES;
import static com.example.servicenowdemo.MainActivity.EXTRA_SYSID;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RequestQueue mQueue;
    public String sys_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Button ApproverParser = findViewById(R.id.Approve);
        final Button RejectParse = findViewById(R.id.Reject);


        mQueue = Volley.newRequestQueue(this);

        ApproverParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Approved = "Approved";
                Toast.makeText(getApplicationContext(),"Approved",Toast.LENGTH_SHORT).show();
                jsonParse(Approved,sys_id);

                RejectParse.setEnabled(false);
            }
        });

        RejectParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Rejected = "Rejected";
                Toast.makeText(getApplicationContext(),"Rejected",Toast.LENGTH_SHORT).show();
                jsonParse(Rejected,sys_id);
                ApproverParser.setEnabled(false);
            }
        });

        Intent intent = getIntent();

        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);
         sys_id = intent.getStringExtra(EXTRA_SYSID);


        TextView textViewCreator = findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_view_like_detail);

        textViewCreator.setText(creatorName);
        textViewLikes.setText("Likes: " + likeCount);
    }


    //from

    private void jsonParse(String value,String sys_id) {



        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("value",value);
        bundle.putString("sys_id",sys_id);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(bundle)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }

    }



    //to


   /* private void jsonParse(String value) {

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("u_body", value);


        } catch (JSONException e) {
            // handle exception
        }

        String id = "58140";

        //String url = "https://dev"+id+".service-now.com/api/now/table/u_api_demo?sysparm_limit=3";
       // Toast.makeText(getApplicationContext(),sys_id,Toast.LENGTH_SHORT).show();

        String url = "https://dev"+id+".service-now.com/api/now/table/u_api_demo/"+sys_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public byte[] getBody() {

                try {
                    Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers =  new HashMap<String, String>();
                String enteredusername = "admin";
                String enteredpassword ="Belikebro@123";
                String credentials = enteredusername + ":" + enteredpassword;
                String encoded = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", encoded);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };


        mQueue.add(request);


        //Intent detailIntent = new Intent(this, MainActivity.class);
        //startActivity(detailIntent);

    } */

}

