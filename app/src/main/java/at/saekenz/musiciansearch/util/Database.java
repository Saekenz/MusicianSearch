package at.saekenz.musiciansearch.util;

import java.util.ArrayList;

import at.saekenz.musiciansearch.model.Artist;

public class Database {

    private static Database singleInstance = null;

    private static final ArrayList<Artist> favArtists = new ArrayList<>();

    public static synchronized Database getInstance() {
        if(singleInstance == null)
            singleInstance = new Database();
        return singleInstance;
    }

    public ArrayList<Artist> getFavArtists() {
        return favArtists;
    }

    public void clearFavArtists() {
        favArtists.clear();
    }

    public boolean addFavArtist(Artist artist) {
        return favArtists.add(artist);
    }

    public boolean removeFavArtist(Artist artist) {
        return favArtists.remove(artist);
    }

    public boolean removeFavArtist(String artistName) {
        return favArtists.removeIf(artist -> artist.getName().equals(artistName));
    }
    public void removeFavArtist(int index) {
        favArtists.remove(index);
    }
}
