package social;

import java.util.Collection;
import java.util.HashSet;

class Person {
	private final String name;
	private final String surname;
	private final String code;
	private final HashSet<String> friends = new HashSet<>();
	private final HashSet<String> groups = new HashSet<>();

	public Person(String code2, String name2, String surname2) {
		code = code2;
		name = name2;
		surname = surname2;
	}

	public void addFriend(String code) {
		friends.add(code);
	}

	public String getCode() {
		return code;
	}

	public String toString() {
		return code + " " + name + " " + surname;
	}

	public Collection<String> getFriends() {
		return friends;
	}

	public void addGroup(String groupName) {
		groups.add(groupName);
	}

	public Collection<String> getGroups() {
		return groups;
	}
}
