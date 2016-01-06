package com.example.aaron.kingdomalliance;

public class Variables {

    public class User {
        String username, password;

        public User (String username, String password){
            this.username = username; this.password = password;
        }

        public User (String username){
            this(username, "");
        }
    }
}
