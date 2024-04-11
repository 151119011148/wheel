package com.wheel.service;

import com.google.common.collect.Lists;
import com.wheel.common.exception.ResultCode;
import com.wheel.common.exception.ServiceException;
import com.wheel.controller.response.ImageVO;
import com.wheel.dao.ProductDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    //自己设置的目录
    public static final String fileDir = "img";
    public static final AtomicInteger SUFFIX = new AtomicInteger(0);

    @Resource
    ProductDao productDao;

    @Value("${file.upload.resource.location:/Users/gaoying/Desktop/work/l78zdemo/wheel/src/main/resources/fileStorage/}")
    private String resourceLocation;

    @Value(value = "${file.upload.suffix:jpg,jpeg,png}")
    private String fileUploadSuffix;

    public List<File> upload(List<MultipartFile> multipartFiles, boolean checkSuffixSuccess)  {
        List<File> files = Lists.newArrayList();
        multipartFiles.stream().forEach(multipartFile -> {
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //校验文件后缀
            if (!checkSuffixSuccess) {
                throw new ServiceException(ResultCode.PARAM_CHECK_FAILED.getCode(), "unsupported file format");
            }

            //首次需生成目录
            File folder = new File(resourceLocation);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = timeFormat(System.currentTimeMillis()) + SUFFIX.getAndIncrement() + "." + suffix;
            String absolutePath = resourceLocation + fileName;
            log.info("absolutePath is {}", absolutePath);
            File file = new File(absolutePath);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            files.add(file);

            // 创建一个权限集合，设置要修改的权限
            Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ); // 设置所有者读取权限
            permissions.add(PosixFilePermission.OWNER_WRITE); // 设置所有者写入权限
            permissions.add(PosixFilePermission.GROUP_READ); // 设置群组读取权限
            permissions.add(PosixFilePermission.OTHERS_READ); // 设置其他用户读取权限
            try {
                // 使用Files类的setPosixFilePermissions方法来设置文件的权限
                Files.setPosixFilePermissions(file.toPath(), permissions);
                System.out.println("文件权限修改成功！");
            } catch (Exception e) {
                System.out.println("文件权限修改失败：" + e.getMessage());
            }
        });
        return files;
    }

    public String uploadImage(MultipartFile multipartFile)  {
        List<String> suffixList = Lists.newArrayList(fileUploadSuffix.split(","));
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        File file = upload(Lists.newArrayList(multipartFile), suffixList.contains(suffix)).get(0);
        return getFileUrl(file);
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles)  {
        multipartFiles.stream().forEach(multipartFile -> {
            List<String> suffixList = Lists.newArrayList(fileUploadSuffix.split(","));
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            //校验文件后缀
            if (!suffixList.contains(suffix)) {
                throw new ServiceException(ResultCode.PARAM_CHECK_FAILED.getCode(), "unsupported file format");
            }
        });
        List<File> files = upload(multipartFiles, true);
        return files.stream()
                .map(FileService::getFileUrl)
                .collect(Collectors.toList());
    }

    private static String getFileUrl(File file) {
        return File.separator + fileDir + File.separator + file.getName();
    }

    public static String timeFormat(Long time) {
        if (Objects.isNull(time)) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(time);

    }

    public List<ImageVO> listImages() {
        //首次需生成目录
        File folder = new File(resourceLocation);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> {
                    String fileName = file.getName();
                    int lastIndexOfDot = fileName.lastIndexOf('.');
                    String fileExtension = fileName.substring(lastIndexOfDot + 1);
                    return fileUploadSuffix.contains(fileExtension);
                })
                .map(file -> new ImageVO(file.getName(), getFileUrl(file), new Date(file.lastModified())))
                .peek(image -> image.setIsUsed(CollectionUtils.isNotEmpty(productDao.findImageByFuzzy(image.getFileName()))))
                .sorted(Comparator.comparing(ImageVO::getModifiedTime).reversed())
                .collect(Collectors.toList());
    }
}
