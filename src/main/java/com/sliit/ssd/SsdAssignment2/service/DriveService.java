package com.sliit.ssd.SsdAssignment2.service;

import com.sliit.ssd.SsdAssignment2.model.FileItem;
import com.sliit.ssd.SsdAssignment2.model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriveService {

    public void uploadFile(MultipartFile multipartFile) throws Exception;

    public List<FileItem> getAllFiles() throws Exception;

    public Message deleteFile(String fileId) throws Exception;
}
