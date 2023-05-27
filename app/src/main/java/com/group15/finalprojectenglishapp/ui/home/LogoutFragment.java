package com.group15.finalprojectenglishapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group15.finalprojectenglishapp.R;
import com.group15.finalprojectenglishapp.taikhoan.LoginActivity;

public class LogoutFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        startActivity(new Intent(getActivity(), LoginActivity.class));

        return root;
    }
}
