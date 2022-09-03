package com.surge.vms.process.integrator;

import java.util.List;

import org.camunda.bpm.engine.rest.dto.identity.GroupDto;

public interface GroupService {

	/* GET Group List */
	List<GroupDto> getGroupList();

	/* POST Group */
	GroupDto addGroup(GroupDto newGroup);

	/* PUT Group */
	GroupDto editGroup(GroupDto editGroup, String id) throws Exception;

	/* DELETE Group */
	void deleteGroup(String id) throws Exception;

	/* Add Member */
	GroupDto addMember(String groupId, String userId);

	/* Delete Member */
	void delMember(String groupId, String userId);

	/* GET Member List By Group */
	List getUserByGroup(String groupId);

	/* GET User List By First Name */
	List getUserByFirstName(String firstName);

	
}
