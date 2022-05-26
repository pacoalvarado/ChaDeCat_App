package com.alvarado.chadecat_app.ui.other;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OtherOptionsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public OtherOptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Perfil");
    }

    public OtherOptionsViewModel(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
