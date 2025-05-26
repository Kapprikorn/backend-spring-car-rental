package nl.novi.sd.carrental;

import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.model.UserRole;
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
        // Only initialize if no users exist
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setName("Admin User");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@testaccount.com");
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);

            // Create regular user
            User user = new User();
            user.setName("Regular User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@testaccount.com");
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
    }
}