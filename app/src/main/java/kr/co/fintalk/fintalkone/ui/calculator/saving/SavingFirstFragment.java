package kr.co.fintalk.fintalkone.ui.calculator.saving;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.CurrencyTextWatcher;

/**
 * Created by BeomyongChoi on 6/21/16.
 */
public class SavingFirstFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_saving, container, false);
    }
}