package uz.pdp.exam8.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private LocalDate deadline;
    @OneToMany
    private List<Comment> comment = new ArrayList<>();
    @ManyToMany
    private List<User> users = new ArrayList<>();
    @ManyToOne
    private Card card;
    @ManyToOne
    private Attachment attachment;

}
