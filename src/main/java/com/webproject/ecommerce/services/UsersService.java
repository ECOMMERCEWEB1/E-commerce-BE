package com.webproject.ecommerce.services;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public List<User> getUsers(){
        return usersRepository.findAll();
    }
    public boolean userEmailExists(String email){
        return usersRepository.findUserByEmail(email).isPresent();
    }
    public boolean userIdExists(Long id){
        return !usersRepository.existsById(id);
    }
    public void createUser(User user){
        usersRepository.save(user);
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
       if (email!=null)db_user.setEmail(email);
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
