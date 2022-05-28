package com.alvarado.chadecat_app.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.databinding.ActivityPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PerfilFragment extends Fragment {

    private ActivityPerfilBinding binding;

    FirebaseUser fUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String comEmail;
    TextView tvName, tvEmail, tvModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);


        binding = ActivityPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String user = fUser.getEmail();

        comEmail = user;

        ReadUser();


        binding = ActivityPerfilBinding.inflate(getLayoutInflater());


        return binding.getRoot();

    }

    public void onViewCreated(View view, Bundle savedInstanceState){

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
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



                                if (document.get("email").equals(comEmail)) {
                                    String nameFinal = document.get("name").toString();
                                    String emailFinal = document.get("email").toString();




                                    tvName.setText(nameFinal);
                                    tvEmail.setText(emailFinal);

                                }


                            }
                        } else {
                        }
                    }
                });
    }
}
