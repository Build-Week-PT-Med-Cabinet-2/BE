package medcabinet.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "strains")
public class Strain extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long strainid;


    private String name;
    private String type;
    private String description;


    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "strains", allowSetters = true)
    private User user;

    public Strain() {
    }

    public long getStrainid() {
        return strainid;
    }

    public void setStrainid(long strainid) {
        this.strainid = strainid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
