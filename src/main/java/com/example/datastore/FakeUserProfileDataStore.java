package com.example.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();
	
	static {
		USER_PROFILES.add(new UserProfile(UUID.fromString("f5dcad67-7172-4f06-a16c-68254d74ee85"), "janetjones", null));
		USER_PROFILES.add(new UserProfile(UUID.fromString("22502cb6-77a0-4fad-8b6f-a6608215e300"), "jessicajames", null));
	}
	
	public List<UserProfile> getUserProfiles(){
		return USER_PROFILES;
	}
}
