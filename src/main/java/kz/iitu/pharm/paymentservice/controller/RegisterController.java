package kz.iitu.pharm.paymentservice.controller;

import kz.iitu.pharm.paymentservice.service.impl.BasketServiceImpl;
import kz.iitu.pharm.paymentservice.service.impl.DrugServiceImpl;
import kz.iitu.pharm.paymentservice.service.impl.UserServiceImpl;
import kz.iitu.pharm.paymentservice.entity.Role;
import kz.iitu.pharm.paymentservice.entity.User;
import kz.iitu.pharm.paymentservice.repository.BasketRepository;
import kz.iitu.pharm.paymentservice.repository.DrugRepository;
import kz.iitu.pharm.paymentservice.repository.RoleRepository;
import kz.iitu.pharm.paymentservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private DrugServiceImpl drugService;
    @Autowired
    private BasketServiceImpl basketService;

    @GetMapping({"/","/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> roles = Arrays.asList(userRole);
        model.addAttribute("signup",true);
        model.addAttribute("userForm", new User());
        model.addAttribute("roles",roles);
        return "signup";
    }

    @PostMapping("/signup")
    public String signupAction(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
        Role userRole = roleRepository.findByName("USER");
        List<Role> roles = Arrays.asList(userRole);
        model.addAttribute("userForm", user);
        model.addAttribute("roles",roles);
        model.addAttribute("signup",true);

        if(result.hasErrors()) {
            return "signup";
        }else {
            try {
                userService.createUser(user);
            } catch (Exception e) {
                model.addAttribute("formErrorMessage",e.getMessage());
            }
        }
        return login();
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("drugs", drugService.getAllDrugs());
        model.addAttribute("roles",roleRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("basketId",basketService.getDrugsInBasket());
        return "index";
    }
    @GetMapping("/basket")
    public String basket(Model model) {
        model.addAttribute("drugs", basketService.getDrugsInBasket());
        model.addAttribute("total", basketService.getTotal().toString());
        return "basket";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
        if(result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab","active");
        }else {
            try {
                userService.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab","active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage",e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab","active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles",roleRepository.findAll());
            }
        }

        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleRepository.findAll());
        return "user-view";
    }

}
