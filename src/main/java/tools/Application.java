package tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import tools.util.DateUtil;

@SpringBootApplication
@EnableScheduling
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Value("${log1.path}")
    private String log1Path;

    @Value("${log2.path}")
    private String log2Path;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void getLogFile() {

        try {
            downloadLogFile("14.49.36.128:3322", log1Path);
            downloadLogFile("14.49.36.129:3322", log2Path);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private void downloadLogFile(String serverAddress, String localDirectory) throws FileSystemException {

        StandardFileSystemManager manager = null;
        FileObject localFile = null;
        FileObject remoteFile = null;
        try {
            manager = new StandardFileSystemManager();

            String fileToDownload = "/contents-info.log." + DateUtil.getBeforeDay(1) + ".log";
            String userId = "company";
            String password = "";
            String remoteDirectory = "usr/local/jboss-as/standalone/log";

            manager.init();

            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

            //Create the SFTP URI using the host name, userid, password,  remote path and file name
            String sftpUri = "sftp://" + userId + ":" + password + "@" + serverAddress + "/" + remoteDirectory
                    + fileToDownload;

            // Create local file object

            String filepath = localDirectory + fileToDownload;
            File file = new File(filepath);
            localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            remoteFile = manager.resolveFile(sftpUri, opts);

            // Copy local file to sftp server
            localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
            logger.info("File download successful. <{}>", filepath);

        } finally {
            if (localFile != null) {
                try {
                    localFile.close();
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
            if (remoteFile != null) {
                try {
                    remoteFile.close();
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
            if (manager != null) {
                manager.close();
            }
        }
    }

}
