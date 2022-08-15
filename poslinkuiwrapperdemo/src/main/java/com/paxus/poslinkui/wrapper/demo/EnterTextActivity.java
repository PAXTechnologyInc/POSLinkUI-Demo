package com.paxus.poslinkui.wrapper.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.paxus.poslinkui.POSLinkUIWrapper;
import com.paxus.poslinkui.state.EnterAmount;

public class EnterTextActivity extends AppCompatActivity {
    
    private EnterAmount enterAmountState;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_text);
        
        if(savedInstanceState == null) {
            enterAmountState = getIntent().getParcelableExtra("state");
        }
    
        setTitle(enterAmountState.getTitle());
    
        EditText etAmount = findViewById(R.id.edit_text);
        
        Button confirm = findViewById(R.id.button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAmountState.setAmount(Long.parseLong(etAmount.getText().toString()));
                POSLinkUIWrapper.getInstance().setResult(enterAmountState);
                finish();
            }
        });
    }
}