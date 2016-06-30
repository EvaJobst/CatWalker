package at.fhhgb.catwalker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.fhhgb.catwalker.data.Post;

/**
 * Created by Eva on 30.06.2016.
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.PostViewHolder> {
    public List<Post> posts;

    public EntryAdapter(List<Post> posts){
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
        //TODO: add getUsername: holder.postAuthor.setText(posts.get(position).getUser());
        holder.postTime.setText(posts.get(position).getDateTime());
        holder.postTitle.setText(posts.get(position).getTitle());
        holder.postDescription.setText(posts.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView postAuthor;
        TextView postTime;
        TextView postTitle;
        TextView postDescription;

        public PostViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDescription = (TextView) itemView.findViewById(R.id.post_description);
        }
    }
}
