package com.musicsharing.connections;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class FriendLibrary {

@Expose
private String userId;
@Expose
private List<FriendLibrarySong> userLibraryDTOList = new ArrayList<FriendLibrarySong>();

/**
*
* @return
* The userId
*/
public String getUserId() {
return userId;
}

/**
*
* @param userId
* The userId
*/
public void setUserId(String userId) {
this.userId = userId;
}

/**
*
* @return
* The userLibraryDTOList
*/
public List<FriendLibrarySong> getFriendLibrary() {
return userLibraryDTOList;
}

/**
*
* @param userLibraryDTOList
* The userLibraryDTOList
*/
public void setUserLibraryDTOList(List<FriendLibrarySong> userLibraryDTOList) {
this.userLibraryDTOList = userLibraryDTOList;
}

}
