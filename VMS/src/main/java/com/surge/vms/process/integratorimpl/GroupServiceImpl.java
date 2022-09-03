package com.surge.vms.process.integratorimpl;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.surge.vms.model.Profile;
import com.surge.vms.process.integrator.GroupService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	@Value("${camunda.api.basepath}")
	private String restApiBasePath;

	private RestTemplate restTemplate;

	public GroupServiceImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	/* GET Group List Start */
	public List<GroupDto> getGroupList() {
		System.out.println(
				this.getClass().getSimpleName() + " - Get GroupList Triggered");
		String url = restApiBasePath + "group";

		ResponseEntity<GroupDto[]> response = restTemplate.getForEntity(url,
				GroupDto[].class);
		GroupDto[] groups = response.getBody();
		List<GroupDto> groupList = new ArrayList<GroupDto>();

		for (GroupDto groupDto : groups) {
			System.out.println("Group Id : " + groupDto.getId());
			System.out.println("Group Name : " + groupDto.getName());
			System.out.println("Group Type : " + groupDto.getType());
			groupList.add(groupDto);
		}
		return groupList;
	}
	/* GET Group List End */

	/* POST a Group Start */
	public GroupDto addGroup(GroupDto newGroup) {
		System.out.println(this.getClass().getSimpleName() + " - Add Group Triggered");
		String url = restApiBasePath + "group/create";
		GroupDto groupDto = new GroupDto();
		try {
			groupDto = restTemplate.postForObject(url, newGroup,
					GroupDto.class);
			System.out.println("Group Saved to Camunda " + groupDto);
			System.out.println("Group Created....");
		} catch (RestClientException e) {
			throw new RestClientException("Connection Failed");
		}
		return groupDto;
	}
	/* POST a Group End */

	/* PUT a Group Start */
	public GroupDto editGroup(GroupDto editGroup, String id) throws Exception {
		System.out.println(this.getClass().getSimpleName()
				+ " - Edit Group Details Triggered");
		String url = restApiBasePath + "group/" + id;
		GroupDto groupDto = restTemplate.getForObject(url, GroupDto.class);
		if (groupDto == null) {
			throw new Exception("Could not find group with id- " + id);
		}
		if (editGroup.getName() == null || editGroup.getName().isEmpty()) {
			editGroup.setName(groupDto.getName());
		}
		if (editGroup.getType() == null || editGroup.getType().isEmpty()) {
			editGroup.setType(groupDto.getType());
		}
		editGroup.setId(id);

		restTemplate.put(url, editGroup, editGroup.getId());
		return editGroup;
	}
	/* PUT a Group End */

	/* DELETE a Group Start */
	public void deleteGroup(String id) throws Exception {
		System.out.println(this.getClass().getSimpleName() + " - Delete Group Triggered");
		String url = restApiBasePath + "group/" + id;
		GroupDto groupDto = restTemplate.getForObject(url, GroupDto.class);
		if (groupDto == null) {
			throw new Exception("Could not find Group with Id " + id);
		}
		restTemplate.delete(url, groupDto.getId());
	}
	/* DELETE a Group End */

	/* Add a Group Member Start */
	public GroupDto addMember(String groupId, String userId) {
		System.out.println(this.getClass().getSimpleName()
				+ " - Add Member to Group Triggered");
		String restGroupurl = restApiBasePath + "group/" + groupId;
		GroupDto groupDto = restTemplate.getForObject(restGroupurl,
				GroupDto.class);

		String addMemurl = restApiBasePath + "group/" + groupId + "/members/"
				+ userId;
		if (groupDto != null && userId != null) {
			restTemplate.put(addMemurl, GroupDto.class);
		}
		return groupDto;
	}
	/* Add a Group Member End */

	/* Delete a Group Member Start */
	public void delMember(String groupId, String userId) {
		System.out.println(this.getClass().getSimpleName()
				+ " - Delete Member from Group Triggered");
		String restGroupurl = restApiBasePath + "group/" + groupId;
		GroupDto groupDto = restTemplate.getForObject(restGroupurl,
				GroupDto.class);
		String delMemurl = restApiBasePath + "group/" + groupId + "/members/"
				+ userId;
		if (groupDto != null && userId != null) {
			restTemplate.delete(delMemurl);
		}
	}
	/* Delete a Group Member End */

	/* GET Member List By Group Start */
	public List getUserByGroup(String groupId) {
		System.out.println(this.getClass().getSimpleName()
				+ " - GET User List By Group Triggered");
		String url = restApiBasePath + "user?memberOfGroup=" + groupId;
		System.out.println(url);

		ResponseEntity<Profile[]> response = restTemplate.getForEntity(url,
				Profile[].class);
		Profile[] profiles = response.getBody();
		List<Profile> userList = new ArrayList<Profile>();

		for (Profile profile : profiles) {

			userList.add(profile);
		}
		return userList;
	}
	/* GET Member List By Group End */

	/* GET User By First Name Start */
	public List getUserByFirstName(String firstName) {
		System.out.println(this.getClass().getSimpleName()
				+ " - GET User List By First Name Triggered");
		String url = restApiBasePath + "user?firstNameLike=" + firstName + "%";
		System.out.println(url);

		ResponseEntity<Profile[]> response = restTemplate.getForEntity(url,
				Profile[].class);
		Profile[] profiles = response.getBody();
		List<Profile> userList = new ArrayList<Profile>();
		for (Profile profile : profiles) {
			userList.add(profile);
		}
		return userList;

	}
	/* GET User By First Name End */

	
}
