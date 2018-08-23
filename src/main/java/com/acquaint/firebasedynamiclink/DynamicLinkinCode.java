package com.acquaint.firebasedynamiclink;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.net.URL;
import java.net.URLDecoder;

public class DynamicLinkinCode extends AppCompatActivity {
    private Button bt_share,bt_generate_link;
    private EditText et_postid,et_userid;
    private String post_id;
    private Uri dynamicLink;
    private Uri shortLink;

    private static String TAG = DynamicLinkinCode.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_linkin_code);
        et_postid=findViewById(R.id.et_postid);
        et_userid=findViewById(R.id.et_userid);
        bt_share=findViewById(R.id.share);
        bt_share.setEnabled(false);
        bt_generate_link=findViewById(R.id.bt_generate_link);


        bt_generate_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    if(isNetworkAvailable(DynamicLinkinCode.this)){
                        generateLink();
                    }
                    else {
                        Toast.makeText(DynamicLinkinCode.this, R.string.er_internet,Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(bt_share.isEnabled()){
                        URL url = new URL(URLDecoder.decode(shortLink.toString(), getString(R.string.utf)));

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.firebase_deep_link));
                        intent.putExtra(Intent.EXTRA_TEXT, url.toString());
                        startActivity(intent);
                        bt_share.setEnabled(false);
                        et_postid.setText("");
                        et_userid.setText("");
                    }
                    else {
                        bt_share.setError(getString(R.string.error_wait));
                    }


                } catch (Exception e) {
                    Log.i(TAG, getString(R.string.er_uri) + e.getLocalizedMessage());
                }
            }
        });
    }

    private boolean validation() {
        if(et_postid.getText().length()<=0){
            et_postid.setError(getString(R.string.er_postid));
            return false;
        }
        else if(et_userid.getText().length()<=0){
            et_userid.setError(getString(R.string.er_userid));
            return false;

        }
        else {
            return true;
        }

    }

    private boolean generateLink() {
        if(et_postid.getText().length()>0){
            post_id=et_postid.getText().toString().trim()+ "," + et_userid.getText().toString().trim();


     /*   Uri deepLink = Uri.parse("https://demoapp.com/post");*/
        Uri.Builder linkBuilder = new Uri.Builder().scheme(getString(R.string.https)).authority(getString(R.string.appcode))
                .appendQueryParameter(getString(R.string.postid),post_id);
        Uri deepLink=linkBuilder.build();

        String packageName = getApplicationContext().getPackageName();

        Uri.Builder builder = new Uri.Builder()
                .scheme(getString(R.string.https))
                .authority(getString(R.string.appcode))
                .path("/")
                .appendQueryParameter("link", deepLink.toString())
                .appendQueryParameter("apn", packageName)
                .appendQueryParameter("ibi","com.acquaintsoft.dyanamic")
                .appendQueryParameter("iosCustomScheme","dlscheme");

                 dynamicLink = builder.build();


            final Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse(String.valueOf(dynamicLink)))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                               shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Log.e(TAG,getString(R.string.flowchart_link)+flowchartLink.toString());
                                Toast.makeText(DynamicLinkinCode.this,""+flowchartLink,Toast.LENGTH_LONG).show();
                                Toast.makeText(DynamicLinkinCode.this,""+shortLink,Toast.LENGTH_LONG).show();
                                bt_share.setEnabled(true);

                            } else {
                                Log.e(getString(R.string.task),getString(R.string.success)+task.isSuccessful());

                            }
                        }
                    });

            return true;
        }
        else {

            return false;
        }

    }
    public static boolean isNetworkAvailable(Context context) {
        final Context appContext = context;
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
