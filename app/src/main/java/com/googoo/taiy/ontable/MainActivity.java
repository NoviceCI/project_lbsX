package com.googoo.taiy.ontable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private List<Product> productList = new ArrayList<>();
    private Context context ;
    private static final String url = "http://qazx.servehttp.com:99/ecom/json/getproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();

        listView = (ListView) findViewById(R.id.listview);

        new getDataJson().execute();
    }


    private class listAdapter extends BaseAdapter {

        List<Product> productList;
        Context context;

        public listAdapter(List<Product> productList, Context context) {
            this.productList = productList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(this.context).inflate(R.layout.rowitem, null);

            Product product = productList.get(position);

            if (productList != null) {

                TextView id = (TextView) convertView.findViewById(R.id.proid);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

                byte[] bytes  = Base64.decode(product.getData(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                imageView.setImageBitmap(bitmap);
                id.setText(product.getId());
                name.setText(product.getName());
            }


            return convertView;
        }
    }

    private class getDataJson extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(JSONmylib.getJson(url));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            Product product;
            for (int i = 0; i < jsonArray.length(); i++) {
             try {
                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                 product = new Product();
                 product.setId(jsonObject.getString("product_id"));
                 product.setName(jsonObject.getString("product_name"));
                 product.setData(jsonObject.getString("data"));
                 productList.add(product);
             }
             catch (JSONException e)
             {
                 e.printStackTrace();
             }
            }
            listView.setAdapter(new listAdapter(productList,context));
        }
    }
}
