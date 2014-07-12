package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class TimelineFragment extends Fragment {
    Context thisContext;
    ListView postListView;
    private Uri imageUri;
    private ContentValues values;
    private TextView emptyTextview;
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    PostAdapter adapter;
    private List<Post> postsList = null;
    // Set the limit of objects to show
    private int limit = 25;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(getActivity(), "B5H6u8fl4PXkvPpNKwxkXyoBHX5dXk9IxfXAwMwM", "nHJX7KClG8ql3CIuG3qDmBdIb0oOFiAuBsI2Wviy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new RemoteDataTask().execute();
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Loading Posts...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
            mProgressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            postsList = new ArrayList<Post>();
            try {
                // Get posts by current user
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Post");
                query.orderByDescending("updatedAt");
               // query.include("User");
                // Set the limit of objects to show
                // query.setLimit(limit);
                ob = query.find();
                for (ParseObject posts : ob) {
                    final Post post = new Post();
                    ParseObject postImage = posts.getParseObject("postImage");
                    final ParseUser postUser = posts.getParseUser("User");
                    postUser.fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            ParseObject profilePic = parseObject.getParseObject("profilePic");
                            profilePic.fetchInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    String profilePicUrl = parseObject.getParseFile("imageFile").getUrl();
                                    post.setProfilePicUrl(profilePicUrl);
                                }
                            });
                            post.setUsername(parseObject.getString("username"));
                            post.setName(parseObject.getString("name"));
                        }
                    });

                    Date posteddate = posts.getCreatedAt();
                    Date currentdate = new Date();
                    String timeSince = getDifference(currentdate, posteddate);
                    if(postImage!=null) {
                        post.setHasPostImage(true);

                        postImage.fetchInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                String postImageUrl = parseObject.getParseFile("imageFile").getUrl();
                                post.setPostImageUrl(postImageUrl);
                            }
                        });
                    }
                    else {
                        post.setPostImageUrl("");
                        post.setHasPostImage(false);
                    }
                    //Setting the post object values.
                    post.setCommentsCount(posts.getInt("commentCount"));
                    post.setFavoritesCount(posts.getInt("favoriteCount"));
                    post.setHasPostImage(postImage != null ? true : false);
                    post.setPostText(posts.getString("text"));
                    post.setLocation(posts.getString("location"));


                    post.setTime(timeSince);
                    postsList.add(post);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the ListView in listview.xml
            if(getActivity()!=null) {
                listview = (ListView) getActivity().findViewById(R.id.timeline_posts);
                // Pass the results into ListViewAdapter.java
                adapter = new PostAdapter(getActivity(), postsList);
                // Binds the Adapter to the ListView
                listview.setAdapter(adapter);
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
            // Create an OnScrollListener
            /*listview.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {
                    int threshold = 1;
                    int count = listview.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (listview.getLastVisiblePosition() >= count
                                - threshold) {
                            // Execute LoadMoreDataTask AsyncTask
                            new LoadMoreDataTask().execute();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }

            });*/
        }
         public String getDifference(Date newerDate, Date olderDate) {

            int diffInDays = (int) ((newerDate.getTime() - olderDate.getTime())
                    / (1000 * 60 * 60 * 24));
            if (diffInDays > 0 && diffInDays < 7) {
                return diffInDays + " d";
            } else if (diffInDays >= 7) {
                return diffInDays / 7 + "w";
            } else if (diffInDays == 0) {
                int diffinHours = (int) ((newerDate.getTime() - olderDate.getTime())
                        / (1000 * 60 * 60));
                if (diffinHours > 0)
                    return diffinHours + "h";
                else {
                    int diffinMins = (int) ((newerDate.getTime() - olderDate.getTime())
                            / (1000 * 60));
                    if (diffinMins == 0) {
                        int diffinSecs = (int) ((newerDate.getTime() - olderDate.getTime())
                                / (1000));
                        return diffinSecs + "s";
                    } else
                        return diffinMins + "m";
                }
            }
            return "" + diffInDays;
        }

    }

}
