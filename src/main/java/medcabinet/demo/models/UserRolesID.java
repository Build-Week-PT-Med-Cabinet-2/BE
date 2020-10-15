package medcabinet.demo.models;

import java.io.Serializable;

public class UserRolesID implements Serializable {

    private long user;

    private long role;

    public UserRolesID() {
    }

    public UserRolesID(long user, long role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        UserRolesID that = (UserRolesID) obj;
        return user == that.user &&
                role == that.role;
    }
}
