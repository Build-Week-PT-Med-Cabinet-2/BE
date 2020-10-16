package medcabinet.demo.services;

import medcabinet.demo.models.User;

public interface UserService {

    User findUserById(long id);

    User findUserByName(String name);

    User save(User user);

    void deleteById(long id);

    User update(User user, long id);

    void addUserRole(long userid, long roleid);

    void deleteUserRole(long userid, long roleid);
}
