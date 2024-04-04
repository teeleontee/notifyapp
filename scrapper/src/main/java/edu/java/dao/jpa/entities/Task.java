package edu.java.dao.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "task")
@IdClass(TaskPk.class)
@Entity
public class Task {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Id
    @Column(name = "link_id")
    private Long linkId;

    @ManyToOne
    @MapsId("linkId")
    @JoinColumn(name = "link_id")
    Link link;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    TgChat chat;
}
