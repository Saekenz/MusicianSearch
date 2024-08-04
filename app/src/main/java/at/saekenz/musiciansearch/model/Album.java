package at.saekenz.musiciansearch.model;

public class Album {

    private String id;
    private String title;
    private String releaseDate;
    private String albumType;
    private String coverUrl;

    public Album(String id, String title, String releaseDate, String albumType, String coverUrl) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.albumType = albumType;
        this.coverUrl = coverUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getAlbumType() {
        return albumType;
    }

    public String getCoverUrl() { return coverUrl; }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", albumType='" + albumType + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
