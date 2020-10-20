package medcabinet.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
public class User
        extends Auditable{

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long userid;


        @NotNull
        @Column(nullable = false,
                unique = true)
        private String username;

        @NotNull
        @Column(nullable = false)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
        @JsonIgnoreProperties(value = "user", allowSetters = true)
        private Set<UserRoles> roles = new HashSet<>();


        @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
        @JsonIgnoreProperties(value="user", allowSetters = true)
        private List<UserRoles> uRoles = new ArrayList<>();

        public User() {
         }

         public User(long userid, String username, String password, Set<UserRoles> roles, List<UserRoles> uRoles) {
            this.userid = userid;
            this.username = username;
            this.password = password;
            this.roles = roles;
            for (UserRoles ur : uRoles){
                ur.setUser(this);
            }
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

        public Set<UserRoles> getRoles() {
            return roles;
        }

        public void setRoles(Set<UserRoles> roles) {
            this.roles = roles;
        }


        public List<UserRoles> getuRoles() {
            return uRoles;
        }

        public void addRole(Role role) {
            roles.add(new UserRoles(this, role));
        }


    public void setuRoles(List<UserRoles> uRoles) {
        this.uRoles = uRoles;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority()
    {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.roles)
        {
            String myRole = "ROLE_" + r.getRole()
                    .getName()
                    .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }


}
