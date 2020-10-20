package medcabinet.demo.controllers;


import medcabinet.demo.models.Strain;
import medcabinet.demo.models.User;
import medcabinet.demo.repository.UserRepository;
import medcabinet.demo.services.StrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class StrainController {
    @Autowired
    private StrainService strainService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value= "/api/strains/{strainid}", produces={"application/json"})
    public ResponseEntity<?> getStrainById(@PathVariable long strainid){
        Strain strain = strainService.findStrainById(strainid);
        return new ResponseEntity<>(strain, HttpStatus.OK);
    }

    @GetMapping(value = "/api/strains", produces ={"application/json"})
    public ResponseEntity<?> getAllStrains() {
        List<Strain> strainList = strainService.findAllStrainsByAuth();
        return new ResponseEntity<>(strainList, HttpStatus.OK);
    }

    @GetMapping(value = "/api/strains/name/{name}", produces = {"application/json"})
    public ResponseEntity<?> getPlantByName(@PathVariable String name){
        Strain strain = strainService.findStrainByName(name);
        return new ResponseEntity<>(strain, HttpStatus.OK);
    }

    @PostMapping(value = "/api/strain", consumes= {"applcation/json"})
    public ResponseEntity<?> addNewStrain(@Valid @RequestBody Strain strain) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        strain.setStrainid(0);
        Strain newStrain = strainService.save(user, strain);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStrainURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{strainid}")
                .buildAndExpand(newStrain.getStrainid())
                .toUri();
        responseHeaders.setLocation(newStrainURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/api/strains/{strainid}")
    public ResponseEntity<?> deleteStrainById(@PathVariable long strainid){
        strainService.deleteById(strainid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/strains/{strainname}")
    public ResponseEntity<?> deleteStrainByName(@PathVariable String name){
        strainService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
