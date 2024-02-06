package com.waayu.owner.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.waayu.owner.R;
import com.waayu.owner.model.ProductCategoryData;
import com.waayu.owner.model.ProductData;
import com.waayu.owner.model.ResponseCP;
import com.waayu.owner.model.Store;
import com.waayu.owner.retrofit.APIClient;
import com.waayu.owner.retrofit.GetResult;
import com.waayu.owner.utils.CustPrograssbar;
import com.waayu.owner.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;

public class ProductEditActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, GetResult.MyListener {
    TextView txt_choosebtn;
    ImageView img_product;
    private static final String TAG = ProductEditActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    Store user;
    Spinner cat_menu_spinner;
    Spinner statusspinner;
    EditText edt_itemname, edt_item_base_price, edt_item_discount_price;
    TextView txt_save, txt_cancel, editTextTime1, editTextTime2, editTextEveTime1, editTextEveTime2;
    EditText etText,edt_item_gst;
    Switch switch_recommonded, switch_egg, switch_customize, switch_Veg;
    ImageView img_back;
    private String dataItem;
    private ProductData response;
    private ArrayList<String> getStringArray;
    private ProductCategoryData productCategoryData;
    private TimePickerDialog timePickerDialog;
    String image64 = "";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 3;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        img_back = findViewById(R.id.img_back);
        txt_choosebtn = findViewById(R.id.txt_choosebtn);
        img_product = findViewById(R.id.img_product);
        statusspinner = findViewById(R.id.statusspinner);
        cat_menu_spinner = findViewById(R.id.cat_menu_spinner);
        switch_recommonded = findViewById(R.id.switch_recommonded);
        switch_egg = findViewById(R.id.switch_egg);
        //switch_customize = findViewById(R.id.switch_customize);
        switch_Veg = findViewById(R.id.switch_Veg);
        edt_itemname = findViewById(R.id.edt_itemname);
        edt_item_base_price = findViewById(R.id.edt_item_base_price);
        edt_item_discount_price = findViewById(R.id.edt_item_discount_price);
        etText = findViewById(R.id.etText);
        txt_save = findViewById(R.id.txt_save);
        editTextTime1 = findViewById(R.id.editTextTime1);
        editTextTime2 = findViewById(R.id.editTextTime2);
        editTextEveTime1 = findViewById(R.id.editTextEveTime1);
        editTextEveTime2 = findViewById(R.id.editTextEveTime2);
        txt_cancel = findViewById(R.id.txt_cancel);
        edt_item_gst = findViewById(R.id.edt_item_gst);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProductEditActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProductEditActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(ProductEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductEditActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_EXTERNAL_STORAGE_PERMISSION);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission has not been granted, request it
                            requestStoragePermission();
                        } else {
                            // Permission already granted, proceed with your logic
                        }
                    }
                }
            }
        }


        txt_choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });
        editTextTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(ProductEditActivity.this, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        editTextTime1.setText(selectedTime);
                    }
                }, 12, 0, false);
                timePickerDialog.show();

            }
        });
        editTextTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(ProductEditActivity.this, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        editTextTime2.setText(selectedTime);
                    }
                }, 12, 0, false);
                timePickerDialog.show();

            }
        });
        editTextEveTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(ProductEditActivity.this, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        editTextEveTime1.setText(selectedTime);
                    }
                }, 12, 0, false);
                timePickerDialog.show();

            }
        });
        editTextEveTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(ProductEditActivity.this, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        editTextEveTime2.setText(selectedTime);
                    }
                }, 12, 0, false);
                timePickerDialog.show();

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch_egg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_Veg.setChecked(false);
                }
            }
        });
        switch_Veg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_egg.setChecked(false);
                }
            }
        });


        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCat = cat_menu_spinner.getSelectedItem().toString();
                String getMenuName = edt_itemname.getText().toString();
                String getBasePrice = edt_item_base_price.getText().toString();
                String getDiscountedPrice = edt_item_discount_price.getText().toString();
                String menuItemStatus = statusspinner.getSelectedItem().toString();

                String morningMenuTime = editTextTime1.getText().toString();
                String moriningCloseTime = editTextTime2.getText().toString();
                String eveningOpenTime = editTextEveTime1.getText().toString();
                String eveningCloseTime = editTextEveTime2.getText().toString();
                String getVegStatus = String.valueOf(switch_Veg.isChecked());
                // String getCustomizeStatus = String.valueOf(switch_customize.isChecked());
                String getEggStatus = String.valueOf(switch_egg.isChecked());
                String getRecommondedStatus = String.valueOf(switch_recommonded.isChecked());
                String getDescription = etText.getText().toString();
                String getGST = edt_item_gst.getText().toString();
                for (int i = 0; i < productCategoryData.getResultData().getProduct_Data().size(); i++) {
                    if (getCat.equals(productCategoryData.getResultData().getProduct_Data().get(i).getTitle())) {
                        getCat = productCategoryData.getResultData().getProduct_Data().get(i).getId();
                    }
                }
                if (isValidate(getCat, getMenuName, getBasePrice, getDiscountedPrice, menuItemStatus, morningMenuTime, moriningCloseTime, eveningCloseTime, eveningOpenTime, getDescription,getGST)) {
                    postApi(getCat, getMenuName, getBasePrice, getDiscountedPrice, menuItemStatus, morningMenuTime, moriningCloseTime, eveningOpenTime, eveningCloseTime, getVegStatus, getEggStatus, getRecommondedStatus, getDescription,getGST);
                }
            }

        });

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ProductEditActivity.this);
        user = sessionManager.getUserDetails("");
        dataItem = getIntent().getStringExtra("pid");
        getProdCat();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        // Request the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
            case REQUEST_EXTERNAL_STORAGE_PERMISSION:
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, continue with your app logic...
                } else {
                    // Permission denied, handle accordingly...
                }
                break;
        }
    }

    private void postApi(String getCat, String getMenuName, String getBasePrice, String getDiscountedPrice, String menuItemStatus, String morningMenuTime, String moriningCloseTime, String eveningOpenTime, String eveningCloseTime, String getVegStatus, String getEggStatus, String getRecommondedStatus, String getDescription, String getGST) {
        custPrograssbar.prograssCreate(ProductEditActivity.this);
        if (menuItemStatus.equals("Active")) {
            menuItemStatus = "1";
        } else {
            menuItemStatus = "0";
        }
       /* if (getCustomizeStatus.equals("false")) {
            getCustomizeStatus = "0";
        } else {
            getCustomizeStatus = "1";
        }*/
        if (getVegStatus.equals("false")) {
            getVegStatus = "0";
        } else {
            getVegStatus = "1";
        }
        if (getEggStatus.equals("false")) {
            getEggStatus = "0";
        } else {
            getEggStatus = "1";
        }
        if (getRecommondedStatus.equals("false")) {
            getRecommondedStatus = "0";
        } else {
            getRecommondedStatus = "1";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", response.getProductdetails().getId());
            jsonObject.put("title", getMenuName);
            jsonObject.put("status", menuItemStatus);
            jsonObject.put("base_price", getBasePrice);
            jsonObject.put("discount_price", getDiscountedPrice);
            jsonObject.put("is_veg", getVegStatus);
            jsonObject.put("is_egg", getEggStatus);
            jsonObject.put("is_recommended", getRecommondedStatus);
            jsonObject.put("is_customize", response.getProductdetails().getIs_customize());
            jsonObject.put("is_variations", response.getProductdetails().getIs_variations());
            jsonObject.put("is_customize_time", response.getProductdetails().getIs_customize());
            jsonObject.put("cdesc", getDescription);
            jsonObject.put("gst", getGST);
            jsonObject.put("category_id", getCat);
            jsonObject.put("open_time", morningMenuTime);
            jsonObject.put("close_time", moriningCloseTime);
            jsonObject.put("evening_open_time", eveningOpenTime);
            jsonObject.put("evening_close_time", eveningCloseTime);
            jsonObject.put("product_image", image64);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().store_update_product_details((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidate(String getCat, String getMenuName, String getBasePrice, String getDiscountedPrice, String menuItemStatus, String morningMenuTime, String moriningCloseTime, String eveningCloseTime, String eveningOpenTime, String getDescription, String getGST) {
        boolean flag = true;
        if (getCat.equals("Select Category")) {
            flag = false;
        }
        if (getMenuName.equals("")) {
            edt_itemname.setError("Please enter menu title");
            flag = false;
        }
        if (getBasePrice.equals("")) {
            edt_item_base_price.setError("Please enter base price");
            flag = false;
        }
        if (getDiscountedPrice.equals("")) {
            edt_item_discount_price.setError("Please enter discounted price");
            flag = false;
        }
        if (getDescription.equals("")) {
            etText.setError("Please enter product description");
            flag = false;
        }
        if (menuItemStatus.equals("Select Status")) {
            Toast.makeText(this, "Select Product Status", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (getGST.equals("")) {
            edt_item_gst.setError("Please enter product GST");
            flag = false;
        }
        if (morningMenuTime.equals("")) {
            flag = true;
        } else {
            if (moriningCloseTime.equals("")) {
                Toast.makeText(this, "Select Morning Menu Close Time", Toast.LENGTH_SHORT).show();
                flag = false;
            } else if (morningMenuTime.equals(moriningCloseTime)) {
                Toast.makeText(this, "Select Proper Morining Close Time", Toast.LENGTH_SHORT).show();
                flag = false;
            } else {
                flag = true;
            }
        }


        if (eveningOpenTime.equals("")) {
            flag = true;
        } else {
            if (eveningCloseTime.equals("")) {
                Toast.makeText(this, "Select Eveing Menu Close Time", Toast.LENGTH_SHORT).show();
                flag = false;
            } else if (eveningOpenTime.equals(eveningCloseTime)) {
                Toast.makeText(this, "Select Proper Evening Close Time", Toast.LENGTH_SHORT).show();
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;
    }

    private void getProductDetails() {
        custPrograssbar.prograssCreate(ProductEditActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", dataItem);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getProductDetails((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProdCat() {
        custPrograssbar.prograssCreate(ProductEditActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getREstroProduct((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {CAMERA_PERMISSION, STORAGE_PERMISSION};
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    showImagePickerOptions();
                } else {
                    if (EasyPermissions.hasPermissions(ProductEditActivity.this, permissions)) {
                        pickImage();
                    } else {
                        EasyPermissions.requestPermissions(ProductEditActivity.this, "This app needs camera and storage permissions.", PERMISSION_REQUEST_CODE, permissions);
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }

            @Override
            public void onChooseGalleryRemoveSelected() {
                Glide.with(ProductEditActivity.this).load("").placeholder(R.drawable.blankwhite).into(img_product);
                image64 = "";
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ProductEditActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ProductEditActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void pickImage() {
        PackageManager packageManager = getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "This device doesn't have a camera", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }


      /*  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                    image64 = getFileToByte(uri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFileToByte(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        Glide.with(this).load(url).into(img_product);
        img_product.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void saveBitmap(Bitmap bitmap) {
        String folderPath = "/sdcard/Waayu/";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "image_" + System.currentTimeMillis() + ".png";
        File file = new File(folder, fileName);

        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            Toast.makeText(this, "Image saved to " + folderPath + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                productCategoryData = gson.fromJson(result.toString(), ProductCategoryData.class);
                if (productCategoryData.getResult().equalsIgnoreCase("true")) {
                    getStringArray = new ArrayList<>();
                    for (int i = 0; i < productCategoryData.getResultData().getProduct_Data().size(); i++) {
                        getStringArray.add(productCategoryData.getResultData().getProduct_Data().get(i).getTitle());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getStringArray);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cat_menu_spinner.setAdapter(arrayAdapter);
                    getProductDetails();
                } else {
//                    txtNodata.setVisibility(View.VISIBLE);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                response = gson.fromJson(result.toString(), ProductData.class);
                if (response.getmResult().equalsIgnoreCase("true")) {
                    if (response.getProductdetails().getTitle() != null && !response.getProductdetails().getTitle().equals("")) {
                        edt_itemname.setText(response.getProductdetails().getTitle());
                    }
                    if (response.getProductdetails().getEvening_open_time() != null && !response.getProductdetails().getEvening_open_time().equals("")) {
                        editTextEveTime1.setText(response.getProductdetails().getEvening_open_time());
                    }
                    if (response.getProductdetails().getEvening_close_time() != null && !response.getProductdetails().getEvening_close_time().equals("")) {
                        editTextEveTime2.setText(response.getProductdetails().getEvening_close_time());
                    }
                    if (response.getProductdetails().getBase_price() != null && !response.getProductdetails().getBase_price().equals("")) {
                        edt_item_base_price.setText(response.getProductdetails().getBase_price());
                    }
                    if (response.getProductdetails().getDiscount_price() != null && !response.getProductdetails().getDiscount_price().equals("")) {
                        edt_item_discount_price.setText(response.getProductdetails().getDiscount_price());
                    }

                    if (response.getProductdetails().getGst() != null && !response.getProductdetails().getGst().equals("")) {
                        edt_item_gst.setText(response.getProductdetails().getGst());
                    }
                    if (response.getProductdetails().getItem_img() != null && !response.getProductdetails().getItem_img().equals("")) {
                        Glide.with(this).load(APIClient.baseUrl + "/" + response.getProductdetails().getItem_img()).placeholder(R.drawable.slider).into(img_product);
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    URL imageUrl = new URL(APIClient.baseUrl + "/" + response.getProductdetails().getItem_img());
                                    URLConnection ucon = imageUrl.openConnection();
                                    InputStream is = ucon.getInputStream();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[1024];
                                    int read = 0;
                                    while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                                        baos.write(buffer, 0, read);
                                    }
                                    baos.flush();
                                    image64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                                } catch (Exception e) {
                                    Log.d("Error", e.toString());
                                }
                            }
                        };

                        thread.start();


                    }
                    if (response.getProductdetails().getCdesc() != null && !response.getProductdetails().getCdesc().equals("")) {
                        etText.setText(response.getProductdetails().getCdesc());
                    }
                    if (response.getProductdetails().getCategory_id() != null && !response.getProductdetails().getCategory_id().equals("")) {
                        for (int i = 0; i < productCategoryData.getResultData().getProduct_Data().size(); i++) {
                            if (productCategoryData.getResultData().getProduct_Data().get(i).getId().equals(response.getProductdetails().getCategory_id())) {
                                cat_menu_spinner.setSelection(i);
                            }
                        }
                    }
                    if (response.getProductdetails().getStatus() != null && !response.getProductdetails().getStatus().equals("")) {
                        if (response.getProductdetails().getStatus().equals("1")) {
                            statusspinner.setSelection(1);
                        } else if (response.getProductdetails().getStatus().equals("0")) {
                            statusspinner.setSelection(2);
                        }
                    } else {
                        statusspinner.setSelection(0);
                    }
                    if (response.getProductdetails().getIs_egg().equals("1")) {
                        switch_egg.setChecked(true);
                    } else {
                        switch_egg.setChecked(false);
                    }
                    if (response.getProductdetails().getIs_recommended().equals("1")) {
                        switch_recommonded.setChecked(true);
                    } else {
                        switch_recommonded.setChecked(false);
                    }

                    if (response.getProductdetails().getIs_veg().equals("1")) {
                        switch_Veg.setChecked(true);
                    } else {
                        switch_Veg.setChecked(false);
                    }
                    if (response.getProductdetails().getIs_customize().equals("1")) {
                        switch_customize.setChecked(true);
                    } else {
                        switch_customize.setChecked(false);
                    }


                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                ResponseCP response = gson.fromJson(result.toString(), ResponseCP.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    Toast.makeText(this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}