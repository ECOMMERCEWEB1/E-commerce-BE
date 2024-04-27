package com.webproject.ecommerce.repositories;

import com.webproject.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById (Long id);

    default Page<User> findAllWithEagerRelationship(Pageable pageable){
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
            value = "select user from User user left join fetch user.orders",
            countQuery = "select count(user) from User user"
    )
    Page<User> findAllWithToOneRelationships(Pageable pageable);

    @Override
    boolean existsById(Long id);
}
