package net.eulerframework.web.module.file.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.eulerframework.web.module.file.conf.FileConfig;

@Component
public class FileStoreListener implements ServletContextListener {
    private final Logger log = LogManager.getLogger();
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String archiveFilePath = FileConfig.getFileArchivedPath();   

        File targetDir = new File(archiveFilePath);
        
        if(!targetDir.exists()) {
            targetDir.mkdirs();
            this.log.info("Create File Archive Dir: " + targetDir.getAbsolutePath());
        } else {
            this.log.info("File Archive Dir exist: " + targetDir.getAbsolutePath());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
