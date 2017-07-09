package com.SpringOne;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloWorldController {
	
	@GetMapping("/")
	public String sayHello(){
		return "index";
	}
}
