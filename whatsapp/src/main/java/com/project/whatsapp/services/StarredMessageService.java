package com.project.whatsapp.services;

import com.project.whatsapp.rest.outbound.StarredMessageResponse;

import java.util.List;

public interface StarredMessageService {
    List<StarredMessageResponse> findStarredMessages(int page);
    boolean starMessage(Long messageId);
    boolean unstarMessage(Long messageId);
}
