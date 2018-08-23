package com.acquaint.firebasedynamiclink;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.Arrays;
import java.util.List;

public class ReceiveDynamicLink extends AppCompatActivity  {

    private TextView tv_link;
   public static String TAG=ReceiveDynamicLink.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_dynamic_link);
        tv_link=findViewById(R.id.tv_dynamiclink);




        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e("link","link"+deepLink);
                            // Handle the deep link. For example, open the linked
                            // content,go to a particular post or apply promotional credit to the user's
                            // account.
                            // ...

                            // ...
                            handleDeeplink(deepLink.toString());
                        }



                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });



    }

    public void handleDeeplink(String deepLink) {

        Uri deepUri = Uri.parse(deepLink);

        String paramValue = deepUri.getQueryParameter("postid");

        List<String> paramList = Arrays.asList(paramValue.split(","));
        tv_link.setText("Postid is: "+paramList.get(0) + " User id is: " +paramList.get(1));

    }

}
