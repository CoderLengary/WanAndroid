package com.example.lengary_l.wanandroid.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lengary_l.wanandroid.R;

/**
 * Created by CoderLengary
 */


public class OnboardingFragment extends Fragment {

    private AppCompatTextView sectionLabel;
    private AppCompatTextView sectionIntro;
    private ImageView sectionImg;

    private int page;


    private static final String ARG_SECTION_NUMBER = "section_number";

    public OnboardingFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OnboardingFragment newInstance(int sectionNumber) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        initView(view);
        switch (page){
            case 0:
                sectionImg.setImageResource(R.drawable.ic_camera_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_1);
                sectionIntro.setText(R.string.onboarding_intro_1);
                break;

            case 1:
                sectionImg.setImageResource(R.drawable.ic_notifications_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_2);
                sectionIntro.setText(R.string.onboarding_intro_2);
                break;

            case 2:
                sectionImg.setImageResource(R.drawable.ic_beenhere_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_3);
                sectionIntro.setText(R.string.onboarding_intro_3);
                break;

                default:break;
        }
        return view;
    }

    private void initView(View view){
        sectionImg = view.findViewById(R.id.section_img);
        sectionLabel = view.findViewById(R.id.section_label);
        sectionIntro = view.findViewById(R.id.section_intro);
    }
}
