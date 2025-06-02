package com.mos.backend.users.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Category;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studymaterials.application.fileuploader.Uploader;
import com.mos.backend.users.application.responsedto.UserDetailRes;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.presentation.requestdto.UserUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final EntityFacade entityFacade;
    private final Uploader uploader;

    public void updateProfileInfo(Long userId, UserUpdateReq req) {
        User user = entityFacade.getUser(userId);

        String categories = req.getCategories().stream()
                .map(Category::getDescription)
                .collect(Collectors.joining(","));

        user.update(req.getNickname(), req.getIntroduction(), categories);
    }

    public UserDetailRes getDetail(Long userId) {
        User user = entityFacade.getUser(userId);
        return UserDetailRes.from(user);
    }

    public void updateProfileImage(Long userId, MultipartFile file, UploadType type) {
        User user = entityFacade.getUser(userId);

        String uuidFileName = uploader.generateUUIDFileName(file);
        String newFilePath = uploader.uploadFileSync(uuidFileName, userId, type, file);

        String oldFilePath = user.getImagePath();
        if (!isNullOrEmpty(oldFilePath))
            uploader.deleteFile(oldFilePath);

        user.updateImagePath(newFilePath);
    }
}
