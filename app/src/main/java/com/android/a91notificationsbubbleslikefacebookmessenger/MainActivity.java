package com.android.a91notificationsbubbleslikefacebookmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

public class MainActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;
    private int MY_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBubble();
        Button button = (Button)findViewById(R.id.btnAddBubble);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewBubble();
            }
        });

        //permission
        if (Build.VERSION.SDK_INT>=23){
            if (!Settings.canDrawOverlays(MainActivity.this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,MY_PERMISSION);
            }
        }else {
            Intent intent = new Intent(MainActivity.this, Service.class);
            startActivity(intent);
        }
    }

    private void initBubble() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();

    }

    private void addNewBubble() {
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.buble_layout,null);
        mBadge = bubbleLayout.findViewById(R.id.count);
        mBadge.setNumber(88);

        bubbleLayout.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleLayout.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleLayout.setShouldStickToWall(true);bubblesManager.addBubble(bubbleLayout,60,20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }
}