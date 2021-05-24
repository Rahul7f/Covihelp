package com.rsin.covihelp.all_Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.rsin.covihelp.FormBeen;
import com.rsin.covihelp.R;
import com.rsin.covihelp.adapters.PhotoAdpater;
import com.rtchagas.pingplacepicker.PingPlacePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FormActivity extends AppCompatActivity {

    //views//
    MaterialButton choose_location,addimg_btn,savedata_btn;
    ImageView aadhar_font_iv,aadhar_back_iv;
    TextInputLayout title_et,description_et,pincode_et,name_et,phone_et,full_address_et;
    Spinner state,city,category;
    TextView longlat;
    GridView gridLayout;
    String latitude;
    String longitude;
    public  int i;
    
   
    //views end//
    PhotoAdpater photoAdpater;
    
    ArrayList<String> statelist;
    ArrayList<Uri> imagesEncodedList;
    int PICK_IMAGE_MULTIPLE = 0;
    int REQUEST_PLACE_PICKER = 1;
    int PICK_IMAGE_1 = 2;
    int PICK_IMAGE_2 = 3;

    
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    
    Uri imageUri,aadhar_f,aadhar_b;

    //firebase stuff
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth auth;
    List<String> downloded_img_list;
    List<String> aadhar_image;
    FirebaseFirestore db;
    CollectionReference helpers_form_collection_reference;
    CollectionReference warriors_form_collection_reference;
    ProgressDialog progressDialog;

    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //hooks//
        state = findViewById(R.id.state_spinner);
        city = findViewById(R.id.city_spinner);
        category = findViewById(R.id.catagory_spinner);
        gridLayout = findViewById(R.id.gridlayout);
        addimg_btn = findViewById(R.id.add_imgbtn);
        choose_location = findViewById(R.id.picklocation_btn);
        longlat = findViewById(R.id.longlet);
        progressDialog = new ProgressDialog(FormActivity.this);
        progressDialog.setTitle("Data uploading please wait");
        progressDialog.setCancelable(false);
        path = getIntent().getStringExtra("path");
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();



        title_et = findViewById(R.id.title_et);
        description_et = findViewById(R.id.description_et);
        pincode_et = findViewById(R.id.pincode_et);
        full_address_et = findViewById(R.id.full_address_et);
        name_et = findViewById(R.id.name_et);
        phone_et = findViewById(R.id.phone_et);
        aadhar_font_iv = findViewById(R.id.aadhar_card_font);
        aadhar_back_iv = findViewById(R.id.aadahr_card_back);
        savedata_btn = findViewById(R.id.post_btn);
        // hookes end//
        
       // instance//
        statelist = new ArrayList<String>();
        imagesEncodedList = new ArrayList<Uri>();
        aadhar_image = new ArrayList<String>();
        Places.initialize(getApplicationContext(), "AIzaSyCvBiSu7l0cfHADT_y0XNPrV2APIarkvTI");
        downloded_img_list = new ArrayList<String>();
        //--end--//

        //firebase stuff
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
         storageRef = storage.getReference();
         auth = FirebaseAuth.getInstance();
         helpers_form_collection_reference = db.collection("helpers_forms");
         warriors_form_collection_reference = db.collection("warriors_form");

        // Create a reference to "mountains.jpg"
//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        //--end--//
        textWatcher(title_et);
        textWatcher(description_et);
        textWatcher(pincode_et);
        textWatcher(full_address_et);
        textWatcher(name_et);
        textWatcher(phone_et);


        addimg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });

        choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacePicker();
            }
        });
        aadhar_font_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_1);
            }
        });

        aadhar_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_2);
            }
        });

        savedata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               savedata();

            }
        });


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            Iterator<String> iter = obj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = obj.get(key);

                    statelist.add(key);

                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statelist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(arrayAdapter);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = parentView.getSelectedItem().toString();
                try {
                    JSONObject obj = new JSONObject(loadJSONFromAsset());
                    JSONArray jsonArray = obj.getJSONArray(selected);
                    ArrayList<String> citylist =citylist = new ArrayList<String>();

                    for(int i=0; i<jsonArray.length(); i++) {
                        citylist.add(jsonArray.getString(i));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, citylist);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void savedata() {
        String title,description,pincode,full_address,name,phone;
        title = title_et.getEditText().getText().toString();
        description = description_et.getEditText().getText().toString();
        pincode = pincode_et.getEditText().getText().toString();
        full_address = full_address_et.getEditText().getText().toString();
        name = name_et.getEditText().getText().toString();
        phone = phone_et.getEditText().getText().toString();

        if (isempity_et(title,description,pincode,full_address,name,phone)){

            // image listvalidation
            if (imagesEncodedList.isEmpty())
            {
                Toast.makeText(FormActivity.this, "choose some image", Toast.LENGTH_SHORT).show();

            }
            //latitude and longitude validation
            else if (latitude==null && longitude==null){
                Toast.makeText(FormActivity.this, "pick location", Toast.LENGTH_SHORT).show();
                choose_location.requestFocus();
            }
            // aadhar card validation
            else if (aadhar_f==null && aadhar_b==null)
            {
                Toast.makeText(FormActivity.this, "choose  aadhar card photo", Toast.LENGTH_SHORT).show();
                aadhar_font_iv.requestFocus();
            }
            //state spinner validation
            else if (state.getSelectedItem().toString().equals("Choose State")){
                TextView errorText = (TextView)state.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("choose state");//changes the selected item text to this
                errorText.requestFocus();
//                Toast.makeText(this, "spinner state", Toast.LENGTH_SHORT).show();

            }

            //city spinner validation
            else if (city.getSelectedItem().toString().equals("choose district")){
                TextView errorText = (TextView)city.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("choose city");//changes the selected item text to this
                errorText.requestFocus();
//                Toast.makeText(this, "spinner city", Toast.LENGTH_SHORT).show();

            }

            //category spinner validation
            else if (category.getSelectedItem().toString().equals("Choose Category")){
                TextView errorText = (TextView)category.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("choose category");//changes the selected item text to this
                errorText.requestFocus();
//                Toast.makeText(this, "spinner catagory", Toast.LENGTH_SHORT).show();

            }
            else {
                progressDialog.show();
                List<String> downloaded_img_url = new ArrayList<>();
                Toast.makeText(this, ""+imagesEncodedList.size(), Toast.LENGTH_SHORT).show();

                for(i=0; i<imagesEncodedList.size(); i++) {
                    Uri uri = imagesEncodedList.get(i);
                    String uuid = UUID.randomUUID().toString();
                    StorageReference imgref = storageRef.child("image/"+auth.getCurrentUser().getUid()+"/"+uuid+".jpg");

                    UploadTask uploadTask =  imgref.putFile(uri);
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return imgref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
//                    upload done
                                Uri downloadUri = task.getResult();
                                downloaded_img_url.add(downloadUri.toString());
//                        Log.d("img_url",downloadUri.toString()+" i "+i);
                                Log.e("I +uris size",i+" "+downloaded_img_url.size());
                                //oncomple ke phele chcek kr
                                if (i==downloaded_img_url.size())
                                {
                                    String uuid = UUID.randomUUID().toString();
                                    String aadharfront =  upload_single_images(aadhar_f,"front");
                                    String aadharback= upload_single_images(aadhar_b,"back");
                                    String state_v,category_v,city_v;
                                    state_v = state.getSelectedItem().toString();
                                    city_v = city.getSelectedItem().toString();
                                    category_v = category.getSelectedItem().toString();
                                    aadhar_image.add("img 1");
                                    aadhar_image.add("img 2");
                                    FormBeen been = new FormBeen(title,description,category_v,state_v,city_v,pincode,full_address,latitude,longitude,name,uuid,phone,downloaded_img_url,aadhar_image);

                                    db.collection(path).document(uuid).set(been)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(FormActivity.this,HomeActivity.class));
                                                    finish();
                                                    Toast.makeText(FormActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(FormActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        }

    }

    private boolean isempity_et(String title, String description, String pincode, String full_address, String name, String phone ) {
        if (title.isEmpty())
        {
            title_et.setError("enter title");
            title_et.requestFocus();

        }
        else if (description.isEmpty())
        {
            description_et.setError("enter description");
            description_et.requestFocus();
        }
        else if (pincode.isEmpty())
        {
            pincode_et.setError("enter pincode");
            pincode_et.requestFocus();
        }
        else if (full_address.isEmpty())
        {
            full_address_et.setError("enter address");
            full_address_et.requestFocus();
        }
        else if (name.isEmpty())
        {
            name_et.setError("enter name");
            name_et.requestFocus();
        }
        else if (phone.isEmpty())
        {
            phone_et.setError("enter phone");
            phone_et.requestFocus();
        }
        else {
            return true;
        }

        return false;
    }

    void textWatcher(TextInputLayout textInputLayout){
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setError(null);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 0:
                try {
                    if(requestCode == PICK_IMAGE_MULTIPLE) {
                        if(resultCode == RESULT_OK) {
                            if(data.getClipData() != null) {
                                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                                if (count>10)
                                {
                                    Toast.makeText(this, "max 10 image are allow", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    for(int i = 0; i < count; i++)
                                    {
                                        imageUri = data.getClipData().getItemAt(i).getUri();
                                        imagesEncodedList.add(imageUri);

                                    }
                                }

                                photoAdpater = new PhotoAdpater(getApplicationContext(),imagesEncodedList);
                                gridLayout.setAdapter(photoAdpater);
                                gridLayout.setVisibility(View.VISIBLE);

                                //do something with the image (save it to some directory or whatever you need to do with it here)
                            }
                        } else if(data.getData() != null) {
                            String imagePath = data.getData().getPath();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                        }
                        else {
                            Toast.makeText(this, "You haven't picked Image null data",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "You haven't picked Image "+e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }

                break;
            case 1:
                if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == RESULT_OK)) {
                    Place place = PingPlacePicker.getPlace(data);
                    if (place != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        latitude= String.valueOf(place.getLatLng().latitude);
                        longitude= String.valueOf(place.getLatLng().longitude);
                        stringBuilder.append("Latitude: ");
                        stringBuilder.append(latitude);
                        stringBuilder.append("\n");
                        stringBuilder.append("longitude: ");
                        stringBuilder.append(longitude);
                        longlat.setText(stringBuilder.toString());

                    }
                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_1) {
                    //TODO: action
                    if (data!=null)
                    {
                        aadhar_f = data.getData();
                        aadhar_font_iv.setImageURI(aadhar_f);

                    }
                    else {
                        Toast.makeText(this, "u have not choose any image", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case 3:
                if (requestCode == PICK_IMAGE_2) {
                    //TODO: action
                    if (data!=null)
                    {
                         aadhar_b = data.getData();
                        aadhar_back_iv.setImageURI(aadhar_b);
                    }
                    else {
                        Toast.makeText(this, "u have not choose any image", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyB2OpM2q8MXmvLhTCay6FMJw9nJkYoTOfE")
                .setMapsApiKey("AIzaSyDXgmstHoP6wmE_MZx2EkT2Iaa2RnEzoHQ");

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
        }
        catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(FormActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(FormActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            Toast.makeText(FormActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(FormActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(FormActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(FormActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FormActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void upload_images_from_list(ArrayList<Uri> uris)
    {
         progressDialog.show();
        List<String> downloaded_img_url = new ArrayList<>();
        Toast.makeText(this, ""+uris.size(), Toast.LENGTH_SHORT).show();


        for(i=0; i<uris.size(); i++) {
            Uri uri = uris.get(i);
            String uuid = UUID.randomUUID().toString();
            StorageReference imgref = storageRef.child("image/"+auth.getCurrentUser().getUid()+"/"+uuid+".jpg");

            UploadTask uploadTask =  imgref.putFile(uri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return imgref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
//                    upload done
                        Uri downloadUri = task.getResult();
                        downloaded_img_url.add(downloadUri.toString());
//                        Log.d("img_url",downloadUri.toString()+" i "+i);
                        Log.e("I +uris size",i+" "+downloaded_img_url.size());
                        //oncomple ke phele chcek kr
                        if (i==downloaded_img_url.size())
                        {
                            progressDialog.dismiss();
                        }

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(FormActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
    String upload_single_images(Uri uri,String tag)
    {
        final String[] downloadUri = new String[1];
        StorageReference imgref = storageRef.child("aadhar/"+auth.getCurrentUser().getUid()+tag+".jpg");
        UploadTask uploadTask =  imgref.putFile(uri);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return imgref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
//                    upload done
                    downloadUri[0] = task.getResult().toString();
                    Log.d("myurl", downloadUri[0].toString());
                    Toast.makeText(FormActivity.this, "sucessfully uplaod\n", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FormActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        return downloadUri[0];


    }
}