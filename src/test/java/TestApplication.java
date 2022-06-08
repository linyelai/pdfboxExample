import com.linseven.Application;
import com.linseven.model.UserPO;
import com.linseven.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 13:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})// 指定启动类
@Slf4j
public class TestApplication {
    @Autowired
    private UserService userService;

    @Test
    public void findUser(){

        UserPO userPO = userService.findUser("test");
    }

    @Test
    public void addUser() throws Exception {
        UserPO userPO = new UserPO();
        userPO.setUsername("Tyrion1");
        userPO.setPassword("123456");
        userPO.setId(2);
        userService.addUser(userPO);
    }
}
