package com.kitesoft.tpkakaosearchapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceListRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    List<Place> places;

    public PlaceListRecyclerAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item_list_fragment, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;

        Place place= places.get(position);

        vh.tvPlaceName.setText( place.place_name );
        if(!place.road_address_name.equals("")) vh.tvAdderss.setText( place.road_address_name );
        else vh.tvAdderss.setText(place.address_name);
        vh.tvDistance.setText( places.get(position).distance +"m" );

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvPlaceName;
        TextView tvAdderss;
        TextView tvDistance;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvPlaceName= itemView.findViewById(R.id.tv_place_name);
            tvAdderss= itemView.findViewById(R.id.tv_address);
            tvDistance= itemView.findViewById(R.id.tv_distance);
        }
    }
}
