package medcabinet.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User
        extends Auditable{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long userid;


        @Column(nullable = false,
                unique = true)
        private String username;


        @Column(nullable = false)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        @Column(nullable = false)
        private List sickness = new ArrayList();

        @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
        @JsonIgnoreProperties(value = "user", allowSetters = true)
        private Set<UserRoles> roles = new HashSet<>();

        public User() {
         }

        public User(String username, String password, List sickness) {
            this.username = username;
            this.password = password;
            this.sickness = sickness;
        }

        public long getUserid() {
            return userid;
        }

        public void setUserid(long userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List getSickness() {
            return sickness;
        }

        public void setSickness(List sickness) {
            this.sickness = sickness;
        }

        public Set<UserRoles> getRoles() {
            return roles;
        }

        public void setRoles(Set<UserRoles> roles) {
            this.roles = roles;
        }

//    @JsonIgnore
//    public List<SimpleGrantedAuthority> getAuthority()
//    {
//        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();
//
//        for (UserRoles r : this.roles)
//        {
//            String myRole = "ROLE_" + r.getRole()
//                    .getName()
//                    .toUpperCase();
//            rtnList.add(new SimpleGrantedAuthority(myRole));
//        }
//
//        return rtnList;
//    }


}
