package com.firman.jakapi.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firman.jakapi.R;
import com.firman.jakapi.adapter.ViewPagerAdapter;
import com.firman.jakapi.helper.Pref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroSliderActivity extends AppCompatActivity {

    @BindView(R.id.viewPagerIntro)
    ViewPager viewPagerIntro;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.buttonSkip)
    Button buttonSkip;
    @BindView(R.id.buttonNext)
    Button buttonNext;

    private Pref pref;
    TextView[] textDots;
    int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        ButterKnife.bind(this);
        pref = new Pref(this);
        if (!pref.isFirstTimeLaunch()) {
            lauchHome();
        }

        layouts = new int[]{
                R.layout.intro_slide_satu,
                R.layout.intro_slide_dua,
                R.layout.intro_slide_tiga,
                R.layout.intro_slide_empat
        };

        addBottomDots(0);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), layouts);
        viewPagerIntro.setAdapter(viewPagerAdapter);
        viewPagerIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                if (position + 1 == layouts.length) {
                    buttonNext.setText("START");
                    buttonSkip.setVisibility(View.GONE);
                } else {
                    buttonNext.setText("NEXT");
                    buttonSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int i) {
        textDots = new TextView[layouts.length];
        int colorActive[] = getResources().getIntArray(R.array.array_dot_activ);
        int colorNonActive[] = getResources().getIntArray(R.array.array_dot_noactiv);
        layoutDots.removeAllViews();

        for (int a = 0; a < textDots.length; a++) {
            textDots[a] = new TextView(this);
            textDots[a].setText(Html.fromHtml("&#8226"));
            textDots[a].setTextSize(35);
            textDots[a].setTextColor(colorNonActive[i]);
            layoutDots.addView(textDots[a]);
        }

        if (textDots.length > 0) {
            textDots[i].setTextColor(colorActive[i]);
        }
    }

    private void lauchHome() {
        pref.setFirstLaunched(false);
        startActivity(new Intent(IntroSliderActivity.this, MainActivity.class));
        finish();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    @OnClick({R.id.buttonSkip, R.id.buttonNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSkip:
                lauchHome();
                break;
            case R.id.buttonNext:
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPagerIntro.setCurrentItem(current);
                } else {
                    lauchHome();
                }
                break;
        }
    }

    private int getItem(int i) {
        return viewPagerIntro.getCurrentItem() + i;
    }
}

