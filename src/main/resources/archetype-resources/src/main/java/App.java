import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private final static Logger logger = LoggerFactory.getLogger("App");

    public static void main(String[] args) {

        try {

            // initialize the Spring context.
            final ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring.xml");

        } catch (Exception e) {

            logger.error("An exception occurred.", e);

        }
    }

}
