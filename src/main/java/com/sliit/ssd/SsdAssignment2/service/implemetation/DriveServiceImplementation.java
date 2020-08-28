package com.sliit.ssd.SsdAssignment2.service.implemetation;

import javax.annotation.PostConstruct;

import com.google.api.services.drive.model.FileList;
import com.sliit.ssd.SsdAssignment2.constant.ApplicationConstant;
import com.sliit.ssd.SsdAssignment2.model.FileItem;
import com.sliit.ssd.SsdAssignment2.model.Message;
import com.sliit.ssd.SsdAssignment2.service.AuthorizationService;
import com.sliit.ssd.SsdAssignment2.service.DriveService;
import com.sliit.ssd.SsdAssignment2.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.util.ArrayList;
import java.util.List;


@Service
public class DriveServiceImplementation implements DriveService {

    private Logger logger = LoggerFactory.getLogger(DriveServiceImplementation.class);

    private Drive driveService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    ApplicationConfig applicationConfig;

    @PostConstruct
    public void init() throws Exception {
        Credential credential = authorizationService.getCredentials();
        driveService = new Drive.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY, credential)
                .setApplicationName(ApplicationConstant.APPLICATION_NAME).build();
    }

    @Override
    public void uploadFile(MultipartFile multipartFile) throws Exception {
        logger.debug("Inside Upload Service...");
        logger.debug("FILE NAME::::: " + multipartFile.getOriginalFilename());
        System.out.println();
        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        Credential credential = authorizationService.getCredentials();
        Drive drive = new Drive.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY, credential)
                .setApplicationName("ssd-assignment2").build();


        String path = "C:\\Users\\Vinu\\Desktop\\SsdAssignment2\\src\\main\\resources\\tempfolder\\"+fileName;
        FileContent mediaContent = new FileContent(contentType, new java.io.File(path));
        File file = drive.files().create(fileMetadata, mediaContent).setFields("id").execute();



        logger.debug("File ID: " + file.getName() + ", " + file.getId());
    }

    @Override
    public List<FileItem> getAllFiles() throws Exception {
        Credential credential = authorizationService.getCredentials();

        Drive drive = new Drive.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY,credential)
                .setApplicationName("ssd-assignment2").build();

        List<FileItem> responseList = new ArrayList<>();

        FileList fileList = drive.files().list().setFields("files(id,name,thumbnailLink)").execute();
        for (File file : fileList.getFiles()) {
            FileItem item = new FileItem();
            item.setId(file.getId());
            item.setName(file.getName());
            item.setThumbnailLink(file.getThumbnailLink());
            responseList.add(item);
        }

        return responseList;

    }

    @Override
    public Message deleteFile(String fileId) throws Exception {
        Credential credential = authorizationService.getCredentials();

        Drive drive = new Drive.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY,credential)
                .setApplicationName("ssd-assignment2").build();

        drive.files().delete(fileId).execute();

        Message message = new Message();
        message.setMessage("File has been deleted.");
        return message;
    }


}
