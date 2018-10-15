package com.example.newsfeed.newsassignment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This is the main activity for this application.
 * This activity holds two fragments, one is the main list items and other is the detailed screen that appearn upon news item click.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.showDetailsListener {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.all_stories_title));

        /**
         *All items are displayed in a fragment.
         */
        FragmentManager fm = getSupportFragmentManager();
        Fragment mTaskFragment = (MainFragment) fm.findFragmentByTag(MainFragment.FRAGMENT_TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new MainFragment();
            fm.beginTransaction().add(R.id.fragment_container, mTaskFragment, MainFragment.FRAGMENT_TAG).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * Thi call back opens the detailed of any news item on click.
     * @param arguments
     */
    @Override
    public void showDetails(Bundle arguments) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment mTaskFragment = (DetailedItemFragment) fm.findFragmentByTag(DetailedItemFragment.FRAGMENT_TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new DetailedItemFragment();
            mTaskFragment.setArguments(arguments);
            fm.beginTransaction().add(R.id.fragment_container, mTaskFragment, DetailedItemFragment.FRAGMENT_TAG).addToBackStack(null).commit();
            showTitle(false);
        }

    }

    /**
     * This updates the title.
     * @param show
     */
    @Override
    public void showTitle(boolean show) {
        if (toolbar != null) {

            if (show) {

                toolbar.setVisibility(View.VISIBLE);

            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }
}

