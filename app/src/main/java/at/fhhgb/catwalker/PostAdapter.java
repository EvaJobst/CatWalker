package at.fhhgb.catwalker;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.fhhgb.catwalker.activity.TimelineActivity;
import at.fhhgb.catwalker.data.Post;

/**
 * Created by Eva on 30.06.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public List<Post> posts;

    public PostAdapter(List<Post> posts){
        this.posts = posts;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postAuthor.setText(posts.get(position).getUser());
        holder.postTime.setText(posts.get(position).getDateTime());
        holder.postTitle.setText(posts.get(position).getTitle());
        holder.postDescription.setText(posts.get(position).getContent());
        //holder.postPosition = new LatLng(Double.valueOf(posts.get(position).getLatitude()), Double.valueOf(posts.get(position).getLongitude()));
        holder.postPosition = new LatLng(0, 0); // Dummy value
    }

    @Override
    public int getItemCount() {
        if(posts==null)
            return 0;
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        CardView cardView;
        TextView postAuthor;
        TextView postTime;
        TextView postTitle;
        TextView postDescription;
        ImageView postImage;
        MapView postMap;
        LatLng postPosition;
        GoogleMap map;
        public GoogleApiClient client;

        public PostViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDescription = (TextView) itemView.findViewById(R.id.post_description);
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            postMap = (MapView) itemView.findViewById(R.id.post_location);

            if(client == null) {
                client = new GoogleApiClient.Builder(itemView.getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            postMap.onCreate(Bundle.EMPTY);
            postMap.getMapAsync(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            if(postMap.getVisibility() == View.GONE) {
                postMap.setVisibility(View.VISIBLE);
                postImage.setVisibility(View.VISIBLE);
            }

            else {
                postMap.setVisibility(View.GONE);
                postImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            client.connect();

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if(ContextCompat.checkSelfPermission(postMap.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(postPosition, 16));
                map.addMarker(new MarkerOptions().position(postPosition));
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }
}