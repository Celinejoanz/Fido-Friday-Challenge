package com.example.fido_fridaychallenge;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    PetRepository petRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("pets", petRepository.findAll());
        return "index";
    }

//    @RequestMapping("/show")
//    public String show(HttpServletRequest request, Model model){
//        String user= userRepository.findById();
//        model.addAttribute("user", userRepository.findById(id));
//        return "show";
//    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

//    @RequestMapping("/secure")
//    public String admin(){
//        return "secure";
//    }
//

    @RequestMapping("/index")
    public String secure(HttpServletRequest request, Authentication authentication, Principal principal){
        Boolean isAdmin =  request.isUserInRole("ADMIN");
        Boolean isUser =  request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();
        return "index";
    }

    @RequestMapping(value="/register", method= RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value="/register", method= RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        if(result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }
    @RequestMapping("/add")
    public String newPet(Model model){
        model.addAttribute("pet", new Pet());
        return "form";
    }
    @PostMapping("/add")
    public String processPet(@ModelAttribute("pet") Pet pet, @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try{
            Map uploadResult= cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            pet.setImg(uploadResult.get("url").toString());
            petRepository.save(pet);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updatePet(@PathVariable("id") long id, Model model){
        model.addAttribute("pet", petRepository.findById(id));
        return "form";
    }

}
