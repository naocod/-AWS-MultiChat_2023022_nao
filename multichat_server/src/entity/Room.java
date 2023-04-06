package entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import main.ConnectedSocket;

// 방장의 이름이 바뀌거나 방 이름을 바꾸는 기능이 없기에 getter
@Getter 
public class Room {
	private String roomName;
	private String owner;
	private List<ConnectedSocket> users;
	
	public Room(String roomName, String owner) {
		this.roomName = roomName;
		this.owner = owner;
		users = new ArrayList<>();
	}
	
	public List<String> getUsernameList() {
		List<String> usernameList = new ArrayList<>();
		for(ConnectedSocket connectedSocket : users) {
			usernameList.add(connectedSocket.getUsername());
		}
		return usernameList;
			
	}

}
