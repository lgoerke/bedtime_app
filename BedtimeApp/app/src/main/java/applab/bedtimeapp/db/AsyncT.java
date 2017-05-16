//package applab.bedtimeapp.db;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.json.JSONObject;
//
///**
// * Created by Willem on 5/16/2017.
// */
//
//class AsyncT extends AsyncTask<Void, Void, Void> {
//    @Override
//    protected Void doInBackground(Void... voids) {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("<YOUR_SERVICE_URL>");
//
//        try {
//
//            JSONObject jsonobj = new JSONObject();
//
//            jsonobj.put("name", "Aneh");
//            jsonobj.put("age", "22");
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));
//
//            Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
//
//            // Use UrlEncodedFormEntity to send in proper format which we need
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            InputStream inputStream = response.getEntity().getContent();
//            InputStreamToStringExample str = new InputStreamToStringExample();
//            responseServer = str.getStringFromInputStream(inputStream);
//            Log.e("response", "response -----" + responseServer);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//
//        txt.setText(responseServer);
//    }
//}