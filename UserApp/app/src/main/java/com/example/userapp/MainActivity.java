package com.example.userapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    //defining views
    EditText editTextIdUser, editTextName, editTextPhone, editTextCountry, editTextAge;
    ListView listView;
    Button buttonAddUpdate;


    //we will use this list to display hero in listview
    List<User> userList;

    //as the same button is used for create and update
    //we need to track whether it is an update or create operation
    //for this we have this boolean
    boolean isUpdating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextIdUser = (EditText) findViewById(R.id.editTextIdUser);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);
        editTextAge = (EditText) findViewById(R.id.editTextAge);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);

        listView = (ListView) findViewById(R.id.listViewUsers);

        userList = new ArrayList<>();


        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if it is updating
                if (isUpdating) {
                    //calling the method update hero
                    //method is commented becuase it is not yet created
                    updateUser();
                } else {
                    //if it is not updating
                    //that means it is creating
                    //so calling the method create hero
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();


        //validating the inputs
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Please enter real name");
            editTextPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(country)) {
            editTextCountry.setError("Please enter name");
            editTextCountry.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {
            editTextAge.setError("Please enter real name");
            editTextAge.requestFocus();
            return;
        }

        //if validation passes

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("country", country);
        params.put("age", age);


        //Calling the create hero API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readUsers() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USERS, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshUserList(JSONArray users) throws JSONException {
        //clearing previous heroes
        userList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < users.length(); i++) {
            //getting each hero object
            JSONObject obj = users.getJSONObject(i);

            //adding the hero to the list
            userList.add(new User(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getInt("phone"),
                    obj.getString("country"),
                    obj.getInt("age")
            ));
        }

        //creating the adapter and setting it to the listview
        UserAdapter adapter = new UserAdapter(userList);
        listView.setAdapter(adapter);
    }

    private void updateUser() {
        String id = editTextIdUser.getText().toString();
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Please enter real name");
            editTextPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(country)) {
            editTextCountry.setError("Please enter name");
            editTextCountry.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {
            editTextAge.setError("Please enter real name");
            editTextAge.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("country", String.valueOf(country));
        params.put("age", age);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_USER, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Add");

        editTextName.setText("");
        editTextPhone.setText("");
        editTextCountry.setText("");
        editTextAge.setText("");

        isUpdating = false;
    }

    private void deleteUser(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_USER + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    class UserAdapter extends ArrayAdapter<User> {

        //our hero list
        List<User> user_List;


        //constructor to get the list
        public UserAdapter(List<User> heroList) {
            super(MainActivity.this, R.layout.layout_user_list, userList);
            this.user_List = userList;
        }


        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

            //getting the textview for displaying name
            //TextView textViewName = listViewItem.findViewById(R.id.textViewIdUser);
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);
            TextView textViewPhone = listViewItem.findViewById(R.id.textViewPhone);
            TextView textViewCountry = listViewItem.findViewById(R.id.textViewCountry);
            TextView textViewAge = listViewItem.findViewById(R.id.textViewAge);


            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final User tmp_user = user_List.get(position);

            textViewName.setText(tmp_user.getName());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //so when it is updating we will
                    //make the isUpdating as true
                    isUpdating = true;

                    //we will set the selected hero to the UI elements
                    editTextIdUser.setText(String.valueOf(tmp_user.getId()));
                    editTextName.setText(tmp_user.getName());
                    editTextPhone.setText(tmp_user.getPhone());
                    editTextCountry.setText(tmp_user.getCountry());
                    editTextAge.setText(tmp_user.getAge());
                    //we will also make the button text to Update
                    buttonAddUpdate.setText("Update");
                }
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Delete " + tmp_user.getName())
                            .setMessage("Seguro que desea eliminar?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the hero
                                    //method is commented because it is not yet created
                                    deleteUser(tmp_user.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}

