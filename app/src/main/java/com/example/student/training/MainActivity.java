package com.example.student.training;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    MyAdapter adapter;
    MyHandler dataHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listview);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent it = new Intent(MainActivity.this, DetailActivity.class);
            it.putExtra("link",dataHandler.newsItems.get(i).link);
            startActivity(it);
        }
    });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_reload:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String str_url = "view-source:https://www.ettoday.net/news/focus/%E7%94%9F%E6%B4%BB/%E6%B0%A3%E8%B1%A1/";
                        URL url = null;
                        try {
                            url = new URL(str_url);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
                            InputStream inputStream = conn.getInputStream();
                            InputStreamReader isr = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String str;

                            while ((str = br.readLine()) != null)
                            {
                                sb.append(str);
                            }
                            String str1 = sb.toString();

                            dataHandler = new MyHandler();
                            SAXParserFactory spf = SAXParserFactory.newInstance();      //本三行為固定寫法
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            xr.setContentHandler(dataHandler);
                            xr.parse(new InputSource(new StringReader(str1)));

                            br.close();
                            isr.close();
                            inputStream.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run(){
                                    adapter = new MyAdapter(MainActivity.this,
                                            dataHandler.newsItems);
                                    lv.setAdapter(adapter);
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
