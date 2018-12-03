package com.example.nicho.instagramclone.Model;

/**
 * This data model is for the image that the user uploads into firebase database
 */
public class ImagePost
    {
        private String caption;
        private String date_created;
        private String image_path;
        private String image_id;
        private String user_id;
        private String tags;

        public ImagePost()
            {
            }

        public ImagePost(String caption, String date_created, String image_path, String image_id, String user_id, String tags)
            {
                this.caption = caption;
                this.date_created = date_created;
                this.image_path = image_path;
                this.image_id = image_id;
                this.user_id = user_id;
                this.tags = tags;
            }

        public String getCaption()
            {
                return caption;
            }

        public void setCaption(String caption)
            {
                this.caption = caption;
            }

        public String getDate_created()
            {
                return date_created;
            }

        public void setDate_created(String date_created)
            {
                this.date_created = date_created;
            }

        public String getImage_path()
            {
                return image_path;
            }

        public void setImage_path(String image_path)
            {
                this.image_path = image_path;
            }

        public String getImage_id()
            {
                return image_id;
            }

        public void setImage_id(String image_id)
            {
                this.image_id = image_id;
            }

        public String getUser_id()
            {
                return user_id;
            }

        public void setUser_id(String user_id)
            {
                this.user_id = user_id;
            }

        public String getTags()
            {
                return tags;
            }

        public void setTags(String tags)
            {
                this.tags = tags;
            }
    }
