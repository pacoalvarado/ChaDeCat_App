package com.alvarado.chadecat_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarado.chadecat_app.databinding.ActivityPerfilBinding;
import com.alvarado.chadecat_app.databinding.FragmentSecondBinding;
import com.alvarado.chadecat_app.ui.perfil.PerfilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class secondFragment extends Fragment {
    private FragmentSecondBinding binding;

    FirebaseUser fUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String comEmail;
    TextView tvName, tvEmail, tvModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);


        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String user = fUser.getEmail();

        comEmail = user;

        ReadUser();

        binding = FragmentSecondBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment perfilFragment = new PerfilFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.replace, perfilFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void ReadUser() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tvName = getActivity().findViewById(R.id.tvName);
                                tvEmail = getActivity().findViewById(R.id.tvEmail);
                                tvModel = getActivity().findViewById(R.id.tvModel);



                                if (document.get("email").equals(comEmail)) {
                                    String nameFinal = document.get("name").toString();
                                    String emailFinal = document.get("email").toString();
                                    String modelFinal = document.get("model").toString();



                                    tvName.setText(nameFinal);
                                    tvEmail.setText(emailFinal);
                                    tvModel.setText(modelFinal);
                                }


                            }
                        } else {
                        }
                    }
                });
    }
}