package it.uniba.di.ivu.sms16.gruppo2.dibapp.studysession;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniba.di.ivu.sms16.gruppo2.dibapp.R;
import it.uniba.di.ivu.sms16.gruppo2.dibapp.adapter.MyNoteAdapter;
import it.uniba.di.ivu.sms16.gruppo2.dibapp.models.Note;
import it.uniba.di.ivu.sms16.gruppo2.dibapp.viewholder.NoteViewHolder;


public class NotesFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_SESSION_ID = "session_id";

    private String mUserId;
    private String mSessionId;

    private DatabaseReference mFirebaseDatabaseReference;

    private ProgressBar mProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mNotesRecyclerView;


    public NotesFragment() {
        // Required empty public constructor
    }


    public static NotesFragment newInstance(String userId, String sessionId) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putString(ARG_SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_USER_ID);
            mSessionId = getArguments().getString(ARG_SESSION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.notesProgressBar);
        mNotesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.notesRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mNotesRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("notes");

        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Note> notes = new ArrayList<Note>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note currentNote = ds.getValue(Note.class);

                    if (currentNote.studySession != null &&
                            currentNote.studySession.equals(mSessionId)) {

                        notes.add(currentNote);
                    }
                }

                mProgressBar.setVisibility(View.GONE);
                mNotesRecyclerView.setLayoutManager(mLinearLayoutManager);
                mNotesRecyclerView.setAdapter(new MyNoteAdapter(notes, getContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
