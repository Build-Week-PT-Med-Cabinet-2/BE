package medcabinet.demo.services;

import medcabinet.demo.models.Role;

public interface RoleService {

    Role findRoleById(long id);

    Role findByRoleName(String rolename);

    Role save(Role role);

    void deleteById(long id);

    Role update(Role role, long id);
}
