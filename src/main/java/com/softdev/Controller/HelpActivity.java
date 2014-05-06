package com.softdev.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.softdev.R;


/**
 * Created by hannahbrown on 4/29/14.
 */
public class HelpActivity extends Activity{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.help_main);
    }

    public void optionsButtonClick(View v){
        openOptionsMenu();
    }
}
