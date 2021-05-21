package com.developfuture.fortknox.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.NewWordActivity;
import com.developfuture.fortknox.R;
import com.developfuture.fortknox.Word;
import com.developfuture.fortknox.WordListAdapter;
import com.developfuture.fortknox.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private WordViewModel mWordViewModel;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final ImageButton imageButton1 = root.findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = root.findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = root.findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = root.findViewById(R.id.imageButton4);
        final ImageButton imageButton5 = root.findViewById(R.id.imageButton5);
        //final ImageButton imageButton6 = root.findViewById(R.id.imageButton6);

        final FloatingActionButton detailedAdd = (FloatingActionButton) root.findViewById(R.id.fab);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        });

        detailedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.add_transaction);
                fbDialogue.setCanceledOnTouchOutside(true);
                fbDialogue.show();

                Spinner spinner = fbDialogue.findViewById(R.id.addTransactionSpinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.add_transaction_names, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final ImageButton closeButton = fbDialogue.findViewById(R.id.addTransactionClose);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fbDialogue.dismiss();
                    }
                });
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mWordViewModel.getAllWords().observe(getViewLifecycleOwner(), words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    R.string.empty_not_saved,
//                    Toast.LENGTH_LONG).show();
        }
    }
}