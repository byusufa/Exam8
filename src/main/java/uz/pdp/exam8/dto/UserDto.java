package uz.pdp.exam8.dto;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.exam8.entity.Attachment;


public interface UserDto {
      Integer getAttachmentId();
      String getFullName();
      Integer getCountTask();

}
