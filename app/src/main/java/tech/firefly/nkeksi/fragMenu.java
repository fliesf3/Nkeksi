package tech.firefly.nkeksi;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class fragMenu extends Fragment {

    RecyclerView menuList;
    DatabaseReference menuRef;
    String key = "";
    TextView textField;


    public fragMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        key = getActivity().getIntent().getStringExtra("key");

        Toast.makeText(getActivity(), "" + key, Toast.LENGTH_SHORT).show();

        //textField = (TextView) view.findViewById(R.id.textField);


        menuList = (RecyclerView) view.findViewById(R.id.resto_menu_list);
        menuList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //menuList.setAdapter(new OfflineAdapter());


        if (key != null && !key.isEmpty()) {
            menuRef = FirebaseDatabase.getInstance().getReference().child("Menu").child("MenuPics");

            FirebaseRecyclerAdapter<MenuCategoryModel, OnlineMenuHolder> adapterOnline =
                    new FirebaseRecyclerAdapter<MenuCategoryModel, OnlineMenuHolder>(MenuCategoryModel.class, R.layout.single_menu_category,
                            OnlineMenuHolder.class, menuRef) {
                        @Override
                        protected void populateViewHolder(OnlineMenuHolder viewHolder, MenuCategoryModel model, int position) {
                            viewHolder.setImage(getActivity(), model.getImage());
                            viewHolder.setName(model.getName());
                        }
                    };
            menuList.setLayoutManager(new LinearLayoutManager(getActivity()));
            menuList.setAdapter(adapterOnline);


        }
        final String[] getKey = {""};
       FirebaseDatabase.getInstance().getReference().child("Menu").child("MenuPics").addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Iterator i = dataSnapshot.getChildren().iterator();

               while(i.hasNext()) {
                   int j = 0;

                   String name = (String) ((DataSnapshot) i.next()).getKey();
                   Toast.makeText(getActivity(), "Size"+j+" = " + name, Toast.LENGTH_SHORT).show();
                   j++;
               }
           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }


    public static class OnlineMenuHolder extends RecyclerView.ViewHolder {
        ImageView catImage;
        TextView catName;

        public OnlineMenuHolder(View itemView) {
            super(itemView);
        }

        public void setName(String name) {
            catName = itemView.findViewById(R.id.categoryName);
            catName.setText(name);
        }

        public void setImage(final Context c, final String image) {
            catImage = itemView.findViewById(R.id.categoryImage);
            if (!image.isEmpty() && image != null) {
                Picasso.with(c).load(image).placeholder(R.drawable.cat_drink_com).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(catImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(c).load(image).placeholder(R.drawable.cat_drink_com).into(catImage);
                            }
                        });
            }
        }

    }


}
