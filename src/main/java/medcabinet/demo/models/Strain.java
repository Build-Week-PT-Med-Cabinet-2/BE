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
    private String sickness;
    private String effects;


    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "strains", allowSetters = true)
    private User user;

    public Strain() {
    }

    public Strain(long strainid, String name, String type, String sickness, String effects, User user) {
        this.strainid = strainid;
        this.name = name;
        this.type = type;
        this.sickness = sickness;
        this.effects = effects;
        this.user = user;
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

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public String getEffects() {
        return effects;
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
