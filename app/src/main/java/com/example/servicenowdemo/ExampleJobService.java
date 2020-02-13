package com.example.servicenowdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Base64;
import android.util.Log;

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

import static com.example.servicenowdemo.MainActivity.EXTRA_SYSID;

public class ExampleJobService extends JobService {

    private static final String TAG = "ExampleJobService";
    private RequestQueue mQueue;
    private boolean jobCancelled = false;
    //swdwd
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {

        mQueue = Volley.newRequestQueue(this);
        final String x = params.getExtras().getString("value");
        final String sys_id = params.getExtras().getString("sys_id");
        Log.d(TAG, sys_id);
        Log.d(TAG, x);
        new Thread(new Runnable() {
            @Override
            public void run() {

                final JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("u_body", x);


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


                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}