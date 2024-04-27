package com.webproject.ecommerce.services;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.repositories.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public List<User> getUsers(){
        return usersRepository.findAll();
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<User> findAllWithEagerRelationships(Pageable pageable) {
        return usersRepository.findAllWithEagerRelationship(pageable);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<User> findAll(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public User getUserById(Long id)
    {
        Optional<User> user = usersRepository.findUserById(id);
        return user.orElse(null);
    }
    public boolean userEmailExists(String email){
        return usersRepository.findUserByEmail(email).isPresent();
    }
    public boolean userIdExists(Long id){
        return usersRepository.existsById(id);
    }
    public User createUser(User user){
         return usersRepository.save(user);
    }
    public boolean userIdCheck(User user, Long id){
        if (!id.equals(user.getId())){
            return false;
        }
        else if (user.getId() == null){
            return false;
        }
        else return true;
    }
    public User updateUser(String firstName,
                           String lastName,
                           Integer age,
                           String email,
                           Long id){
       User db_user = usersRepository.findUserById(id).get();
       if(email!=null)db_user.setEmail(email);
       if(firstName!=null)db_user.setFirstName(firstName);
       if(lastName!=null)db_user.setLastName(lastName);
       if(age!=null)db_user.setAge(age);
       usersRepository.save(db_user);
       return db_user;
    }
    public void deleteUser(Long id){
        usersRepository.deleteById(id);
    }
}
