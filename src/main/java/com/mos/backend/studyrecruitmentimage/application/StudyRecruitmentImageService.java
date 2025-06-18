package com.mos.backend.studyrecruitmentimage.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.presentation.requestdto.StudyCreateRequestDto;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studymaterials.application.fileuploader.Uploader;
import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.studyrecruitmentimage.infrastructure.StudyRecruitmentImageRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StudyRecruitmentImageService {

    private final StudyRecruitmentImageRepository studyRecruitmentImageRepository;
    private final Uploader uploader;
    private final EntityFacade entityFacade;
    private final StudyService studyService;

    @Transactional
    public String temporaryUpload(UploadType type, MultipartFile file, Long userId) {
        User user = entityFacade.getUser(userId);
        String uuidFileName = uploader.generateUUIDFileName(file);
        String filePath = uploader.uploadFileSync(uuidFileName, userId, type, file);
        StudyRecruitmentImage studyRecruitmentImage = StudyRecruitmentImage.create(user, filePath, file.getOriginalFilename(), file.getSize());
        studyRecruitmentImageRepository.save(studyRecruitmentImage);
        return filePath;
    }

    @Transactional
    public void permanentUpload(Long userId, String content, Long studyId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        List<StudyRecruitmentImage> studyRecruitmentImageList = studyRecruitmentImageRepository.findAllByUser(user);

        Map<Boolean, List<StudyRecruitmentImage>> dividedRecruitImageList = divideRecruitImageList(studyRecruitmentImageList, content);
        List<StudyRecruitmentImage> imageToDelete = dividedRecruitImageList.getOrDefault(false, Collections.emptyList());
        imageToDelete.forEach(this::delete);

        List<StudyRecruitmentImage> imageToProcess = dividedRecruitImageList.getOrDefault(true, Collections.emptyList());
        imageToProcess.forEach( image -> {
            process(image, studyId);
            image.changeToPermanent(userId, study);
            studyService.changeImageToPermanent(userId, studyId);
        });
    }

    private Map<Boolean, List<StudyRecruitmentImage>> divideRecruitImageList(List<StudyRecruitmentImage> studyRecruitmentImageList, String content) {
        return studyRecruitmentImageList.stream().collect(Collectors.partitioningBy(
                ri -> content.contains(ri.getFilePath())
        ));
    }

    private void delete(StudyRecruitmentImage studyRecruitmentImage) {
        uploader.deleteFile(studyRecruitmentImage.getFilePath());
        studyRecruitmentImageRepository.delete(studyRecruitmentImage);
    }

    private void process(StudyRecruitmentImage studyRecruitmentImage, Long studyId) {
        String filePath = studyRecruitmentImage.getFilePath();
        String sourceKey = uploader.uriToFileObjectKey(filePath);
        String uuidFileName = filePathToUUIDString(filePath);
        String destinationKey = uploader.generateFileObjectKey(UploadType.STUDY, studyId, uuidFileName);
        uploader.moveFile(sourceKey, destinationKey);
    }

    private String filePathToUUIDString(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

}
