package uz.pdp.exam8.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilter {

    private String title;
    private String deadline;
    private Integer userId;


}
