package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import dto.response.ResponseDto;

public class ClientRecive extends Thread {
	
	private Socket socket;
	private Gson gson;
	
	public ClientRecive(Socket socket) {
		this.socket = socket;
		gson = new Gson();
	}
	
	@Override
	public void run() {
		
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while(true) {
				String responseJson = bufferedReader.readLine();
				responseMapping(responseJson);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void responseMapping(String responseJson) {
		ResponseDto<?> responseDto = gson.fromJson(responseJson, ResponseDto.class);
		System.out.println(responseDto);
		switch (responseDto.getResource()) {
		case "usernameCheckIsBlank": // or와 같이 usernameCheckIsBlank이거나 usernameCheckIsDuplicate일 때 실행됨
		case "usernameCheckIsDuplicate":
			// errorMessage = (String) responseDto.getBody(); // case는 이어지기 때문에 변수 재선언이 불가능함
			JOptionPane.showMessageDialog(null, (String) responseDto.getBody(), "접속오류", JOptionPane.WARNING_MESSAGE);
			break;
			
		case "usernameCheckSuccessfully":
			ClientApplication.getInstance()
							.getMainCard()
							.show(ClientApplication.getInstance().getMainPanel(), "roomListPanel");
			break;
			
		case "refreshRoomList":
			refreshRoomList((List<Map<String, String>>) responseDto.getBody()); // responseDto.getBody() > ArrayList로 들어옴
			break;
			
		case "createRoomSuccessfully":
			ClientApplication.getInstance()
							.getMainCard()
							.show(ClientApplication.getInstance().getMainPanel(), "roomPanel");
			
		case "refreshUsernameList":
			refreshUsernameList((List<String>) responseDto.getBody());
			break;
			
		case "enterRoomSuccessfully":
			ClientApplication.getInstance()
							.getMainCard()
							.show(ClientApplication.getInstance().getMainPanel(), "roomPanel");
			break;
		
		case "reciveMessage":
			ClientApplication.getInstance().getChttingContent().append((String) responseDto.getBody() + "\n");
			break;
			
		case "exitRoom":
			ClientApplication.getInstance().getChttingContent().setText("");
			ClientApplication.getInstance().getMainCard().show(ClientApplication.getInstance().getMainPanel(), "roomListPanel");
			break;
			
		default :
			break;
		}
	}
	
	private void refreshRoomList(List<Map<String, String>> roomList) {
		ClientApplication.getInstance().getRoomNameListModel().clear();
		ClientApplication.getInstance().setRoomInfoList(roomList);
//		ClientApplication.getInstance().getRoomNameListModel().addAll(roomNameList);
		// map이라 하나하나 넣어줘야됨
		for(Map<String, String> roomInfo : roomList) {
			ClientApplication.getInstance().getRoomNameListModel().addElement(roomInfo.get("roomName"));
		}
		
		ClientApplication.getInstance().getRoomList().setSelectedIndex(0);;
	}
	
	private void refreshUsernameList(List<String> usernameList) {
		ClientApplication.getInstance().getUsernameListModel().clear();
		ClientApplication.getInstance().getUsernameListModel().addAll(usernameList);;
		ClientApplication.getInstance().getJoinUserList().setSelectedIndex(0);
	}
}
















