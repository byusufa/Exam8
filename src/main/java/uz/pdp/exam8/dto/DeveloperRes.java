package uz.pdp.exam8.dto;

import jakarta.persistence.Column;
import lombok.*;


public interface DeveloperRes {
     Integer getAttachmentId() ;
     Integer getTotalTasks();
     Integer getExpiredTasks();
     Integer getApproachingTasks();
     Integer getLongDeadlineTasks();
     Integer getTasksCompleted();


}

