package com.spring.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeMonitoringController {
    @RequestMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/on")
    public String turnOn() {
        System.out.println("on");
        return "home";

    }
    @GetMapping("/off")
    public String turnOff() {
        System.out.println("off");
        return "home";
    }
}
