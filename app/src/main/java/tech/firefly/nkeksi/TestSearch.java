package tech.firefly.nkeksi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by user on 10-Aug-17.
 */

public class TestSearch extends AppCompatActivity {
    DatabaseReference queryLocation;
    DatabaseReference writeLocation;
    Query query;
    FirebaseDatabase firebaseDatabase;
    MaterialSearchView materialSearchView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        firebaseDatabase = FirebaseDatabase.getInstance();

        queryLocation = firebaseDatabase.getReference().child("restaurant");

        recyclerView = (RecyclerView) findViewById(R.id.search_result_list);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_bar_test);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                    return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    query = queryLocation.orderByChild("location").startAt(newText);
                    FirebaseRecyclerAdapter<Loc, LocationHolder> fAdapter = new FirebaseRecyclerAdapter<Loc, LocationHolder>
                            (Loc.class, android.R.layout.simple_list_item_1, LocationHolder.class, query) {
                        @Override
                        protected void populateViewHolder(LocationHolder viewHolder, Loc model, int position) {
                            viewHolder.textView.setText(model.getLocation());
                        }
                    };
                    recyclerView.setLayoutManager(new LinearLayoutManager(TestSearch.this));
                    recyclerView.setAdapter(fAdapter);
                    return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public static class LocationHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public LocationHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(android.R.id.text1);
        }
    }
}
