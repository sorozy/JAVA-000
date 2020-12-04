package me.lgd.repository;

import me.lgd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author lgd
 * @date 2020/12/1 13:56
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("update User set name = ?1 where id = ?2")
    int updateById(String name, Integer id);
}
