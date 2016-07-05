package at.fhhgb.catwalker;

import android.*;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import at.fhhgb.catwalker.activity.TimelineActivity;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.ServiceLocator;
import pl.droidsonroids.gif.GifTextView;

/**
 * Created by Eva on 30.06.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public List<Post> posts;
    Context context;
    Typeface custom_font;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
        custom_font = Typeface.createFromAsset(context.getApplicationContext().getAssets(),  "fonts/Champagne_Limousines-Thick.ttf");
    }

    public Post findPostById(String key){
        for ( Post post : posts ) {
            if(post.getId() == key)
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
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postAuthor.setText(posts.get(position).getUser());
        holder.postTime.setText(posts.get(position).getDateTime());
        holder.showTitle( posts.get(position).getTitle());
        holder.showContent( posts.get(position).getContent());
        holder.postPosition = new LatLng(posts.get(position).getLatitude(), posts.get(position).getLongitude());
        holder.key = posts.get(position).getId();
        holder.hasImage = posts.get(position).getHasImage();

        //expand the post if needed
        holder.isExpandable = holder.isExpandable();
        holder.expand(posts.get(position).isExpanded());
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PropertyChangeListener {

        public void showTitle(String title) {
            if(title.isEmpty())
                postTitle.setVisibility(View.GONE);
            else{
                postTitle.setText(title);
                postTitle.setVisibility(View.VISIBLE);
                postTitle.setTypeface(custom_font);
            }
        }

        public void showContent(String content) {
            if(content.isEmpty())
                postDescription.setVisibility(View.GONE);
            else{
                postDescription.setText(content);
                postDescription.setVisibility(View.VISIBLE);
            }
        }

        String key;
        Boolean hasImage;
        CardView cardView;
        TextView postAuthor;
        TextView postTime;
        TextView postTitle;
        TextView postDescription;
        ImageView postImage;
        ProgressBar postProgress;
        GifTextView imagePlaceholder;
        //ImageView imagePlaceholder;
        MapView postMap;
        LatLng postPosition;
        GoogleMap map;
        public GoogleApiClient client;

        public boolean isExpandable;
        ImageView postExpand;

        public PostViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDescription = (TextView) itemView.findViewById(R.id.post_description);
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            //postProgress = (ProgressBar) itemView.findViewById(R.id.post_progressbar);
            postMap = (MapView) itemView.findViewById(R.id.post_location);
            imagePlaceholder = (GifTextView) itemView.findViewById(R.id.post_image_placeholder);
            //imagePlaceholder = (ImageView) itemView.findViewById(R.id.post_image_placeholder);
            key = "";
            hasImage = false;

            postExpand = (ImageView) itemView.findViewById(R.id.post_expand);
            itemView.setOnClickListener(this);

            ServiceLocator.getDataModel().getLocalData().addPropertyChangeListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Post post = findPostById(key);

            if(isExpandable) {
                if (!post.isExpanded()) {
                    expand(true);
                } else {
                    expand(false);
                }
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            client.connect();

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(postPosition, 16));
                map.addMarker(new MarkerOptions().position(postPosition));
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        private void showImage(Bitmap img){
            imagePlaceholder.setVisibility(View.GONE);
            //postProgress.setVisibility(View.GONE);
            postImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            postImage.setImageBitmap(img);
            postImage.setVisibility(View.VISIBLE);
        }

        private void showMap(){
            //Log.d("Position","Post:" + postTitle.getText() + " Lat = "+postPosition.latitude +", Long = " + postPosition.longitude);
            if(postPosition.latitude != 0 && postPosition.longitude!=0) {
                Log.d("Position","Post:" + postTitle.getText() + " Lat = "+postPosition.latitude +", Long = " + postPosition.longitude);

                if (client == null) {
                    client = new GoogleApiClient.Builder(itemView.getContext())
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
                }

                postMap.onCreate(Bundle.EMPTY);
                postMap.getMapAsync(this);

                postMap.setVisibility(View.VISIBLE);
            }
        }

        public boolean isExpandable() {
            Post post = findPostById(key);
            Double lat = post.getLatitude();
            Double lon = post.getLongitude();

            if(!post.getHasImage() && lat == 0 && lon == 0) {
                postExpand.setVisibility(View.GONE);
                return false;
            }

            else {
                postExpand.setVisibility(View.VISIBLE);
                return true;
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            //show the image if the oldValue(key) equals the key of the viewholder and an image was loaded
            if (event.getPropertyName().equals("image.load") && key.equals((String) event.getOldValue())) {
                showImage((Bitmap) event.getNewValue());
            }
        }
        public void expand(boolean expand) {
            Post post = findPostById(key);
            if (expand) {
                LocalData data = ServiceLocator.getDataModel().getLocalData();
                if(hasImage) {
                    Bitmap img = data.getImage(key);
                    if (img == null){
                        ServiceLocator.getDataModel().loadImage(key);
                        imagePlaceholder.setVisibility(View.VISIBLE);
                    }
                    else {
                        showImage(img);
                    }
                }

                showMap();
                postExpand.setImageResource(R.drawable.ic_cardview_collapse);
                post.setExpanded(true);
            } else {
                postMap.setVisibility(View.GONE);
                postImage.setVisibility(View.GONE);
                imagePlaceholder.setVisibility(View.GONE);
                post.setExpanded(false);
                postExpand.setImageResource(R.drawable.ic_cardview_expand);
            }
        }
    }
}
