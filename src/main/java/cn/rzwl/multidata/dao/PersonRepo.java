package cn.rzwl.multidata.dao;

import cn.rzwl.multidata.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : championjing
 * @ClassName: PersonRepo
 * @Description:
 * @Date: 7/10/2019 3:19 PM
 */
@Repository
public interface PersonRepo extends JpaRepository<Person,Long> {
    
}
