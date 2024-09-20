package com.airfranceklm.fasttrack.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class AngularForwardController {

    @RequestMapping(method = RequestMethod.GET)
    public String forwardToAngular() {
        return "forward:/browser/index.html";
    }
}
