package uz.pdp.exam8.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Boolean isCompleted;
    private Integer orderNumber;
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
}
