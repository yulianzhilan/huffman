import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.HelperService;

/**
 * Created by scott on 2017/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-context.xml"})
public class HelperTest {
    @Autowired
    private HelperService helperService;
    @Test
    public void test1(){
        System.out.println(helperService.getInfoFromDataBase(1,"ARTICLE", "FOLDER", "jane").get(0).getDictName());
    }

}
