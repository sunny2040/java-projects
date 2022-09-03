package com.surge.vms.controller;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.surge.vms.model.Profile;
import com.surge.vms.process.integrator.GroupService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class GroupController {

	@Autowired
	GroupService groupservice;

	/* Get Group List */
	@GetMapping(value = "/group")
	public List<GroupDto> getGroupList() {

		System.out.println("getGroupList....");
		List<GroupDto> groupDto = groupservice.getGroupList();
		System.out.println("temptaskDtosList...:" + groupDto);
		return groupDto;
	}

	/* Post Group */
	@PostMapping(value = "/group/create")
	public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto newGroup) {
		GroupDto groupObj = groupservice.addGroup(newGroup);
		return new ResponseEntity<GroupDto>(groupObj, HttpStatus.CREATED);
	}

	/* Edit Group */
	@PutMapping(value = "/group/{id}")
	public ResponseEntity<GroupDto> editGroup(@RequestBody GroupDto editGroup,
			@PathVariable String id) throws Exception {
		GroupDto groupDto = groupservice.editGroup(editGroup, id);
		return new ResponseEntity<GroupDto>(groupDto, HttpStatus.OK);
	}

	/* Delete Group */
	@DeleteMapping(value = "/group/{id}")
	public void deleteGroup(@PathVariable String id) throws Exception {
		groupservice.deleteGroup(id);
	}

	/* Add Member */
	@RequestMapping(path = "/group/{groupId}/members/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<List> addMember(
			@PathVariable("groupId") String groupId,
			@PathVariable("userId") String userId) {
		GroupDto groupDto = groupservice.addMember(groupId, userId);
		List userList = groupservice.getUserByGroup(groupDto.getId());
		return new ResponseEntity<List>(userList,HttpStatus.OK);
	}

	/* Delete Member */
	@RequestMapping(path = "/group/{groupId}/members/{userId}", method = RequestMethod.DELETE)
	public void deleteMember(@PathVariable("groupId") String groupId,
			@PathVariable("userId") String userId) {
		groupservice.delMember(groupId, userId);
	}

	/* GET Member List By Group Id */
	@RequestMapping(path = "/group/{groupId}/members", method = RequestMethod.GET)
	public List getUserByGroup(@PathVariable("groupId") String groupId) {
		System.out.println("getUserByGroup....");
		List userList = groupservice.getUserByGroup(groupId);
		System.out.println("temptaskDtosList...:" + userList);
		return userList;
	}

	/* Get User List By First Name */
	@RequestMapping(path = "/user/firstName/{firstName}", method = RequestMethod.GET)
	public List getUserByFirstName(
			@PathVariable("firstName") String firstName) {
		System.out.println("getUserByFirstName....");
		List userList = new ArrayList<>();
		userList = groupservice.getUserByFirstName(firstName);
		return userList;
	}

	/* GET Remain User */
	@RequestMapping(path = "/group/{groupId}/firstName/{firstName}", method = RequestMethod.GET)
	public List remainUser(@PathVariable("groupId") String groupId,
			@PathVariable("firstName") String firstName) {
		ArrayList<Profile> getUserlist = new ArrayList<Profile>(
				groupservice.getUserByFirstName(firstName));
		ArrayList<Profile> getMemberlist = new ArrayList<Profile>(
				groupservice.getUserByGroup(groupId));

		for (int i = 0; i < getUserlist.size(); i++) {
			for (int j = 0; j < getMemberlist.size(); j++) {
				Profile userProfile = getUserlist.get(i);
				Profile memberProfile = getMemberlist.get(j);
				if (userProfile.getFirstName()
						.equalsIgnoreCase(memberProfile.getFirstName())) {
					Profile common = getUserlist.get(i);
					getUserlist.remove(common);
					System.out.println(getUserlist);
				}
			}
		}
		return getUserlist;
	}
}
