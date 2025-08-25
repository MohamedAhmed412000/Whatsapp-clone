package com.project.core.services;

import com.project.core.rest.outbound.StarredMessageResponse;

import java.util.List;

public interface StarredMessageService {
    List<StarredMessageResponse> findStarredMessages(int page);
    boolean starMessage(Long messageId);
    boolean unstarMessage(Long messageId);
}
