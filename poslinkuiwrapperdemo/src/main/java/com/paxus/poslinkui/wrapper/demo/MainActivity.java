package com.paxus.poslinkui.wrapper.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.paxus.poslinkui.IPOSLinkUICallback;
import com.paxus.poslinkui.POSLinkUIWrapper;
import com.paxus.poslinkui.state.EnterAmount;
import com.paxus.poslinkui.state.IPOSLinkUIState;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button start = findViewById(R.id.start_transaction);
    
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> startTransaction()).start();
            }
        });
    }
    
    private void startTransaction() {
        //enable state components in POSLinkUI wrapper
        POSLinkUIWrapper.getInstance().enableStates(getApplicationContext(),
                EnterAmount.class);
        
        //register callback
        POSLinkUIWrapper.getInstance().setCallback(new IPOSLinkUICallback() {
            @Override
            public void onStateChanged(IPOSLinkUIState state) {
                if(state instanceof EnterAmount) {
                    Intent intent = new Intent(MainActivity.this, EnterTextActivity.class);
                    intent.putExtra("state", state);
                    startActivity(intent);
                }
            }
        });
    
        //call POSLink SDK
        //Response = POSLink.xxxx;
        //
    
        //clean
        //POSLinkUIWrapper.getInstance().disableAllStates(getApplicationContext());
    }
}