package com.messengers.swipechat.swipechats;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupFragment extends Fragment {

    private View GroupFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_group= new ArrayList<>();
    private DatabaseReference GroupRef;

    public GroupFragment()
    {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GroupFragmentView= inflater.inflate(R.layout.fragment_group, container, false);
        GroupRef= FirebaseDatabase.getInstance().getReference().child("Groups");

        InitializeField();
        RetriveAndDisplayGroup();

        return GroupFragmentView;
    }



    private void RetriveAndDisplayGroup()
    {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set< String> set =new HashSet<>();

                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());

                }

                list_group.clear();
                list_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void InitializeField()
    {
        listView=(ListView) GroupFragmentView.findViewById(R.id.ListView_id);
        arrayAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_group);

        listView.setAdapter(arrayAdapter);



    }

}
