package com.mos.backend.studyrecruitmentimage.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.presentation.requestdto.StudyCreateRequestDto;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studymaterials.application.fileuploader.FileUploader;
import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.studyrecruitmentimage.infrastructure.StudyRecruitmentImageRepository;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StudyRecruitmentImageServiceTest {

    @InjectMocks
    private StudyRecruitmentImageService studyRecruitmentImageService;

    @Mock
    private StudyRecruitmentImageRepository studyRecruitmentImageRepository;
    @Mock
    private FileUploader fileUploader;
    @Mock
    private EntityFacade entityFacade;
    @Mock
    private StudyService studyService;

    @Captor
    private ArgumentCaptor<StudyRecruitmentImage> studyRecruitmentImageCaptor;
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<StudyRecruitmentImage> deletedImageCaptor;

    @Test
    @DisplayName("temporaryUpload: 이미지 임시 업로드 성공")
    void temporaryUpload_Success() {
        Long userId = 1L;
        UploadType uploadType = UploadType.TEMP;
        String originalFilename = "image.png";
        String generatedUUID = "uuid-temp.png";
        String expectedFilePath = "temp/1/uuid-temp.png";
        User mockUser = mock(User.class);
        MultipartFile mockMultipartFile = new MockMultipartFile("file", originalFilename, "image/png", new byte[]{1, 2, 3});

        given(entityFacade.getUser(userId)).willReturn(mockUser);
        given(fileUploader.generateUUIDFileName(any(MultipartFile.class))).willReturn(generatedUUID);
        given(fileUploader.uploadFileSync(eq(generatedUUID), eq(userId), eq(uploadType), any(MultipartFile.class)))
                .willReturn(expectedFilePath);

        String resultFilePath = studyRecruitmentImageService.temporaryUpload(uploadType, mockMultipartFile, userId);

        assertThat(resultFilePath).isEqualTo(expectedFilePath);
        verify(studyRecruitmentImageRepository).save(studyRecruitmentImageCaptor.capture());
        StudyRecruitmentImage savedImage = studyRecruitmentImageCaptor.getValue();
        assertThat(savedImage.getUser()).isEqualTo(mockUser);
        assertThat(savedImage.getFilePath()).isEqualTo(expectedFilePath);
        assertThat(savedImage.getOriginalName()).isEqualTo(originalFilename); // Assuming getter is getOriginalFileName
        verify(fileUploader).uploadFileSync(eq(generatedUUID), eq(userId), eq(uploadType), eq(mockMultipartFile));
    }

    @Test
    @DisplayName("permanentUpload: 사용된 이미지는 영구 저장 위치로 이동, 미사용 이미지는 삭제")
    void permanentUpload_MoveUsedAndDeleteUnused() {
        Long userId = 1L;
        Long studyId = 10L;
        User mockUser = mock(User.class);
        Study mockStudy = mock(Study.class);
        StudyCreateRequestDto mockRequestDto = mock(StudyCreateRequestDto.class);

        StudyRecruitmentImage imageUsed = mock(StudyRecruitmentImage.class);
        StudyRecruitmentImage imageUnused = mock(StudyRecruitmentImage.class);
        String usedImagePath = "temp/1/used-image.jpg";
        String unusedImagePath = "temp/1/unused-image.jpg";
        String usedImageUUID = "used-image.jpg";

        given(entityFacade.getUser(userId)).willReturn(mockUser);
        given(entityFacade.getStudy(studyId)).willReturn(mockStudy);
        given(imageUsed.getFilePath()).willReturn(usedImagePath);
        given(imageUnused.getFilePath()).willReturn(unusedImagePath);
        given(studyRecruitmentImageRepository.findAllByUser(mockUser)).willReturn(List.of(imageUsed, imageUnused));

        String contentContainingUsedImage = "Some text <img src=\"" + usedImagePath + "\"> more text.";
        given(mockRequestDto.getContent()).willReturn(contentContainingUsedImage);

        String usedImageSourceKey = "temp/1/used-image.jpg";
        String usedImageDestinationKey = "study/" + studyId + "/" + usedImageUUID;
        given(fileUploader.uriToFileObjectKey(usedImagePath)).willReturn(usedImageSourceKey);
        given(fileUploader.generateFileObjectKey(UploadType.STUDY, studyId, usedImageUUID)).willReturn(usedImageDestinationKey);
        willDoNothing().given(fileUploader).moveFile(anyString(), anyString());
        willDoNothing().given(fileUploader).deleteFile(unusedImagePath);
        willDoNothing().given(studyRecruitmentImageRepository).delete(imageUnused);
        willDoNothing().given(imageUsed).changeToPermanent(userId, mockStudy);
        willDoNothing().given(studyService).changeImageToPermanent(userId, studyId);

        studyRecruitmentImageService.permanentUpload(userId, mockRequestDto, studyId);

        verify(fileUploader).uriToFileObjectKey(usedImagePath);
        verify(fileUploader).generateFileObjectKey(UploadType.STUDY, studyId, usedImageUUID);
        verify(fileUploader).moveFile(usedImageSourceKey, usedImageDestinationKey);
        verify(imageUsed).changeToPermanent(userId, mockStudy);
        verify(studyService).changeImageToPermanent(userId, studyId);
        verify(fileUploader).deleteFile(unusedImagePath);
        verify(studyRecruitmentImageRepository).delete(imageUnused);
        verify(fileUploader, never()).deleteFile(usedImagePath);
        verify(studyRecruitmentImageRepository, never()).delete(imageUsed);
        verify(imageUnused, never()).changeToPermanent(anyLong(), any(Study.class));
    }

    @Test
    @DisplayName("permanentUpload: 모든 이미지가 사용될 경우 전체 이동 및 삭제 없음")
    void permanentUpload_AllUsed_ShouldMoveAllAndDeleteNone() {
        Long userId = 1L;
        Long studyId = 10L;
        User mockUser = mock(User.class);
        Study mockStudy = mock(Study.class);
        StudyCreateRequestDto mockRequestDto = mock(StudyCreateRequestDto.class);

        StudyRecruitmentImage image1 = mock(StudyRecruitmentImage.class);
        StudyRecruitmentImage image2 = mock(StudyRecruitmentImage.class);
        String path1 = "temp/1/image1.png";
        String path2 = "temp/1/image2.gif";
        String uuid1 = "image1.png";
        String uuid2 = "image2.gif";
        given(image1.getFilePath()).willReturn(path1);
        given(image2.getFilePath()).willReturn(path2);

        given(entityFacade.getUser(userId)).willReturn(mockUser);
        given(entityFacade.getStudy(studyId)).willReturn(mockStudy);
        given(studyRecruitmentImageRepository.findAllByUser(mockUser)).willReturn(List.of(image1, image2));

        String contentContainingAllImages = "Content with <img src='" + path1 + "'> and <img src='" + path2 + "'>";
        given(mockRequestDto.getContent()).willReturn(contentContainingAllImages);

        String sourceKey1 = "temp/1/image1.png";
        String destKey1 = "study/" + studyId + "/" + uuid1;
        given(fileUploader.uriToFileObjectKey(path1)).willReturn(sourceKey1);
        given(fileUploader.generateFileObjectKey(UploadType.STUDY, studyId, uuid1)).willReturn(destKey1);
        String sourceKey2 = "temp/1/image2.gif";
        String destKey2 = "study/" + studyId + "/" + uuid2;
        given(fileUploader.uriToFileObjectKey(path2)).willReturn(sourceKey2);
        given(fileUploader.generateFileObjectKey(UploadType.STUDY, studyId, uuid2)).willReturn(destKey2);

        willDoNothing().given(fileUploader).moveFile(anyString(), anyString());
        willDoNothing().given(image1).changeToPermanent(anyLong(), any(Study.class));
        willDoNothing().given(image2).changeToPermanent(anyLong(), any(Study.class));
        willDoNothing().given(studyService).changeImageToPermanent(anyLong(), anyLong());

        studyRecruitmentImageService.permanentUpload(userId, mockRequestDto, studyId);

        verify(fileUploader, times(2)).moveFile(stringCaptor.capture(), stringCaptor.capture());
        assertThat(stringCaptor.getAllValues()).containsExactlyInAnyOrder(sourceKey1, destKey1, sourceKey2, destKey2);
        verify(image1).changeToPermanent(userId, mockStudy);
        verify(image2).changeToPermanent(userId, mockStudy);
        verify(studyService, times(2)).changeImageToPermanent(userId, studyId);
        verify(fileUploader, never()).deleteFile(anyString());
        verify(studyRecruitmentImageRepository, never()).delete(any(StudyRecruitmentImage.class));
    }

    @Test
    @DisplayName("permanentUpload: 사용된 이미지가 없을 경우 전체 삭제 및 이동 없음")
    void permanentUpload_NoneUsed_ShouldDeleteAllAndMoveNone() {
        Long userId = 1L;
        Long studyId = 10L;
        User mockUser = mock(User.class);
        Study mockStudy = mock(Study.class);
        StudyCreateRequestDto mockRequestDto = mock(StudyCreateRequestDto.class);

        StudyRecruitmentImage image1 = mock(StudyRecruitmentImage.class);
        StudyRecruitmentImage image2 = mock(StudyRecruitmentImage.class);
        String path1 = "temp/1/image1.png";
        String path2 = "temp/1/image2.gif";
        given(image1.getFilePath()).willReturn(path1);
        given(image2.getFilePath()).willReturn(path2);

        given(entityFacade.getUser(userId)).willReturn(mockUser);
        given(entityFacade.getStudy(studyId)).willReturn(mockStudy);
        given(studyRecruitmentImageRepository.findAllByUser(mockUser)).willReturn(List.of(image1, image2));

        String contentWithoutImages = "Content without any images.";
        given(mockRequestDto.getContent()).willReturn(contentWithoutImages);

        willDoNothing().given(fileUploader).deleteFile(anyString());
        willDoNothing().given(studyRecruitmentImageRepository).delete(any(StudyRecruitmentImage.class));

        studyRecruitmentImageService.permanentUpload(userId, mockRequestDto, studyId);

        verify(fileUploader, times(2)).deleteFile(stringCaptor.capture());
        assertThat(stringCaptor.getAllValues()).containsExactlyInAnyOrder(path1, path2);
        verify(studyRecruitmentImageRepository, times(2)).delete(deletedImageCaptor.capture());
        assertThat(deletedImageCaptor.getAllValues()).containsExactlyInAnyOrder(image1, image2);
        verify(fileUploader, never()).moveFile(anyString(), anyString());
        verify(image1, never()).changeToPermanent(anyLong(), any(Study.class));
        verify(image2, never()).changeToPermanent(anyLong(), any(Study.class));
        verify(studyService, never()).changeImageToPermanent(anyLong(), anyLong());
    }

    @Test
    @DisplayName("permanentUpload: 임시 이미지가 없을 경우 아무 작업도 수행하지 않음")
    void permanentUpload_NoTemporaryImagesFound() {
        Long userId = 1L;
        Long studyId = 10L;
        User mockUser = mock(User.class);
        Study mockStudy = mock(Study.class);
        StudyCreateRequestDto mockRequestDto = mock(StudyCreateRequestDto.class);

        given(entityFacade.getUser(userId)).willReturn(mockUser);
        given(entityFacade.getStudy(studyId)).willReturn(mockStudy);
        given(mockRequestDto.getContent()).willReturn("Some content");
        given(studyRecruitmentImageRepository.findAllByUser(mockUser)).willReturn(List.of());

        studyRecruitmentImageService.permanentUpload(userId, mockRequestDto, studyId);

        verify(fileUploader, never()).moveFile(anyString(), anyString());
        verify(fileUploader, never()).deleteFile(anyString());
        verify(studyRecruitmentImageRepository, never()).delete(any(StudyRecruitmentImage.class));
        verify(studyService, never()).changeImageToPermanent(anyLong(), anyLong());
    }

}