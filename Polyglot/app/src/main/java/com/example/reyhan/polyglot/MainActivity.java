package com.example.reyhan.polyglot;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/*******
 * add fragment and put it into this activity
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LanguageListFragment())
                    .commit();
        }
    }


}