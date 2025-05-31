package com.project.media.services.impl;

import com.project.media.services.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaServiceImpl implements MediaService {


    public String uploadMediaMessage(String chatId, String senderId, MultipartFile file) {
        return null;
    }

}
