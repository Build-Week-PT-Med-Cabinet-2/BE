package medcabinet.demo.services;


import medcabinet.demo.exceptions.ResourceFoundException;
import medcabinet.demo.exceptions.ResourceNotFoundException;
import medcabinet.demo.models.Role;
import medcabinet.demo.models.User;
import medcabinet.demo.models.UserRoles;
import medcabinet.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserAuditing userAuditing;

    @Autowired
    private HelperFunctions helperFunctions;


    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User id "+ id +" not found"));
    }

    @Override
    public User findUserByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new ResourceNotFoundException("User name" + name + " not found.");
        }
        return user;
    }

    @Override
    public User save(User user) {
        User newuser = new User();

        if (user.getUserid() != 0) {
            if (helperFunctions.isAuthorizedToMakeChange(user.getUsername())) {
                User old = userRepository.findById(user.getUserid()).orElseThrow(() ->
                        new ResourceNotFoundException("User id" + user.getUserid() + " not found."));

                for (UserRoles ur : old.getRoles()) {
                    deleteUserRole(ur.getUser().getUserid(), ur.getRole().getRoleid());
                }
                newuser.setUserid(user.getUserid());
            } else {
                throw new ResourceNotFoundException("This user is not authorized");
            }
        } else {
            if (userRepository.findByUsername((user.getUsername())) != null) {
                throw new ResourceNotFoundException("User already exists");
            }
        }
        newuser.setUsername(user.getUsername());
        newuser.setPassword(user.getPassword());

        newuser.getRoles().clear();
        if(user.getUserid() == 0){
            for(UserRoles ur : user.getRoles()) {
                Role newRole = roleService.findRoleById(ur.getRole().getRoleid());

                newuser.addRole(newRole);
            }
        } else {
           for(UserRoles ur : user.getRoles()) {
               addUserRole(newuser.getUserid(), ur.getRole().getRoleid());
           }
        }
        return userRepository.save(newuser);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User update(User user, long id) {
        User cUser = findUserById(id);

        if(helperFunctions.isAuthorizedToMakeChange(cUser.getUsername())){
            if(user.getUsername() != null) {
                cUser.setUsername(user.getUsername());
            }

            if(user.getPassword() != null){
                cUser.setPassword(user.getPassword());
            }

            if(user.getRoles().size() > 0) {
                for(UserRoles ur : cUser.getRoles()) {
                    deleteUserRole(ur.getUser().getUserid(), ur.getRole().getRoleid());
                }

                for(UserRoles ur : user.getRoles()){
                    addUserRole(cUser.getUserid(), ur.getRole().getRoleid());
                }
            }
            return userRepository.save(cUser);
        } else {
            throw new ResourceNotFoundException("This user is not authorized");
        }
    }

    @Transactional
    @Override
    public void addUserRole(long userid, long roleid) {
        userRepository.findById(userid).orElseThrow(() ->
                new ResourceNotFoundException("User id not found"));
        roleService.findRoleById(roleid);

        if(userRepository.checkUserRolesCombo(userid, roleid).getCount() <= 0){
            userRepository.insertUserRoles(userAuditing.getCurrentAuditor().get(), userid, roleid);
        } else {
            throw new ResourceFoundException("User already has this role");
        }
    }

    @Transactional
    @Override
    public void deleteUserRole(long userid, long roleid) {
        userRepository.findById(userid).orElseThrow(() ->
                new ResourceNotFoundException("User id not found"));
        roleService.findRoleById(roleid);

        if(userRepository.checkUserRolesCombo(userid, roleid).getCount() > 0) {
            userRepository.deleteUserRoles(userid, roleid);
        } else {
            throw new ResourceNotFoundException("This user does not have this role");
        }
    }
}
