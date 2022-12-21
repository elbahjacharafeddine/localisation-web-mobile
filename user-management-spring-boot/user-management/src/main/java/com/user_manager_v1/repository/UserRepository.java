package com.user_manager_v1.repository;

import com.user_manager_v1.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {


    @Query(value = "SELECT email FROM users WHERE email = :email ", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);

    @Query(value = "SELECT password FROM users WHERE email = :email", nativeQuery = true)
    String checkUserPasswordByEmail(@Param("email") String email);

    @Query(value = " SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User GetUserDetailsByEmail(@Param("email") String email);





    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USERS(first_name, last_name, email, password,bdate,genre,phone) VALUES(:first_name, :last_name, :email, :password,:bdate,:genre,:phone)", nativeQuery = true)

    int registerNewUser(@Param("first_name") String first_name,
                        @Param("last_name") String last_name,
                        @Param("email") String email,
                        @Param("password") String password,
                        @Param("bdate") String bdate,
                        @Param("genre") String genre,
                        @Param("phone") String phone);

    @Query(value = " SELECT * FROM users", nativeQuery = true)
    List<User> findAllUser();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users_amis(users_id,amis_id,etat) values (?,?,'non')" ,nativeQuery = true)
    void addAmi(@PathVariable int user_id, @PathVariable int amis_id);


    @Query(value ="SELECT * from users e where e.id != :id and e.id not in (select amis_id from users_amis where etat='oui')  " ,nativeQuery = true)
    List<User> getUsers(@Param("id") int id);

    @Query(value ="select u.* from users u, users_amis a where u.id= a.users_id and a.etat ='non' and a.amis_id=:id" ,nativeQuery = true)
    List<User> getDemandeurInvit(@Param("id") int id);
    @Transactional
    @Modifying
    @Query(value ="UPDATE users_amis set etat = 'oui' where users_id =:user_id and amis_id=:ami_id" ,nativeQuery = true)
    void acceptInvitaion(@Param("user_id") int user_id, @Param("ami_id") int ami_id);

    @Query(value = "SELECT u.* from users u,users_amis a where u.id = a.users_id and a.etat='oui' and a.amis_id =:id ",nativeQuery = true)
    List<User> getAllAmis(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = "update users set image =:image where id = :id",nativeQuery = true)
    void updateImageUser(@Param("image") byte[] image, @PathVariable int id);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO ami(first_name, last_name, email, password) VALUES(:first_name, :last_name, :email, :password)", nativeQuery = true)
    int registerNewAmi(@Param("first_name") String first_name,
                        @Param("last_name") String last_name,
                        @Param("email") String email,
                        @Param("password") String password);



}
