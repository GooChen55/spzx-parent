package com.spzx.file.test;

import io.minio.*;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileUploader {
  public static void main(String[] args)
      throws IOException, NoSuchAlgorithmException, InvalidKeyException {
    try {
      // 创建客户端对象
      MinioClient minioClient =
          MinioClient.builder()
                  //设置miniou服务器的地址
              .endpoint("http://192.168.6.100:9000")
                  //设置密钥对，如果没有配置设置用户名和密码
              .credentials("admin", "admin123456")
              .build();

      // 判断某个同是否存在
      boolean found =
          minioClient.bucketExists(BucketExistsArgs.builder().bucket("star").build());
      if (!found) {
        // 如果桶不存在则创建它
        minioClient.makeBucket(MakeBucketArgs.builder().bucket("star").build());
      } else {
        System.out.println("Bucket 'spzx' already exists.");
      }

      // 创建UploadObjectArgs对象
      UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
              //设置桶的名字
              .bucket("star")
              //设置上传到minio服务器之后文件的名字
              .object("wuqian.jpg")
              //设置要上传的文件的路径
              .filename("E:/凡凡.jpg")
              .build();

//      PutObjectArgs putObjectArgs = PutObjectArgs.builder("")
//              .object("")
//              .stream()
//              .build();
      //通过客户端对象上传本地的路径
      minioClient.uploadObject(uploadObjectArgs);
      System.out.println("文件上传成功");
    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
      System.out.println("HTTP trace: " + e.httpTrace());
    }
  }
}