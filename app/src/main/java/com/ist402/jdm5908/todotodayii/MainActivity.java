package com.ist402.jdm5908.todotodayii;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Additional generic cop compliment, Brian...

public class MainActivity extends AppCompatActivity {

    protected DBHelper mDBHelper;
    private List<ToDo_Item> list;
    private MyAdapter adapt;
    private EditText myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTask = (EditText) findViewById(R.id.editText1);
        mDBHelper = new DBHelper(this);
    }

    @Override
    protected void onResume(){

        super.onResume();
        list = mDBHelper.getAllTasks();
        adapt = new MyAdapter(this, R.layout.todo_item, list);
        ListView listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
    }

    // Button click listener for adding a task
    public void addTaskNow(View view) {

        String s = myTask.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(getApplicationContext(), "A TODO task must be entered.", Toast.LENGTH_SHORT).show();
        } else {
            // Build a new task item and add it to the database
            ToDo_Item task = new ToDo_Item(s, 0);
            mDBHelper.addToDoItem(task);

            // Clear the task EditText
            myTask.setText("");

            // Add the task and refresh ListView
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }
    }

    // Button click listener for deleting all tasks
    public void clearTasks(View view){

    mDBHelper.clearAll(list);
    adapt.notifyDataSetChanged();
    }

    //*************************************** ADAPTER *********************************************
    private class MyAdapter extends ArrayAdapter<ToDo_Item>{

        Context context;
        List<ToDo_Item> taskList = new ArrayList<>();

        public MyAdapter(Context c, int rId, List<ToDo_Item> objects){
            super(c, rId, objects);
            taskList = objects;
            context = c;
        }

        //******************************** TASK ITEM VIEW *****************************************
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            CheckBox isDoneChBx = null;
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.todo_item, parent, false);
                isDoneChBx = (CheckBox) convertView.findViewById(R.id.chkStatus);
                convertView.setTag(isDoneChBx);

                isDoneChBx.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        CheckBox cb = (CheckBox) view;
                        ToDo_Item changeTask = (ToDo_Item) cb.getTag();
                        changeTask.setIs_done(cb.isChecked() == true ? 1 : 0);
                        mDBHelper.updateTask(changeTask);
                    }
                });
            }
            else{
                isDoneChBx = (CheckBox) convertView.getTag();
            }
            ToDo_Item current = taskList.get(position);
            isDoneChBx.setText(current.getDescription());
            isDoneChBx.setChecked(current.getIs_done() == 1 ? true : false);
            isDoneChBx.setTag(current);
            return convertView;
        }
    }
}
