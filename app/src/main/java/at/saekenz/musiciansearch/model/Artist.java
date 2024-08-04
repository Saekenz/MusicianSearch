package at.saekenz.musiciansearch.model;

import java.io.Serializable;
import java.util.Objects;

public class Artist implements Serializable {

    private String id;
    private String name;
    private String country;

    public Artist(String id, String name, String country){
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Artist() {
        this.id = "dummy-ID";
        this.name = "dummy-Name";
        this.country = "dummy-Country";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) && Objects.equals(name, artist.name) && Objects.equals(country, artist.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country);
    }
}
