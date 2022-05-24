package com.alvarado.chadecat_app.ui.perfil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.databinding.ActivityPerfilBinding;
import com.alvarado.chadecat_app.databinding.FragmentHomeBinding;
import com.alvarado.chadecat_app.ui.home.HomeViewModel;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = ActivityPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String user = fUser.getEmail();

        comEmail = user;

        ReadUser();

        Log.e("ON CREATE", "perfil de OnCreate");

        return root;
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

                                //Log.e("A",document.getId() + " => " + document.getData());
                                //idUser = document.getId();
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
