package com.example.android.navigationdrawerexample;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.ParseImageView;
import com.parse.ParseObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostAdapter extends BaseAdapter {

    private Context thisContext;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private List<Post> postsList = null;
    private ArrayList<Post> arraylist;
    private Post post;

    public PostAdapter(Context context, List<Post> postsList) {
        thisContext = context;
        inflater = LayoutInflater.from(thisContext);
        this.postsList = postsList;
        this.arraylist = new ArrayList<Post>();
        this.arraylist.addAll(postsList);
    }

    @Override
    public int getCount() {
        return postsList.size();
    }

    @Override
    public Post getItem(int position) {
        return postsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        post = postsList.get(position);
        imageLoader = new ImageLoader(thisContext);
        final PostHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.post_list_item, null);
            holder = new PostHolder(view);
            view.setTag(holder);
        } else {
            if(view.getTag()!= null)
                holder = (PostHolder) view.getTag();
            else {
                holder = new PostHolder(view);
                view.setTag(holder);
            }
        }

        holder.nameText.setText(post.getName());
        holder.usernameText.setText("@" + (String) post.getUsername());
        holder.postText.setText(post.getPostText());

        // Get and set like and comment counts
        holder.favoriteCount.setText(post.getFavoritesCount()+"");
        holder.commentCount.setText(post.getCommentsCount()+"");
        holder.locationText.setText(post.getLocation());
        holder.clocktime.setText(post.getTime());
        if(holder.profileImageUrl == null) {
            // Get and set profile picture
            if(!post.getProfilePicUrl().equals("")) {
               /* Ion.with(holder.profilePic)
                                .placeholder(R.drawable.profile)
                                .load(post.getProfilePicUrl());*/
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(false)
                        .delayBeforeLoading(10)
                        .cacheInMemory(false)
                        .cacheOnDisk(false)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .bitmapConfig(Bitmap.Config.ARGB_8888)
                        .build();
                MainActivity.imageLoader.displayImage(post.getProfilePicUrl(), holder.profilePic, options);
            }
             else {
                InputStream bitmap = null;

                try {
                    bitmap = thisContext.getAssets().open("profilepicbig.png");
                    Bitmap bit = BitmapFactory.decodeStream(bitmap);
                    holder.profilePic.setImageBitmap(bit);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bitmap != null)
                        try {
                            bitmap.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
        else{
            ImageView profilePicImageView = holder.profilePic;
            if (profilePicImageView != null)
                imageLoader.DisplayImage(holder.profileImageUrl, profilePicImageView);
        }
        if(!post.getPostImageUrl().trim().equals("")) {
            if(holder.postImage!=null) {
               /* Ion.with(holder.postImage)
                        .placeholder(R.drawable.postpicph)
                        .load(post.getPostImageUrl());*/
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(false)
                        .delayBeforeLoading(10)
                        .cacheInMemory(false)
                        .cacheOnDisk(false)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .bitmapConfig(Bitmap.Config.ARGB_8888)
                        .build();
                MainActivity.imageLoader.displayImage(post.getPostImageUrl(), holder.postImage, options);
            }
        }
        else{
            holder.postImage.setVisibility(View.GONE);
            holder.postImage.setImageDrawable(null);
        }

        /*holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewProfile = new Intent(thisContext, ProfileActivity.class);
                viewProfile.putExtra("username", post.getUsername());
                thisContext.startActivity(viewProfile);
            }
        });
        holder.nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfile = new Intent(thisContext, ProfileActivity.class);
                viewProfile.putExtra("username", post.getUsername());
                thisContext.startActivity(viewProfile);
            }
        });
        holder.usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfile = new Intent(thisContext, ProfileActivity.class);
                viewProfile.putExtra("username", post.getUsername());
                thisContext.startActivity(viewProfile);
            }
        });*/

        String postType = post.getType();
        if (postType != null && postType.equalsIgnoreCase("checkin")) {
            holder.locationIcon.setVisibility(View.VISIBLE);
        } else {
            holder.locationIcon.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    /*public void onCommentButtonClicked(ParseObject post) {
        Intent expandedComments = new Intent(MainActivity.mainContext, ExpandedPostActivity.class);
        expandedComments.putExtra("postId", post.getObjectId());
        thisContext.startActivity(expandedComments);

    }*/

     class PostHolder {
        ImageView profilePic;
        TextView nameText;
        TextView usernameText;
        TextView clocktime;
        ImageView locationIcon;
        TextView locationText;
        TextView postText;
        ImageView postImage;
        ImageButton favoriteButton;
        TextView favoriteCount;
        ImageButton commentButton;
        TextView commentCount;
        String profileImageUrl;

        public PostHolder(View convertView){
            // UI references
            nameText= (TextView) convertView.findViewById(R.id.post_nameView);
            usernameText = (TextView) convertView.findViewById(R.id.post_usernameView);
            clocktime= (TextView) convertView.findViewById(R.id.post_timeView);
            postText= (TextView) convertView.findViewById(R.id.post_postTextView);
            locationText= (TextView) convertView.findViewById(R.id.locationText);
            favoriteCount = (TextView) convertView.findViewById(R.id.favourite_count);
            commentCount = (TextView) convertView.findViewById(R.id.comment_count);
            profilePic = (ImageView) convertView.findViewById(R.id.post_profilePictureView);
            commentButton = (ImageButton) convertView.findViewById(R.id.commentButton);
            favoriteButton = (ImageButton) convertView.findViewById(R.id.favouriteButton);
            postImage = (ParseImageView) convertView.findViewById(R.id.postImageView);
            locationIcon = (ImageView) convertView.findViewById(R.id.locationIconPost);
        }

    }
}
