package uz.pdp.exam8.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskGet {
    private String description;
    private Integer taskId;
    private MultipartFile file;
    private LocalDate deadline;
}
