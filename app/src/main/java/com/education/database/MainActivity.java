package com.education.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.education.database.adapter.UserAdapter;
import com.education.database.room.UserDao;
import com.education.database.room.UserDataBase;
import com.education.database.room.Users;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.UserItemClickListener {

    private TextView editTextName;
    private Button addButton;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Users selectedUser;

    List<Users> usersListAlreadyavaliable;
    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intializeDataBase();
        prepareData();
        intializeViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intializeRecycleView();
                setOnclicklistner();
            }
        },500);


    }

    private void prepareData() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                usersListAlreadyavaliable=userDao.getAllNotes();
            }
        });
    }


    private void intializeViews(){
        editTextName = findViewById(R.id.editTextName);
        addButton = findViewById(R.id.button_Submitt);
        recyclerView = findViewById(R.id.recyclerView);
    }
    private void setOnclicklistner() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addButton.getText().toString().equalsIgnoreCase("Update Record")){
                        updateRecord(selectedUser);
                        selectedUser=null;
                        editTextName.setText("");
                        addButton.setText("Submit");
                }else {
                    insertRecordToDB();
                }

            }
        });
    }


    private void intializeDataBase(){
        UserDataBase userDatabase = UserDataBase.getInstance(this);
        userDao = userDatabase.userDao();
    }

    private void insertRecordToDB(){
        String username=  editTextName.getText().toString();
        Users users=new Users(username);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(users);
                List<Users> userList = userDao.getAllNotes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userAdapter.updateData(userList);
                        userAdapter.notifyDataSetChanged();
                        hideSoftKeyboard();
                    }
                });
            }
        });
        editTextName.setText("");
        Toast.makeText(this,"Record Inserted",Toast.LENGTH_SHORT).show();

    }
    private void updateRecord(Users users) {
        String newUsername = editTextName.getText().toString();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Users existingUser = userDao.getUserById(users.getId());
                if (existingUser != null) {
                    existingUser.setName(newUsername);
                    userDao.update(existingUser);
                    List<Users> userList = userDao.getAllNotes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the RecyclerView with the updated data
                            userAdapter.updateData(userList);
                            hideSoftKeyboard();
                        }
                    });
                }
            }
        });

        editTextName.setText("");
        Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
    }


    private void intializeRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userAdapter=new UserAdapter(usersListAlreadyavaliable,MainActivity.this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,LinearLayoutManager.VERTICAL));
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onEditClick(Users users) {
        selectedUser=users;
        editTextName.setText(users.getName());
        addButton.setText("Update Record");
    }

    @Override
    public void onDeleteClick(Users users) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                userDao.delete(users);
                List<Users> userList = userDao.getAllNotes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userAdapter.updateData(userList);
                        userAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        Toast.makeText(this, "Delete Clicked for user: " + users.getName(), Toast.LENGTH_SHORT).show();

    }
}