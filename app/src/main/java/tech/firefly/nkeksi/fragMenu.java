package tech.firefly.nkeksi;


import android.content.Context;
import android.content.Intent;
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

        menuList = (RecyclerView) view.findViewById(R.id.resto_menu_list);
        menuList.setLayoutManager(new LinearLayoutManager(view.getContext()));



        if (key != null && !key.isEmpty()) {
            menuRef = FirebaseDatabase.getInstance().getReference().child("Menu").child("MenuPics");

            FirebaseRecyclerAdapter<MenuCategoryModel, OnlineMenuHolder> adapterOnline =
                    new FirebaseRecyclerAdapter<MenuCategoryModel, OnlineMenuHolder>(MenuCategoryModel.class, R.layout.single_menu_category,
                            OnlineMenuHolder.class, menuRef) {
                        @Override
                        protected void populateViewHolder(final OnlineMenuHolder viewHolder, MenuCategoryModel model, int position) {
                            final String keyMe = getRef(position).getKey();
                            viewHolder.setImage(getActivity(), model.getImage());
                            viewHolder.setName(model.getName());

                            if (keyMe != null) {
                                DatabaseReference menuReference = FirebaseDatabase.getInstance().getReference().child("Menu").child(key).child(keyMe);
                                menuReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        long count = dataSnapshot.getChildrenCount();
                                        viewHolder.catCount.setText(String.valueOf(count));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(),FoodItemActivity.class);
                                        i.putExtra("prevKey",key);
                                        i.putExtra("currKey",keyMe);
                                        getActivity().startActivity(i);
                                    }
                                });

                            }
                        }
                    };
            menuList.setLayoutManager(new LinearLayoutManager(getActivity()));
            menuList.setAdapter(adapterOnline);


        }

        FirebaseDatabase.getInstance().getReference().child("Menu").child("MenuPics").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String name = dataSnapshot.getKey();
                List<String> gotten = new ArrayList<>();
                gotten.add(name);
                //Toast.makeText(getActivity(), ""+gotten.size(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), ""+gotten.get(2), Toast.LENGTH_SHORT).show();
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
        TextView catCount;
        View mView;

        public OnlineMenuHolder(View itemView) {
            super(itemView);
            mView = itemView;
            catCount = itemView.findViewById(R.id.categoryCount);
        }

        public void setName(String name) {
            catName = itemView.findViewById(R.id.categoryName);
            catName.setText(name);
        }

        public void setImage(final Context c, final String image) {
            catImage = itemView.findViewById(R.id.categoryImage);
            if (!image.isEmpty() && image != null) {
                Picasso.with(c).load(image).placeholder(R.drawable.defaultloader).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(catImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(c).load(image).placeholder(R.drawable.defaultloader).into(catImage);
                            }
                        });
            }
        }

    }


}
