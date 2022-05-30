package CRUDvalidacionDTOSmodelMapper.service;

import CRUDvalidacionDTOSmodelMapper.entity.Person;
import CRUDvalidacionDTOSmodelMapper.entity.PersonInputDTO;
import CRUDvalidacionDTOSmodelMapper.entity.PersonOutputDTO;
import CRUDvalidacionDTOSmodelMapper.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ModelMapper modelMapper;


    ///// ----------------------- Add new Person -----------------------/////
    public PersonOutputDTO addPerson(PersonInputDTO personInputDTO) throws Exception {

        if (personInputDTO.getUsername().length() < 6 || personInputDTO.getUsername().length() > 10) {

            throw new Exception("The username must be between 6 and 10 characters long");

        } else if (personInputDTO.getUsername() == null || personInputDTO.getUsername().isEmpty()) {

            throw new Exception("The username cannot be null or empty");

        } else if (personInputDTO.getPassword() == null || personInputDTO.getPassword().isEmpty()) {

            throw new Exception("The password cannot be null or empty");

        } else if (personInputDTO.getName() == null || personInputDTO.getName().isEmpty()) {

            throw new Exception("The name cannot be null or empty");

        } else if (personInputDTO.getCompany_email() == null | personInputDTO.getCompany_email().isEmpty()) {

            throw new Exception("The company email cannot be null or empty");

        } else if (personInputDTO.getPersonal_email() == null || personInputDTO.getPersonal_email().isEmpty()) {

            throw new Exception("The personal email cannot be null or empty");

        } else if (personInputDTO.getCity() == null || personInputDTO.getCity().isEmpty()) {

            throw new Exception("The city cannot be null or empty");

        } else {
            LocalDate creationDate = LocalDate.now();
            PersonOutputDTO personOutputDTO = new PersonOutputDTO();
            personInputDTO.setCreated_date(
                    new SimpleDateFormat("yyyy-mm-dd").parse(creationDate.toString()));
            Person personEntity =
                    personRepository.saveAndFlush(modelMapper.map(personInputDTO, Person.class));
            personOutputDTO = modelMapper.map(personInputDTO, PersonOutputDTO.class);
            personOutputDTO.setId_person(personEntity.getId_person());

            return personOutputDTO;
        }
    }

    ///// ----------------------------- Get All person ---------------------/////
    public List<PersonOutputDTO> listPerson() {
        List<PersonOutputDTO> personList = new ArrayList<>();
        personRepository.findAll()
                .forEach(
                        person -> {
                            PersonOutputDTO personOutputDTO = new PersonOutputDTO();
                            personOutputDTO = modelMapper.map(person, PersonOutputDTO.class);
                            personList.add(personOutputDTO);
                        });
        return personList;
    }

    ///// ---------------------------- Find Person By Id ------------------------/////
    public PersonOutputDTO personById(int id) throws Exception {
        try {
            Optional<Person> personInBD = personRepository.findById(id);
            PersonOutputDTO personOutputDTO = new PersonOutputDTO();
            personOutputDTO = modelMapper.map(personInBD, PersonOutputDTO.class);
            return personOutputDTO;
        } catch (Exception e) {
            throw new Exception("the person no does not exist");
        }
    }

    ///// ------------------------------- Find By Name ----------------------------- /////
    public List<PersonOutputDTO> personByName(String name) throws Exception {
        try {
            List<PersonOutputDTO> lisPersonOutputDTOS = new ArrayList<>();
            List<Person> personsInBD = personRepository.findByName(name);
            personsInBD
                    .forEach(person -> {
                        PersonOutputDTO personOutputDTO = modelMapper.map(person, PersonOutputDTO.class);
                        lisPersonOutputDTOS.add(personOutputDTO);
                    });
            return lisPersonOutputDTOS;

        } catch (Exception e) {
            throw new Exception("the person name no does not exist");
        }
    }

    ///// ------------------------------- Delete Person --------------------------- /////
    public String deletePersona(int id) throws Exception {
        try {
            Optional<Person> personToDelete = personRepository.findById(id);
            personRepository.deleteById(id);
            return "Has been deleted: "
                    + personToDelete.get().getName()
                    + " from: "
                    + personToDelete.get().getCity()
                    + " with id: "
                    + personToDelete.get().getId_person();

        } catch (Exception e) {
            throw new Exception("the person does not exist");
        }
    }

    ///// -------------------------------- Update Person ----------------------------- /////
    public PersonOutputDTO updatePerson(PersonInputDTO personInputDTO, int id) throws Exception {
        Optional<Person> personEntity;
        try {
            personEntity = personRepository.findById(id);
            personInputDTO.setId_person(id);
            personInputDTO.setUsername(
                    Optional.ofNullable(personInputDTO.getUsername())
                            .orElse(personEntity.get().getUsername()));
            personInputDTO.setPassword(
                    Optional.ofNullable(personInputDTO.getPassword())
                            .orElse(personEntity.get().getPassword()));
            personInputDTO.setName(
                    Optional.ofNullable(personInputDTO.getName()).orElse(personEntity.get().getName()));
            personInputDTO.setSurname(
                    Optional.ofNullable(personInputDTO.getSurname()).orElse(personEntity.get().getSurname()));
            personInputDTO.setCompany_email(
                    Optional.ofNullable(personInputDTO.getCompany_email())
                            .orElse(personEntity.get().getCompany_email()));
            personInputDTO.setPersonal_email(
                    Optional.ofNullable(personInputDTO.getPersonal_email())
                            .orElse(personEntity.get().getPersonal_email()));
            personInputDTO.setCity(
                    Optional.ofNullable(personInputDTO.getCity()).orElse(personEntity.get().getCity()));
            personInputDTO.setImage_url(
                    Optional.ofNullable(personInputDTO.getImage_url())
                            .orElse(personEntity.get().getImage_url()));
            personInputDTO.setActive(personEntity.get().getActive());
            personInputDTO.setCreated_date(personEntity.get().getCreated_date());
            personRepository.saveAndFlush(modelMapper.map(personInputDTO, Person.class));
            PersonOutputDTO personOutputDTO = modelMapper.map(personInputDTO, PersonOutputDTO.class);
            return personOutputDTO;

        } catch (Exception e) {
            throw new Exception("Something went wrong or the person does not exist");
        }
    }
}
