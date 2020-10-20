package medcabinet.demo.services;


import medcabinet.demo.exceptions.ResourceFoundException;
import medcabinet.demo.exceptions.ResourceNotFoundException;
import medcabinet.demo.models.Role;
import medcabinet.demo.repository.RoleRepository;
import medcabinet.demo.repository.StrainRepository;
import medcabinet.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAuditing userAuditing;


    @Override
    public Role findRoleById(long id) {
        return roleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Role not found"));
    }

    @Override
    public Role findByRoleName(String rolename) {
        Role role = roleRepository.findRoleByName(rolename);

        if(role != null){
            return role;
        } else {
            throw new ResourceNotFoundException("Role not found");
        }
    }

    @Transactional
    @Override
    public Role save(Role role) {
        if(role.getUsers().size() > 0){
            throw new ResourceFoundException("User roles are not updated");
        }

        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        roleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Role not found"));
        roleRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Role update(Role role, long id) {
        if(role.getName() == null) {
            throw new ResourceNotFoundException("No role name to update.");
        }

        if(role.getUsers().size()>0){
            throw new ResourceFoundException("User roles are not updated.");
        }

        Role newrole = findRoleById(id);

        roleRepository.updateRoleName(userAuditing.getCurrentAuditor().get(), id, role.getName());
        return findRoleById(id);
    }
}
