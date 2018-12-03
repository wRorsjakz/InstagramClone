package com.example.nicho.instagramclone.Model;

public class UserAccountSettings
    {
        private String email_address;
        private String location;
        private String phone_number;
        private String profile_name;
        private String profile_photo;
        private String status;
        private String user_id;
        private String username;
        private String website;

        public UserAccountSettings(String email_address, String location, String phone_number, String profile_name, String profile_photo, String status, String user_id, String username, String website)
            {
                this.email_address = email_address;
                this.location = location;
                this.phone_number = phone_number;
                this.profile_name = profile_name;
                this.profile_photo = profile_photo;
                this.status = status;
                this.user_id = user_id;
                this.username = username;
                this.website = website;
            }

        public UserAccountSettings()
            {
            }

        public String getEmail_address()
            {
                return email_address;
            }

        public void setEmail_address(String email_address)
            {
                this.email_address = email_address;
            }

        public String getLocation()
            {
                return location;
            }

        public void setLocation(String location)
            {
                this.location = location;
            }

        public String getPhone_number()
            {
                return phone_number;
            }

        public void setPhone_number(String phone_number)
            {
                this.phone_number = phone_number;
            }

        public String getProfile_name()
            {
                return profile_name;
            }

        public void setProfile_name(String profile_name)
            {
                this.profile_name = profile_name;
            }

        public String getProfile_photo()
            {
                return profile_photo;
            }

        public void setProfile_photo(String profile_photo)
            {
                this.profile_photo = profile_photo;
            }

        public String getStatus()
            {
                return status;
            }

        public void setStatus(String status)
            {
                this.status = status;
            }

        public String getUser_id()
            {
                return user_id;
            }

        public void setUser_id(String user_id)
            {
                this.user_id = user_id;
            }

        public String getUsername()
            {
                return username;
            }

        public void setUsername(String username)
            {
                this.username = username;
            }

        public String getWebsite()
            {
                return website;
            }

        public void setWebsite(String website)
            {
                this.website = website;
            }

        @Override
        public String toString()
            {
                return "UserAccountSettings{" +
                        "email_address='" + email_address + '\'' +
                        ", location='" + location + '\'' +
                        ", phone_number='" + phone_number + '\'' +
                        ", profile_name='" + profile_name + '\'' +
                        ", profile_photo='" + profile_photo + '\'' +
                        ", status='" + status + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", username='" + username + '\'' +
                        ", website='" + website + '\'' +
                        '}';
            }
    }
