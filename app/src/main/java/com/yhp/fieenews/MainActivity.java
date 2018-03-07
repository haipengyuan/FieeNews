package com.yhp.fieenews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.yhp.fieenews.ui.fragment.MainFragment;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {

    private boolean isFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.frame_layout_main, MainFragment.newInstance(), true, true);
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (isFinish) {
                finish();
            } else {
                isFinish = true;
                Toast.makeText(this, R.string.finish_reminder, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isFinish = false;
                    }
                }).start();
            }
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
