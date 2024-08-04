package at.saekenz.musiciansearch.ui.favourites;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.saekenz.musiciansearch.R;
import at.saekenz.musiciansearch.ui.search.MainActivity;
import at.saekenz.musiciansearch.util.Database;

public class FavouriteListActivity extends AppCompatActivity {

    private static final String TAG = "FavouriteListActivity";

    private Database database = Database.getInstance();
    private ArtistAdapter artistAdapter = new ArtistAdapter(this, database.getFavArtists());

    private RecyclerView recyclerView;
    private NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        /* --------- initialize bottom nav bar & send fav artists to MainActivity on button press --------- */
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.bottom_fav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bottom_search) {
                Intent flIntent = new Intent(FavouriteListActivity.this, MainActivity.class);
                startActivity(flIntent);
                return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.recyclerViewArtist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(artistAdapter);
    }

}