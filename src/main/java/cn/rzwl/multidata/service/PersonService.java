package cn.rzwl.multidata.service;

import cn.rzwl.multidata.dao.PersonRepo;
import cn.rzwl.multidata.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : championjing
 * @ClassName: PersonService
 * @Description:
 * @Date: 7/11/2019 10:34 AM
 */
@Service
public class PersonService {
    @Autowired
    private PersonRepo personRepo;
    
    public List<Person> list(){
        return personRepo.findAll();
    }
}
