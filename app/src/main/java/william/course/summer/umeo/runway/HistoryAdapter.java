package william.course.summer.umeo.runway;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Manages the RecyclerView Adapter for presenting history elements
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    public HistoryAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    /**
     * Construts a new ViewHolder element
     * @param parent parent element
     * @param viewType viewtype element
     * @return returns a new ViewHolder
     */
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * used in order to bind sertain viewelements for each element in the holder
     * @param holder the holder to be bound to
     * @param position the position in the holder
     */
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.head.setText(listItem.getDate());
        holder.time.setText(listItem.getTime());
        holder.elevation.setText(listItem.getAltitude());
        holder.kmphour.setText(listItem.getSpeed());
        holder.distance.setText(listItem.distance());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    /**
     * Inner Class used in order to construct each subelement
     * and make the correct bindings
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView head;
        public TextView time;
        public TextView elevation;
        public TextView kmphour;
        public TextView distance;


        public ViewHolder(View itemView) {
            super(itemView);

            head = (TextView) itemView.findViewById(R.id.textViewHead);
            time = (TextView) itemView.findViewById(R.id.time);
            elevation = (TextView) itemView.findViewById(R.id.elevation);
            distance = (TextView) itemView.findViewById(R.id.distance);
            kmphour = (TextView) itemView.findViewById(R.id.kilometer_per_hour);
        }
    }
}
