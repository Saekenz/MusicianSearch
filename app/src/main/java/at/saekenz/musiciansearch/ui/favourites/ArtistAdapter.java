package at.saekenz.musiciansearch.ui.favourites;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsramraj.flags.Flags;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Locale;

import at.saekenz.musiciansearch.R;
import at.saekenz.musiciansearch.model.Artist;
import at.saekenz.musiciansearch.util.Database;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<Artist> artistList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Database database = Database.getInstance();
        private final TextView textViewName;
        private final ImageView imageCountry;
        private final TextView textViewCountry;
        private final ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.imageCountry = itemView.findViewById(R.id.imageViewCountry);
            this.textViewCountry = itemView.findViewById(R.id.textViewCountry);
            this.buttonDelete = itemView.findViewById(R.id.buttonDel);
            buttonDelete.setOnClickListener(view -> { database.removeFavArtist(getAdapterPosition());});
        }
    }

   public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artistList = artists;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_rv_row, parent, false);
        Flags.init(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewName.setText(artistList.get(position).getName());
        Locale loc = new Locale("", artistList.get(position).getCountry());
        String country = loc.getDisplayCountry(Locale.ENGLISH);
        holder.textViewCountry.setText(country);
        // set flag default to US
        BitmapDrawable flag = Flags.forCountry("US");
        try {
            flag = Flags.forCountry(artistList.get(position).getCountry());
        }
        catch (InvalidParameterException ipe) {

        }
        holder.imageCountry.setImageBitmap(flag.getBitmap());
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }
}
