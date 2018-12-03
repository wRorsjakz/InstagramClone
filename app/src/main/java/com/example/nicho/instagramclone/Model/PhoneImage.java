package com.example.nicho.instagramclone.Model;

public class PhoneImage
    {
        private String imageUrl;

        public PhoneImage()
            {
            }

        public PhoneImage(String imageUrl)
            {
                this.imageUrl = imageUrl;
            }

        public String getImageUrl()
            {
                return imageUrl;
            }

        public void setImageUrl(String imageUrl)
            {
                this.imageUrl = imageUrl;
            }
    }
