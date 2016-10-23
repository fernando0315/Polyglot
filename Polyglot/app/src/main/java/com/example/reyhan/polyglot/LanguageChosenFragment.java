package com.example.reyhan.polyglot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Reyhan on 10/22/2016.
 */

public class LanguageChosenFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*****
     * Create the language chosen view and get the updated language that user choose
     * and put the flag picture on to the ui
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_languagechosen, container, false);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.chosen_flag);

        Bundle bundle = getArguments();

        if(bundle != null) {
            imageButton.setBackgroundResource(bundle.getInt("lang"));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newfragment = new LanguageListFragment();
                Bundle bundle = new Bundle();
                newfragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, newfragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return rootView;

    }
}
