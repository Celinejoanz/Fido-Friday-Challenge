package com.example.fido_fridaychallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception{
        System.out.println("Loading data...");

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole= roleRepository.findByRole("ADMIN");
        Role userRole= roleRepository.findByRole("USER");

        User user=new User("bob@bob.com", passwordEncoder.encode("bob"), "Bob", "Bobberson", true, "bob");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user=new User("jim@jim.com", passwordEncoder.encode("jim"), "Jim", "Jimmerson", true, "jim");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user=new User("admin@adm.com", passwordEncoder.encode("pass"), "Admin", "User", true, "admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        user=new User("sam@ev.com", passwordEncoder.encode("pass"), "Sam", "Everyman", true, "sam");
        user.setRoles(Arrays.asList(userRole, adminRole));
        userRepository.save(user);
    }
}