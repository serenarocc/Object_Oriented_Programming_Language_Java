package social;

import java.util.Collection;
import java.util.HashSet;

class Group {

	private final String name;
	private final HashSet<String> members = new HashSet<>();

	public Group(String groupName) {
    name = groupName;
	}

	public void addPerson(String codePerson) {
		members.add(codePerson);
	}

	public Collection<String> getMembers() {
		return members;
	}

	public String getName() {
		return name;
	}
}