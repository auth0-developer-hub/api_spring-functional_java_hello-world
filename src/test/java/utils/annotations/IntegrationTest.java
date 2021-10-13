package utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.helloworld.HelloWorldApplication;

import org.springframework.boot.test.context.SpringBootTest;

@UnitTest
@Target(ElementType.TYPE)
@SpringBootTest(classes = { HelloWorldApplication.class })
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {

}
