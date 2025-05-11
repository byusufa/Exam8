package uz.pdp.exam8.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.exam8.entity.Attachment;
import uz.pdp.exam8.entity.AttachmentContent;
import uz.pdp.exam8.entity.User;
import uz.pdp.exam8.repo.AttachmentContentRepository;
import uz.pdp.exam8.repo.AttachmentRepository;
import uz.pdp.exam8.repo.UserRepository;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String update;

    @Override
    public void run(String... args) throws Exception {
        if (update.equals("create")) {
            InputStream inputStream1 = Files.newInputStream(Paths.get("src/main/resources/photo/user1.jpg"));
            byte[] allBytes1 = inputStream1.readAllBytes();
            InputStream inputStream2 = Files.newInputStream(Paths.get("src/main/resources/photo/user2.jpg"));
            byte[] allBytes2 = inputStream2.readAllBytes();
            InputStream inputStream3 = Files.newInputStream(Paths.get("src/main/resources/photo/user3.jpg"));
            byte[] allBytes3 = inputStream3.readAllBytes();

            Attachment attachment1 = new Attachment(null, "user1");
            attachmentRepository.save(attachment1);
            AttachmentContent attachmentContent1 = new AttachmentContent(null, allBytes1, attachment1);
            attachmentContentRepository.save(attachmentContent1);
            Attachment attachment2 = new Attachment(null, "user2");
            attachmentRepository.save(attachment2);
            AttachmentContent attachmentContent2 = new AttachmentContent(null, allBytes2, attachment2);
            attachmentContentRepository.save(attachmentContent2);
            Attachment attachment3 = new Attachment(null, "user3");
            attachmentRepository.save(attachment3);
            AttachmentContent attachmentContent3 = new AttachmentContent(null, allBytes3, attachment3);
            attachmentContentRepository.save(attachmentContent3);
            List<User> users = new ArrayList<>(List.of(
                    new User(null, "a", passwordEncoder.encode("1"), "Eshmat", "Toshmatov", "ROLE_USER", attachment1, new ArrayList<>()),
                    new User(null, "b", passwordEncoder.encode("2"), "Ali", "Valiyev", "ROLE_USER", attachment2, new ArrayList<>()),
                    new User(null, "c", passwordEncoder.encode("3"), "Nusrat", "Hikmatov", "ROLE_USER", attachment3, new ArrayList<>())
            ));
            userRepository.saveAll(users);
        }

    }
}
