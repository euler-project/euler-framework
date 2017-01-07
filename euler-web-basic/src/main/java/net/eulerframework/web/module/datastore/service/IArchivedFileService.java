package net.eulerframework.web.module.datastore.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import net.eulerframework.web.core.base.service.IBaseService;
import net.eulerframework.web.module.datastore.entity.ArchivedFile;
import net.eulerframework.web.module.datastore.exception.FileArchiveException;

public interface IArchivedFileService extends IBaseService {

    public ArchivedFile saveFile(File file) throws FileArchiveException;

    public ArchivedFile saveMultipartFile(MultipartFile multipartFile) throws FileArchiveException;

    public ArchivedFile findArchivedFile(String archivedFileId);

}