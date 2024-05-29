package org.minispring.app.controller;

import org.minispring.app.entity.User;
import org.minispring.core.web.ModelAndView;
import org.minispring.core.web.RequestMapping;
import org.minispring.core.web.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/test1")
    public String doTest1() {
        return "test 1, hello world!";
    }

    @RequestMapping("/test2")
    public String doTest2() {
        return "test 2, hello world!";
    }

    @RequestMapping("/test3")
    public String doTest3(User user) {
        log.info("doTest3:{}", user);
        return user.toString();
    }

    @RequestMapping("/test4")
    @ResponseBody
    public User doTest4(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        return user;
    }

    @RequestMapping("/test5")
    public ModelAndView doTest5(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        ModelAndView mv = new ModelAndView();
        mv.addAttribute("user", user);
        mv.setViewName("test5");
        return mv;
    }
}
