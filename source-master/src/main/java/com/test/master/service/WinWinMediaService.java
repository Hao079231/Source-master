package com.test.master.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WinWinMediaService {
  public boolean deleteFile(String filePath){
    try{
      Path path = Paths.get(filePath);
      return Files.deleteIfExists(path);
    } catch (Exception e){
      return false;
    }
  }
}
