package cn.rzwl.multidata.ctrl;

import cn.rzwl.multidata.dao.PersonRepo;
import cn.rzwl.multidata.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author : championjing
 * @ClassName: PersonCtrl
 * @Description:
 * @Date: 7/10/2019 3:03 PM
 */
@RestController
@Slf4j
public class PersonCtrl {
    
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void seeSource(){
        log.info("data:{}",dataSource.getClass());
    }
    
    @GetMapping("/person")
    public List<Person> list(){
        log.info("获取所有用户");
        List<Person> all = personRepo.findAll();
        log.info("用户条数:{}",all.size());
        return all;
    }
}
