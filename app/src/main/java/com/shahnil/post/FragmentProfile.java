package com.shahnil.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentProfile extends Fragment {

    public static final String LIST = "LIST";



    private RecyclerView mRecyclerView;
    private RecyclerAdaptorGrid mAdaptor;

    SwipeRefreshLayout pullToRefresh;



    private  ArrayList<userinformation> userlist;




    private FirebaseFirestore fdb ;
    private CollectionReference notebookref ;
    private FirebaseAuth mAuth;
    private String UserId;

    private  FloatingActionButton fab;

    private Button logoutBtn;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview =  inflater.inflate(R.layout.fragment_profile,container,false);


        userlist = new ArrayList<>();
        mRecyclerView = rootview.findViewById(R.id.recyclerviewGrid);
        mRecyclerView.setHasFixedSize(true);
        int spaceInPixels = 1;
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));
        //mLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);

        mAdaptor = new RecyclerAdaptorGrid(getActivity(),userlist);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdaptor);


        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        fdb = FirebaseFirestore.getInstance();


        notebookref = fdb.collection("Users").document(UserId).collection("posts");



        LoadList();


        fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentCreatePost()).commit();
            }
        });



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });



        mAdaptor.setOnitemclicklistener(new RecyclerAdaptorGrid.Onitemclicklistener() {
            @Override
            public void onitemclick(int position) {
                Toast.makeText(getActivity(), "Enlarge image", Toast.LENGTH_SHORT).show();
                userinformation user = userlist.get(position);
                Intent intent = new Intent(getActivity(), FullPagePost.class);
                intent.putExtra(LIST, user);
                startActivity(intent);

            }
        });





        pullToRefresh = rootview.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pullToRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(true);
                        LoadList();
                    }
                });


            }
        });


        logoutBtn = rootview.findViewById(R.id.logoutbtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SendOtpPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        return rootview;

    }



    private void LoadList(){


        notebookref.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        pullToRefresh.setRefreshing(false);

                        userlist.clear();


                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            userinformation list = documentSnapshot.toObject(userinformation.class);

                            userlist.add(list);

                        }

                        mAdaptor.notifyDataSetChanged();

                    }


                });


    }

}
