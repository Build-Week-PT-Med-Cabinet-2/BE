package medcabinet.demo.services;


import medcabinet.demo.exceptions.ResourceNotFoundException;
import medcabinet.demo.models.User;
import medcabinet.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "securityUserService")
public class SecurityUserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String string)
        throws
            ResourceNotFoundException {
        User user = userRepository.findByUsername(string.toLowerCase());
        if(user == null){
            throw new ResourceNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

}
