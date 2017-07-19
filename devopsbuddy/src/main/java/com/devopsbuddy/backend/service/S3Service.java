package com.devopsbuddy.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

/**
 * Created by Jayden on 7/19/2017.
 */
@Service
public class S3Service {
    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final String PROFILE_PICTURE_FILE_NAME = "profilePicture";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${image.store.temp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3 amazonS3;

    public String storeProfileImage(MultipartFile uploadedFile, String username) throws IOException{
        String profileImageUrl = null;

        try {
            if (uploadedFile != null && !uploadedFile.isEmpty()) {
                byte[] bytes = uploadedFile.getBytes();

                // The root of our temporary assets. Will create if it doesn't exist
                File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);
                if (!tmpImageStoredFolder.exists()) {
                    LOG.info("Creating the temporary root for the S3 assets");
                    tmpImageStoredFolder.mkdirs();
                }

                // The temporary file where the profile image will be stored
                File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath()
                        + File.separatorChar
                        + PROFILE_PICTURE_FILE_NAME
                        + "."
                        + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));

                LOG.info("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

                try(BufferedOutputStream stream =
                            new BufferedOutputStream(
                                    new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))) {
                    stream.write(bytes);
                }

                profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

                // Clean up the temporary folder
                tmpProfileImageFile.delete();
            }
        } catch (IOException e) {
            throw new IOException(e);
        }

        return profileImageUrl;
    }

    private String storeProfileImageToS3(File resource, String username) {
        String resourceUrl = null;

        if(!resource.exists()){
            LOG.error("The file {} doesn't exist. Throwing an exception", resource.getAbsolutePath());
            throw new IllegalArgumentException("The file " + resource.getAbsolutePath() + " doesn't exist.");
        }

        String rootBucketUrl = this.ensuraBucketExists(bucketName);
        LOG.info("The root bucket url: {} ", rootBucketUrl);

        if(rootBucketUrl == null){
            LOG.error("The bucket {} does not exist and the application " +
            "was not able to create it. The image won't be stored with the profile", rootBucketUrl);
        }else{
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username + "/" + PROFILE_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(resource.getName());
            try{
                amazonS3.putObject(new PutObjectRequest(bucketName,key,resource).withAccessControlList(acl));
                resourceUrl = amazonS3.getUrl(bucketName, key).getPath();
            }catch(AmazonClientException ace){
                LOG.error("A client exception occurred with trying to store the profile" +
                "image {} on s3. The profile image won't be stored", resource.getAbsolutePath(), ace);
            }
        }
        return resourceUrl;
    }

    private String ensuraBucketExists(String bucketName) {
        String bucketUrl = null;

        try{
            if(!amazonS3.doesBucketExist(bucketName)){
                LOG.info("Bucket {} doesn't exists .. Creating one.", bucketName);
                amazonS3.createBucket(bucketName);
                LOG.info("Create Bucket: {}", bucketName);
            }
            bucketUrl = amazonS3.getUrl(bucketName,null).getPath() + bucketName;
        }catch (AmazonClientException ace){
            LOG.error("An error occurred while connecting to S3. Will not execute action" +
                    " for bucket: {}", bucketName, ace);
            throw new AmazonClientException(ace);
        }
        return bucketUrl;
    }
}
