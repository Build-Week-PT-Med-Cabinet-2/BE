package medcabinet.demo.repository;

import medcabinet.demo.models.Strain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrainRepository extends CrudRepository<Strain, Long> {

    List<Strain> findPlantsByUser(long id);

    Strain findStrainByName(String name);


}
