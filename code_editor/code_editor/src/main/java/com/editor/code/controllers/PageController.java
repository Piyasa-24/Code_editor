package com.editor.code.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/editor")
    public String openEditor (HttpSession session) {

        if(session.getAttribute("user")==null){
            return "redirect:/login";
        }
    return "index"; 
}

@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}
}

