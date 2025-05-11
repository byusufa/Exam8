package uz.pdp.exam8.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskUpdateRequest {
    private Integer taskId;
    private Integer cardId;
}
