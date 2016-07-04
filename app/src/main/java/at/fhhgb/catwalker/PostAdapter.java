package at.fhhgb.catwalker;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.Resources;
import pl.droidsonroids.gif.GifTextView;

/**
 * Created by Eva on 30.06.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public Post findPostById(String key){
        for ( Post post : posts ) {
            if(post.getId().equals(key))
                return post;
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postAuthor.setText(posts.get(position).getUser());
        holder.postTime.setText(posts.get(position).getDateTime());
        holder.postTitle.setText(posts.get(position).getTitle());
        holder.postDescription.setText(posts.get(position).getContent());
        holder.postPosition = new LatLng(posts.get(position).getLatitude(), posts.get(position).getLongitude());
        holder.key = posts.get(position).getId();
        holder.hasImage = posts.get(position).getHasImage();
        //expand the post if needed
        holder.expand(posts.get(position).isExpanded());
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PropertyChangeListener {
        String key;
        Boolean hasImage;
        CardView cardView;
        TextView postAuthor;
        TextView postTime;
        TextView postTitle;
        TextView postDescription;
        ImageView postImage;
        GifTextView imagePlaceholder;
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
            imagePlaceholder = (GifTextView) itemView.findViewById(R.id.post_image_placeholder);
            key = "";
            hasImage = false;

            if (client == null) {
                client = new GoogleApiClient.Builder(itemView.getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            postMap.onCreate(Bundle.EMPTY);
            postMap.getMapAsync(this);
            itemView.setOnClickListener(this);
            Resources.getLocalData().addPropertyChangeListener(this);
        }

        @Override
        public void onClick(View itemView) {
            if (postMap.getVisibility() == View.GONE) {
               expand(true);
            } else {
               expand(false);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            client.connect();

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if (ContextCompat.checkSelfPermission(postMap.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        private void showImage(Bitmap img){
            imagePlaceholder.setVisibility(View.GONE);
            postImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            postImage.setImageBitmap(img);
            postImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            //show the image if the oldValue(key) equals the key of the viewholder and an image was loaded
            if (event.getPropertyName().equals("image.load") && key.equals(event.getOldValue())) {
                showImage((Bitmap) event.getNewValue());
            }
        }

        public void expand(boolean expand) {
            Post post = findPostById(key);
            if (expand) {
                postMap.setVisibility(View.VISIBLE);
                LocalData data = Resources.getLocalData();
                if(hasImage) {
                    Bitmap img = data.getImage(key);
                    if (img == null){
                        Resources.getDataModel().loadImage(key);
                        imagePlaceholder.setVisibility(View.VISIBLE);
                    }
                    else
                        showImage(img);
                }
                post.setExpanded(true);
            } else {
                postMap.setVisibility(View.GONE);
                postImage.setVisibility(View.GONE);
                imagePlaceholder.setVisibility(View.GONE);
                post.setExpanded(false);
            }
        }
    }
}
