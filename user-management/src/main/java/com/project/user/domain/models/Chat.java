package com.project.user.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat")
public class Chat extends BaseModel {
    @MongoId
    @Field(value = "_id")
    private String id = UUID.randomUUID().toString();
    @Field(value = "name")
    private String name;

    public String updateChatName(String userId, String anotherUserFullName) {
        StringBuilder newChatName = new StringBuilder();
        for (String idWithName: name.split("#")) {
            if (idWithName.startsWith(userId)) {
                newChatName.append(userId).append("&").append(anotherUserFullName).append("#");
            } else {
                newChatName.append(idWithName).append("#");
            }
        }
        return newChatName.substring(0, newChatName.toString().length() - 1);
    }
}
