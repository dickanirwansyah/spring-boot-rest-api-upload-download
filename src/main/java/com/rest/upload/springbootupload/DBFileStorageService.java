package com.rest.upload.springbootupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public DBFile storeFile(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{

            if (fileName.contains("..")){
                throw new FileStorageException("Sorry ! Filename contains is invalid path sequence "+fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            return dbFileRepository.save(dbFile);
        }catch (IOException e){
            e.printStackTrace();
            throw new FileStorageException("Could not store file "+fileName+". please try again ! ");
        }
    }

    public DBFile getFile(String fileId){
        return dbFileRepository.findById(fileId)
                .orElseThrow(()-> new MyFileNotFoundException("file not found with id "+fileId));
    }
}
