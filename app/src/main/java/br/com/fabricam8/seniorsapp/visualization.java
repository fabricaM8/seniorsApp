package br.com.fabricam8.seniorsapp;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class visualization extends ActionBarActivity {


    String[] medicamentos       = {"Medicamento1","Medicamento2","Medicamento3","Medicamento4","Medicamento5","Medicamento6",
            "Medicamento7","Medicamento8","Medicamento9","Medicamento10","Medicamento11","Medicamento12","Medicamento13"};
    String[] ativadades = {"Correr","Nadar", "Caminhar"};

    String[] todos       = {"Lorax","doril","Correr","Caminhar"};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

     }
    public void runList1(View v)
    {
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todos);
        listView.setAdapter(array);

    }
    public void runList2(View v)
    {
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,medicamentos);
        listView.setAdapter(array);
    }
    public void runList3(View v)
    {

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> array2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ativadades);
        listView.setAdapter(array2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
