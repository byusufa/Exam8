package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.exam8.entity.AttachmentContent;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Integer> {
}