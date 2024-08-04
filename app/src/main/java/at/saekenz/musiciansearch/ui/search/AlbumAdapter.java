package at.saekenz.musiciansearch.ui.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import at.saekenz.musiciansearch.R;
import at.saekenz.musiciansearch.model.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private static final int MAX_TITLE_LENGTH = 27;
    private static final String TAG = "AlbumAdapter";

    private Context context;
    private List<Album> albumList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewRelease;
        private final TextView textViewType;
        private final ImageView imageViewCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewRelease = itemView.findViewById(R.id.textViewDate);
            this.textViewType = itemView.findViewById(R.id.textViewType);
            this.imageViewCover = itemView.findViewById(R.id.imageViewCover);
        }
    }

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albumList = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_rv_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /* --------- check if album title fits RecyclerView (format if not) --------- */
        String title = albumList.get(position).getTitle();
        if(title.length() > MAX_TITLE_LENGTH) {
            title = formatTitle(title);
        }
        holder.textViewTitle.setText(title);
        holder.textViewRelease.setText(albumList.get(position).getReleaseDate());
        holder.textViewType.setText(albumList.get(position).getAlbumType());
        String url = albumList.get(position).getCoverUrl();

        /* --------- load cover art into ImageView of each RecyclerView row with Picasso --------- */
        try {
            Picasso.get().load(url).resize(92,92)
            .placeholder(R.drawable.ic_baseline_sync_24).error(R.drawable.ic_baseline_image_not_supported_24).into(holder.imageViewCover);
        } catch (Exception e) {
            Log.e(TAG, "Error loading with Picasso: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    /**
     * Used to fit album titles to the screen.
     * @param title Title of the album (full length)
     * @return re-formatted title that fits the screen
     */
    private String formatTitle(String title) {
        String formattedString;
        int spaceIndex = title.indexOf(" ", 20);

        if(spaceIndex != -1) {
            formattedString = title.substring(0, spaceIndex);
        } else formattedString = title.substring(0, MAX_TITLE_LENGTH);

        return formattedString;
    }
}
