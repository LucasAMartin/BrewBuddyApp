package com.example.brewbuddycs380;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * The activity that handles the question flow in the BrewBuddy app.
 */
public class QuestionActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ArrayList<String> titles;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar_question));
        }
        setContentView(R.layout.activity_question);

        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout);

        Button getStartedBtn = findViewById(R.id.getStarted);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles = new ArrayList<String>();
                titles.add("Q1");
                titles.add("Q2");
                titles.add("Q3");
                titles.add("Q4");
                titles.add("Q5");
                titles.add("Q6");
                titles.add("Q7");
                titles.add("S");
                setViewPagerAdapter();
                new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(titles.get(position));
                    }
                }).attach();
                getStartedBtn.setVisibility(View.GONE);
                findViewById(R.id.introText).setVisibility(View.GONE);
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setProgress((position + 1) * 15);
            }
        });
    }

    /**
     * Sets up the ViewPager2 adapter and adds the fragments to it.
     */
    public void setViewPagerAdapter() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new question1());
        fragmentList.add(new question2());
        fragmentList.add(new question3());
        fragmentList.add(new question4());
        fragmentList.add(new question5());
        fragmentList.add(new question6());
        fragmentList.add(new question7());
        fragmentList.add(new QuestionSubmit());
        viewPager2Adapter.setData(fragmentList);
        viewPager2.setAdapter(viewPager2Adapter);
    }
}
