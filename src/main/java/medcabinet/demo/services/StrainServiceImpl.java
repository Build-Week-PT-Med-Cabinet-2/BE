package medcabinet.demo.services;


import medcabinet.demo.exceptions.ResourceNotFoundException;
import medcabinet.demo.models.Strain;
import medcabinet.demo.models.User;
import medcabinet.demo.repository.StrainRepository;
import medcabinet.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "strainService")
public class StrainServiceImpl implements StrainService {

    @Autowired
    private StrainRepository strainRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HelperFunctions helperFunctions;


    public StrainServiceImpl() {
        super();
    }

    @Override
    public List<Strain> findAllStrainsByUser(long id) {
        List<Strain> strains = new ArrayList<>();
        strainRepository.findPlantsByUser(id).iterator().forEachRemaining(strains::add);
        return strains;
    }

    @Override
    public Strain findStrainById(long strainid) {
        return strainRepository.findById(strainid).orElseThrow(() ->
                new ResourceNotFoundException("Strain with id " + strainid + " not found."));
    }

    @Override
    public Strain findStrainByName(String name) {
        return strainRepository.findStrainByName(name);
    }

    @Override
    public Strain save(User user, Strain strain) {
        Strain newStrain = new Strain();

        if(strain.getStrainid() != 0) {
            strainRepository.findById(strain.getStrainid()).orElseThrow(()
                    -> new ResourceNotFoundException("Strain with id " + strain.getStrainid() + " not found."));
            newStrain.setStrainid(strain.getStrainid());
        }

        newStrain.setName(strain.getName());
        newStrain.setType(strain.getType());
        newStrain.setSickness(strain.getSickness());
        newStrain.setEffects(strain.getEffects());
        newStrain.setUser(user);

        return strainRepository.save(newStrain);
    }

    @Transactional
    @Override
    public void deleteById(long strainid) {
        if(helperFunctions.isAuthorizedToMakeChange(findStrainById(strainid).getUser().getUsername())) {
            if(strainRepository.findById(strainid).isPresent()) {
                strainRepository.deleteById(strainid);
            }
            else {
                throw new ResourceNotFoundException("Strain id "+ strainid+ " not found");
            }
        }
    }

    @Transactional
    @Override
    public void deleteByName(String name) {
        if(helperFunctions.isAuthorizedToMakeChange((strainRepository.findStrainByName(name).getUser().getUsername()))) {
            strainRepository.deleteById(strainRepository.findStrainByName(name).getStrainid());
        }
        else {
            throw new ResourceNotFoundException("Strain with name " + name + "not found.");
        }
    }

    @Override
    public List<Strain> findAllStrainsByAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            User cUser = userRepository.findByUsername(auth.getName());
            if(cUser == null){
                throw new ResourceNotFoundException("User id" + cUser.getUserid() + "not found.");
            }
            return strainRepository.findPlantsByUser(cUser.getUserid());
        } else {
            throw new ResourceNotFoundException("User is not valid.");
        }
    }
}
