package com.example.profile;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bucket.BucketName;
import com.example.filestore.FileStore;

@Service
public class UserProfileService {

	private final UserProfileDataAccessService userprofileDataAccessService;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(UserProfileDataAccessService userprofileDataAccessService, FileStore fileStore) {
		this.userprofileDataAccessService = userprofileDataAccessService;
		this.fileStore = fileStore;
	}

	List<UserProfile> getUserProfiles() {
		return userprofileDataAccessService.getUserProfiles();
	}

	public void uploaduserProfileImage(UUID userProfileId, MultipartFile file) {

		// 1. check if image is not empty
		isFileEmpty(file);

		// 2. If file is an image
		isImage(file);

		// 3. The user exists in our database
		UserProfile user = getUserProfileOrThrow(userProfileId);

		// 4. Grab some metadata from file if any
		Map<String, String> metadata = extractMetadata(file);

		// 5. Store the image in s3 and update database (userProfileImageLink) with s3
		// image link
		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
		String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

		try {
			System.out.println("before save");
			fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
			System.out.println("after save");
			user.setUserPofileImageLink(filename);
			
			System.out.println(user.getUserPofileImageLink());
			
			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}
	
	public byte[] downloadUserProfileImage(UUID userProfileId) {
		
		UserProfile user = getUserProfileOrThrow(userProfileId);
		
		String path = String.format("%s/%s", 
				BucketName.PROFILE_IMAGE.getBucketName(), 
				user.getUserProfileId());

		System.out.println("In user profile service download");
		return user.getUserPofileImageLink()
				.map(key -> fileStore.download(path, key))
				.orElse(new byte[0]);
		
	}

	private Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}

	private UserProfile getUserProfileOrThrow(UUID userProfileId) {
		return userprofileDataAccessService.getUserProfiles().stream()
				.filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId)).findFirst().orElseThrow(
						() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
	}

	private void isImage(MultipartFile file) {
		if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
			throw new IllegalStateException("File must be an Image" + file.getContentType());
		}
	}

	private void isFileEmpty(MultipartFile file) {
		if (file.isEmpty()) {
			throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
		}
	}

}
