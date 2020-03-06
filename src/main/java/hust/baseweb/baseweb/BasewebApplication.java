package hust.baseweb.baseweb;

import hust.baseweb.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BasewebApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BasewebApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println(UserLogin.PASSWORD_ENCODER.encode("admin"));
	}
}
