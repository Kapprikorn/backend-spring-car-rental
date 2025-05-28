package nl.novi.sd.carrental;

import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin != null) {
            admin.setPassword(passwordEncoder.encode("Admin!23"));
            userRepository.save(admin);
        }
        User user = userRepository.findByUsername("user").orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode("User!23"));
            userRepository.save(user);
        }
    }
}