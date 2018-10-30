package com.brighton.caninecentral;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.Routes;


import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>{
    private List<Routes> routes;
    private Context context;

    public RouteAdapter(Context context, List<Routes> r) {
        routes = r;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view, parent, false);
        return new ViewHolder(v);
        }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Routes route = routes.get(position);
        int time = route.time;
        holder.header.setText(route.date);
        holder.routeDistance.setText(String.format("%02.2f KM", route.distance));

        if(time <60) {
            holder.routeTime.setText(Integer.toString(time) + " Mins");
        } else {
            int hours = time / 60;
            int minutes = time % 60;

            holder.routeTime.setText(String.format("%dh:%dm" , hours, minutes));

        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapPopupActivity.class);
                intent.putExtra("route_id", Integer.toString(route.id) );
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return routes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout parentLayout;
        public TextView header;
        public TextView routeDistance;
        public TextView routeTime;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            routeDistance = itemView.findViewById(R.id.routeDistance);
            routeTime = itemView.findViewById(R.id.routeTime);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }
}
