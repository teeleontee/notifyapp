package edu.java.dao.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tgchat")
@Entity
public class TgChat {
    @Id
    private Long id;

    @OneToMany(mappedBy = "chat")
    Set<Task> tasks;
}
