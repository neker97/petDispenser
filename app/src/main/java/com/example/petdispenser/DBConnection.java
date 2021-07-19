 package com.example.petdispenser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import androidx.annotation.Nullable;


 public class DBConnection {
    String JSON_STRING;

    public void connectUpdate(Context context, String query) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://gtvv2wtfv4.execute-api.us-east-2.amazonaws.com/production/";
        url += query;

        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ((Boolean) response.get("success")) {
                        Log.i("result", "update successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.

            }
        });


        queue.add(ExampleRequest);
    }

    public void connectPost(Context context, String query, JSONObject body) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://gtvv2wtfv4.execute-api.us-east-2.amazonaws.com/production/";
        url += query;



        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ((Boolean) response.get("success")) {
                        Log.i("result", "update successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.

            }
        });


        queue.add(ExampleRequest);
    }

    public void connectPut(Context context, String query, JSONObject body) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://gtvv2wtfv4.execute-api.us-east-2.amazonaws.com/production/";
        url += query;



        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.PUT, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ((Boolean) response.get("success")) {
                        Log.i("result", "update successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.

            }
        });


        queue.add(ExampleRequest);
    }

    public void connectDelete(Context context, String query) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://gtvv2wtfv4.execute-api.us-east-2.amazonaws.com/production/";
        url += query;



        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ((Boolean) response.get("success")) {
                        Log.i("result", "update successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.

            }
        });


        queue.add(ExampleRequest);
    }

    public JSONArray jsonArray ;
    public JSONObject jsonObjectRequest;

    public JSONObject connectSearch(Context context, String query, @Nullable RestCallback completion) throws InterruptedException, ExecutionException, JSONException {
        BackgroundTask backgroundTask = new BackgroundTask(context, completion);

        String result = backgroundTask.execute(query).get();
        jsonObjectRequest = new JSONObject(result);
        return jsonObjectRequest;
    }

    class BackgroundTask extends AsyncTask<String, Void, String>
    {
        private Context ctx;
        String json_url;
        private RestCallback completion;

        public BackgroundTask(Context context, RestCallback completion)
        {
            this.ctx = context;
            this.completion = completion;
        }
        public JSONArray getJsonArray()
        {
            return jsonArray;
        }

        @Override
        protected void onPreExecute()
        {
            json_url= "https://gtvv2wtfv4.execute-api.us-east-2.amazonaws.com/production/";
        }

        @Override
        protected String doInBackground(String... query) {
            try {
                json_url = json_url + query[0];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=  new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine()) != null)
                {

                    stringBuilder.append(JSON_STRING+"\n");
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("result",result);
            if (this.completion != null) this.completion.onComplete(result);
        }
    }

     public interface RestCallback {
         void onComplete(String result);
     }
}

