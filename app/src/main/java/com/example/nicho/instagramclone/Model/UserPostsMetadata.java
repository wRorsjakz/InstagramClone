package com.example.nicho.instagramclone.Model;

public class UserPostsMetadata
    {
       private int followers;
       private int following;
       private int posts;

        public UserPostsMetadata()
            {
            }

        public UserPostsMetadata(int followers, int following, int posts)
            {
                this.followers = followers;
                this.following = following;
                this.posts = posts;
            }

        public int getFollowers()
            {
                return followers;
            }

        public void setFollowers(int followers)
            {
                this.followers = followers;
            }

        public int getFollowing()
            {
                return following;
            }

        public void setFollowing(int following)
            {
                this.following = following;
            }

        public int getPosts()
            {
                return posts;
            }

        public void setPosts(int posts)
            {
                this.posts = posts;
            }
    }
