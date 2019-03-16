package org.se;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
public class SimpleEtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleEtlApplication.class, args);
	}

	@RequestMapping("/")
	public ModelAndView index(){
		return new ModelAndView("index");
	}

}
