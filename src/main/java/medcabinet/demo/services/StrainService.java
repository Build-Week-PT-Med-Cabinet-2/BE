package medcabinet.demo.services;

import medcabinet.demo.models.Strain;
import medcabinet.demo.models.User;

import java.util.List;

public interface StrainService {

    List<Strain> findAllStrainsByUser(long id);

    Strain findStrainById(long strainid);

    Strain findStrainByName(String name);

    Strain save(User user, Strain strain);

    void deleteById(long strainid);

    void deleteByName(String name);

    List<Strain> findAllStrainsByAuth();

}
