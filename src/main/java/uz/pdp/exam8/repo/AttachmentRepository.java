package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.exam8.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}