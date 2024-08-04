package at.saekenz.musiciansearch.ui.search;

import static at.saekenz.musiciansearch.util.ApiKeys.USER_AGENT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.saekenz.musiciansearch.ui.favourites.FavouriteListActivity;
import at.saekenz.musiciansearch.R;
import at.saekenz.musiciansearch.model.Album;
import at.saekenz.musiciansearch.model.Artist;
import at.saekenz.musiciansearch.util.Database;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static final String ARTIST_SEARCH_URL = "https://musicbrainz.org/ws/2/artist/?query=";
    private static final String ALBUMS_SEARCH_URL = "https://musicbrainz.org/ws/2/artist/";
    private static final String COVER_SEARCH_URL = "https://coverartarchive.org/release-group/";

    private List<Album> albumList = new ArrayList<>();
    private AlbumAdapter albumAdapter = new AlbumAdapter(this, albumList);
    private Artist artist;

    private SearchView searchView;
    private RecyclerView recyclerView;
    private CheckBox favCheckbox;
    private RequestQueue requestQueue;
    private BottomNavigationView bottomNavigationView;

    private Database database = Database.getInstance();
    private List<Artist> favArtists = database.getFavArtists();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* --------- initialize bottom nav bar & send fav artists to FavouriteListActivity on button press --------- */
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bottom_fav) {
                Intent intent = new Intent(MainActivity.this, FavouriteListActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        favCheckbox = findViewById(R.id.favorite_chkbox);
        favCheckbox.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                artist = new Artist();
                albumList.clear();
                String artistSearchUrl = ARTIST_SEARCH_URL + query + "&fmt=json";
                JsonObjectRequest artistRequest = new JsonObjectRequest(Request.Method.GET, artistSearchUrl, null,
                        artistQueryResponse -> {
                                handleArtistRequest(artistQueryResponse);
                                /* --------- remember if artist was previously added to favourites --------- */
                                if(favArtists.contains(artist)) favCheckbox.toggle();
                                String albumsSearchUrl = ALBUMS_SEARCH_URL + artist.getId() + "?inc=release-groups&fmt=json";
                                @SuppressLint("NotifyDataSetChanged") JsonObjectRequest albumsRequest = new JsonObjectRequest(Request.Method.GET, albumsSearchUrl, null,
                                        albumsQueryResponse -> {
                                            handleAlbumsRequest(albumsQueryResponse);
                                            /* --------- tell adapter that List contents have changed --------- */
                                            albumAdapter.notifyDataSetChanged();
                                        }, error -> {
                                            Log.e(TAG, "Album Query Error: " + error.getMessage());
                                        }) {
                                    /* --------- manually set User-Agent String for Albums Request --------- */
                                    @Override
                                    public Map<String, String> getHeaders(){
                                        Map<String, String> headers = new HashMap<>();
                                        headers.put("User-agent", USER_AGENT);
                                        return headers;
                                    }
                                };
                                requestQueue.add(albumsRequest);
                        }, error -> {
                            Log.e(TAG, "Artist Query Error: " + error.getMessage());
                        }) {
                    /* --------- manually set User-Agent String for Albums Request --------- */
                    @Override
                    public Map<String, String> getHeaders(){
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-agent", USER_AGENT);
                        return headers;
                    }
                };
                requestQueue.add(artistRequest);
                /* --------- reset fav status for new artist search --------- */
                if(favCheckbox.isChecked()) favCheckbox.toggle();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * Extracts information from artist query response
     * @param artistQueryResponse JSON object that is received after querying artist name
     */
    private void handleArtistRequest(JSONObject artistQueryResponse ) {
        try {
            JSONArray jsonArtistsArray = artistQueryResponse.getJSONArray("artists");
            JSONObject artistObject = jsonArtistsArray.getJSONObject(0);

            String id = artistObject.getString("id");
            String name = artistObject.getString("name");

            /* --------- check if artist has optional country attribute --------- */
            String country = "n/a";
            if(artistObject.has("country")) {
                country = artistObject.getString("country");
            }

            artist = new Artist(id, name, country);
            Log.d(TAG, "Parsed artist: " + artist);

        } catch (JSONException jsonException) {
            Log.e(TAG, "Error while requesting artist: " + jsonException.getMessage());
        }
    }

    /**
     * extracts information from albums query response
     * @param albumsQueryResponse JSON object that is received after querying albums with artist id
     */
    private void handleAlbumsRequest(JSONObject albumsQueryResponse) {
        try {
            JSONArray jsonAlbumsArray = albumsQueryResponse.getJSONArray("release-groups");
            for (int i = 0; i < jsonAlbumsArray.length(); i++) {
                JSONObject albumObject = jsonAlbumsArray.getJSONObject(i);
                String albumID = albumObject.getString("id");
                albumList.add(new Album(albumID,
                        albumObject.getString("title"),
                        albumObject.getString("first-release-date"),
                        albumObject.getString("primary-type"),
                        COVER_SEARCH_URL + albumID + "/front"));
            }
            Log.d(TAG, "Albums received: " + albumList.size());
        } catch (JSONException jsonException) {
            Log.e(TAG, "Error while requesting albums: " + jsonException.getMessage());
        }
    }

    /**
     * handles adding/removing artists from favourites list
     */
    @Override
    public void onClick(View v) {
        if(favCheckbox.isChecked()) {
            if(artist != null && !favArtists.contains(artist))
                favArtists.add(artist);
            else {
                favArtists.remove(artist);
                for(Artist a : Database.getInstance().getFavArtists()){
                    Log.e(TAG, a.toString());
                }
            }
        } else {
            favArtists.remove(artist);
        }
    }
}