package com.project.whatsapp.domain.models;

import com.project.whatsapp.domain.converters.UserRolePriorityConverter;
import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAT_USER")
public class ChatUser extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "CHAT_ID")
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @Enumerated(EnumType.ORDINAL)
    @Convert(converter = UserRolePriorityConverter.class)
    @Column(name = "USER_ROLE")
    private ChatUserRoleEnum role;
    @Column(name = "LAST_SEEN_MESSAGE_AT")
    private LocalDateTime lastSeenMessageAt;
}
