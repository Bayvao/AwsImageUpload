package com.example.bucket;

public enum BucketName {

	PROFILE_IMAGE("bayvao-file-upload");

	private final String bucketName;

	BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}

}
