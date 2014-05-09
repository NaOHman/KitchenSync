package com.softdev.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.softdev.R;


/**
 * Created by hannahbrown on 4/29/14.
 * A screen that shows help text to the user.
 */
public class HelpActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_main);
    }

    public void optionsButtonClick(View v){
        openOptionsMenu();
    }
}
