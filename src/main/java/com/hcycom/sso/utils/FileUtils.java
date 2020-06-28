package com.hcycom.sso.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-05-11 9:26
 */
public class FileUtils {

    public static final List<String> IMG_FILTER = new ArrayList<>();
    static {
        IMG_FILTER.add("jpg");
        IMG_FILTER.add("gif");
        IMG_FILTER.add("png");
    }


    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

}
