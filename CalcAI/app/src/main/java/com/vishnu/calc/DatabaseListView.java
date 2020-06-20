package com.vishnu.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DatabaseListView extends AppCompatActivity {
    private ListView listView;
    public TextView textView;
    DatabaseAccess databaseAccess;
    String[] querys = new String[10];
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list_view);


        this.listView = (ListView) findViewById(R.id.listView);
        databaseAccess = DatabaseAccess.getInstance(this);
        querys[count] = "SELECT tbl_name FROM sqlite_master;";
        setDatas(querys[count]);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String product = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(), product, Toast.LENGTH_LONG).show();
                count++;
                querys[count] = "select * from '" + product + "';";
                setDatas(querys[count]);
            }
        });
    }

    void setDatas(String query) {
        databaseAccess.open();
        List<String> quotes = databaseAccess.getQuotes(query);
        databaseAccess.close();
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quotes);
            this.listView.setAdapter(adapter);

        } catch (Exception e) {
            textView = (TextView) findViewById(R.id.errorrep);
            textView.setVisibility(View.VISIBLE);
            textView.setText(e.toString());
        }
    }

    @Override
    public void onBackPressed() {

        if (count > 0) {
            count--;
            setDatas(querys[count]);
        } else {
            super.onBackPressed();
        }

    }
}
