package edu.java.dao.jpa.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Table(name = "task")
public class TaskPk implements Serializable {
    private Long chatId;
    private Long linkId;

    public TaskPk(Long chatId, Long linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaskPk taskPk = (TaskPk) obj;
        return Objects.equals(chatId, taskPk.chatId) && Objects.equals(linkId, taskPk.linkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, linkId);
    }
}
