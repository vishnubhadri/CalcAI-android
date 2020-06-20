package com.vishnu.calc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class Main extends AppCompatActivity {
    Button manipulate, formula, Ok;
    EditText inputs;
    EditText ed[];
    AI ai = new AI();
    static String dbformula = "";
    String s = "", datas = "";
    String[] arraydatas;
    LinearLayout ll;
    final Context context = this;
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll = (LinearLayout) findViewById(R.id.linearlayout1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        manipulate = (Button) findViewById(R.id.manipulate);
        formula = (Button) findViewById(R.id.formula);
        inputs = (EditText) findViewById(R.id.input);
        formula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), DatabaseListView.class));
                dialogfun();
            }
        });

        manipulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = inputs.getText().toString();
                s = s.replace("pi", "π");
                s = s.replace("Pi", "π");
                s = s.replace("PI", "π");
                s = s.replace("sqrt", "√");
                datas = "";

                if (!s.isEmpty()) {
                    for (int i = 0; i < s.length(); i++) {
                        if ((Character.isLetter(s.charAt(i)))) {
                            if (!(datas.contains(s.charAt(i) + ""))) {
                                datas = datas + s.charAt(i);
                            }
                        }
                    }
                    try {
                        arraydatas = new String[(datas.length() / 2) + 1];
                        for (int i = 0; i < (datas.length() / 2) + 1; i++) {
                            if (!(";".equals(datas.charAt(i) + ""))) {
                                arraydatas[i] = datas.charAt(i) + "";
                            }
                        }
                        createEditTexts(arraydatas);
                    } catch (StringIndexOutOfBoundsException e) {
                        showResult(inputs.getText().toString());
                    }
                }
            }
        });
    }

    void showResult(String arg1) {

        ai.parseString(arg1);
        TextView output = new TextView(this);
        Space s = new Space(this);
        s.setMinimumHeight(2);

        ll.addView(output);
        try {
            output.setText(ai.result());
        } catch (Exception e) {
            output.setText(e.toString());
        }
        ll.addView(s);
    }

    void createEditTexts(final String[] arg1/*Input labels*/) {

        ed = new EditText[arg1.length];
        for (int i = 0; i < arg1.length; i++) {
            ed[i] = new EditText(this);
            switch (arg1[i]) {
                case "π": {
                    ed[i].setId(i);
                    ed[i].setText("3.14159265359");
                    ed[i].setEnabled(false);
                    ed[i].setVisibility(View.VISIBLE);
                    ll.addView(ed[i]);
                    break;
                }
                case "E": {
                    ed[i].setId(i);
                    ed[i].setText("2.71828182845904523536028747135266249775724709369995");
                    ed[i].setEnabled(false);
                    ed[i].setVisibility(View.VISIBLE);
                    ll.addView(ed[i]);
                    break;
                }
                case "K": {
                    ed[i].setId(i);
                    ed[i].setText("2.6854520010");
                    ed[i].setEnabled(false);
                    ed[i].setVisibility(View.VISIBLE);
                    ll.addView(ed[i]);
                    break;
                }
                case "A": {
                    ed[i].setId(i);
                    ed[i].setText("1.2824271291");
                    ed[i].setEnabled(false);
                    ed[i].setVisibility(View.VISIBLE);
                    ll.addView(ed[i]);
                    break;
                }
                default: {
                    ed[i].setHint("Enter the data for " + arg1[i]);
                    ed[i].setId(i);
                    ed[i].setMinLines(1);
                    ll.addView(ed[i]);
                }

            }
        }
        Ok = new Button(this);
        Ok.setText("Get Result");
        Ok.setGravity(View.FOCUS_RIGHT);
        ll.addView(Ok);

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = s;
                System.out.println("First =" + s);
                System.out.println("ed.len=" + ed.length);
                for (int i = 0; i < ed.length; i++) {
                    System.out.println("Values for " + i + " is " + ed[i].getText().toString());
                    temp = temp.replace(arg1[i], ed[i].getText().toString());
                }
                System.out.println(temp);
                showResult(temp);
            }
        });
    }

    int count = 0;
    String[] querys = new String[10];
    View alertLayout;
    LayoutInflater inflater;
    ListView listView;
    TextView textView;
    String data[] = new String[2];
    int profcount = 0;

    void dialogfun() {
        inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.activity_database_list_view, null);
        listView = (ListView) alertLayout.findViewById(R.id.listView);
        textView = (TextView) alertLayout.findViewById(R.id.errorrep);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Formulas");
        alert.setView(alertLayout);
        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        querys[count] = "SELECT tbl_name FROM sqlite_master;";
        setDatas(querys[count]);
        final AlertDialog dialog = alert.create();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String product = ((TextView) view).getText().toString();
                count++;
                if (count <= 1) {
                    querys[count] = "select * from `" + product + "`";
                } else {
                    querys[count] = querys[count - 1].replace("*", "RHS") + " where `LHS` LIKE '" + product.replace("-", "_") + "'";
                }
                if (countWords(querys[count], "where") >= 2) {
                    querys[count] = querys[count].substring(querys[count].lastIndexOf("LIKE") + 6, querys[count].length() - 1);
                    System.out.println("Setting datas" + querys[count]);
                    inputs.setText(querys[count]);
                    dialog.dismiss();
                } else {
                    setDatas(querys[count]);
                }

            }

        });
        dialog.show();
        count = 0;

    }

    void setDatas(String query) {
        databaseAccess.open();
        List<String> quotes = databaseAccess.getQuotes(query);
        databaseAccess.close();
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quotes);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            textView = (TextView) findViewById(R.id.errorrep);
            textView.setVisibility(View.VISIBLE);
            textView.setText(e.toString());
        }
    }

    int countWords(String Sen, String word) {
        String a[] = Sen.split("\\ ");
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(word)) {
                count++;
            }
        }
        return count;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.formula:
                addFormula();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addFormula() {

        final Dialog addFormula = new Dialog(this);

        addFormula.setContentView(R.layout.formulainsertlayout);
        addFormula.setTitle("Enter The Formula");
        final Button insert = (Button) addFormula.findViewById(R.id.Insert);
        final Button cancel = (Button) addFormula.findViewById(R.id.Cancel);
        final EditText LHS = (EditText) addFormula.findViewById(R.id.LHS);
        final EditText RHS = (EditText) addFormula.findViewById(R.id.RHS);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(LHS.getText().toString().isEmpty() || RHS.getText().toString().isEmpty())) {
                    try {
                        databaseAccess.addFormula(LHS.getText().toString(), RHS.getText().toString());
                        Toast.makeText(getApplicationContext(), "Formula Inserted Successfully", Toast.LENGTH_LONG).show();
                    } catch (NullPointerException e) {
                        databaseAccess.open();
                        databaseAccess.addFormula(LHS.getText().toString(), RHS.getText().toString());
                    }
                    Toast.makeText(getApplicationContext(), "Formula Inserted Successfully", Toast.LENGTH_LONG).show();
                    addFormula.dismiss();
                } else if (LHS.getText().toString().isEmpty() && RHS.getText().toString().isEmpty()) {
                    LHS.setHint("Enter the formula here");
                    RHS.setHint("Enter the formula here");
                } else if (RHS.getText().toString().isEmpty()) {
                    RHS.setHint("Enter the formula here");
                } else {
                    LHS.setHint("Enter the formula here");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFormula.dismiss();
            }
        });
        addFormula.show();

    }
}