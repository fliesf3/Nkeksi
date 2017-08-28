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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class fragMenu extends Fragment {

    RecyclerView menuList;
    DatabaseReference menuRef;
    String key = "";
    TextView textField;
    int[] image = {R.drawable.cat_cakes_com, R.drawable.cat_drink_com,
            R.drawable.cat_icecream_com, R.drawable.cat_pizza, R.drawable.cat_veges_com,
    };
    String[] name = {"Cakes", "Drinks", "IceCream", "Pizza", "Vegetables"};


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

    public class OfflineAdapter extends RecyclerView.Adapter<OfflineHolder> {

        @Override
        public OfflineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_menu_category, parent, false);
            return new OfflineHolder(v);
        }

        @Override
        public void onBindViewHolder(OfflineHolder holder, int position) {
            holder.catImage.setImageResource(image[position]);
            holder.catName.setText(name[position]);
        }

        @Override
        public int getItemCount() {
            return image.length;
        }
    }

    public class OfflineHolder extends RecyclerView.ViewHolder {

        ImageView catImage;
        TextView catName;

        public OfflineHolder(View itemView) {
            super(itemView);
            catImage = itemView.findViewById(R.id.categoryImage);
            catName = itemView.findViewById(R.id.categoryName);
        }

    }
}
