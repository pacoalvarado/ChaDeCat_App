package com.alvarado.chadecat_app.ui.other;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.databinding.ActivityPerfilBinding;


public class OtherOptions extends Fragment {

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_options, container, false);
    }
}