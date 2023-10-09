package social;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Social {
	private final HashMap<String, Person> people = new HashMap<>();
	private final HashMap<String, Group> groups = new HashMap<>();

	/**
	 * Creates a new account for a person
	 * 
	 * @param code	nickname of the account
	 * @param name	first name
	 * @param surname last name
	 * @throws PersonExistsException in case of duplicate code
	 */
	public void addPerson(String code, String name, String surname)
			throws PersonExistsException {
		Person p = new Person(code, name, surname);
		if (people.containsKey(code))
			throw new PersonExistsException();
		people.put(code, p);
	}

	/**
	 * Retrieves information about the person given their account code.
	 * The info consists in name and surname of the person, in order, separated by blanks.
	 * 
	 * @param code account code
	 * @return the information of the person
	 * @throws NoSuchCodeException
	 */
	public String getPerson(String code) throws NoSuchCodeException {
		Person p = people.get(code);
		if (p == null)
			throw new NoSuchCodeException();
		return p.toString();
	}

	/**
	 * Define a friendship relationship between to persons given their codes.
	 * 
	 * Friendship is bidirectional: if person A is friend of a person B, that means that person B is a friend of a person A.
	 * 
	 * @param codePerson1	first person code
	 * @param codePerson2	second person code
	 * @throws NoSuchCodeException in case either code does not exist
	 */
	public void addFriendship(String codePerson1, String codePerson2)
			throws NoSuchCodeException {
		Person p1 = people.get(codePerson1);
		Person p2 = people.get(codePerson2);
		if (p1 == null || p2 == null)
			throw new NoSuchCodeException();
		p1.addFriend(codePerson2);
		p2.addFriend(codePerson1);
	}

	/**
	 * Retrieve the collection of their friends given the code of a person.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return the list of person codes
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> listOfFriends(String codePerson)
			throws NoSuchCodeException {
		if (people.containsKey(codePerson)) {
			if (people.get(codePerson).getFriends().size() == 0)
				return new LinkedList<>();
			else
				return people.get(codePerson).getFriends();
		} else
			throw new NoSuchCodeException();
	}

	private final static Collection<String> emptyList=new LinkedList<>();
	/**
	 * Retrieves the collection of the code of the friends of the friends
	 * of the person whose code is given, i.e. friends of the second level.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return collections of codes of second level friends
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> friendsOfFriends(String codePerson)
			throws NoSuchCodeException {
		Person p1 = people.get(codePerson);
		if (p1 == null)
			throw new NoSuchCodeException();

		return p1.getFriends().stream()
				.map(people::get)
				.map(Person::getFriends)
				.flatMap(Collection::stream)
				.filter(p -> !p.equals(codePerson))
				.collect(Collectors.toList())
				;
	}

	/**
	 * Retrieves the collection of the code of the friends of the friends
	 * of the person whose code is given, i.e. friends of the second level.
	 * The result has no duplicates.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return collections of codes of second level friends
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> friendsOfFriendsNoRepetition(String codePerson)
			throws NoSuchCodeException {
		Person p1 = people.get(codePerson);
		if (p1 == null)
			throw new NoSuchCodeException();

		return p1.getFriends().stream()
				.map(people::get)
				.map(Person::getFriends)
				.flatMap(Collection::stream)
				.filter(p -> !p.equals(codePerson))
				.collect(Collectors.toSet())
				;
	}

	/**
	 * Creates a new group with the given name
	 * 
	 * @param groupName name of the group
	 */
	public void addGroup(String groupName) {
		groups.put(groupName, new Group(groupName));
	}

	/**
	 * Retrieves the list of groups.
	 * 
	 * @return the collection of group names
	 */
	public Collection<String> listOfGroups() {
		if (groups.size() == 0)
			return emptyList;
		return groups.keySet();
	}

	/**
	 * Add a person to a group
	 * 
	 * @param codePerson person code
	 * @param groupName  name of the group
	 * @throws NoSuchCodeException in case the code or group name do not exist
	 */
	public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
		Group g = groups.get(groupName);
		Person p = people.get(codePerson);
		if (p == null || g == null){
			throw new NoSuchCodeException();
		}
		g.addPerson(codePerson);
		p.addGroup(groupName);
	}

	/**
	 * Retrieves the list of people on a group
	 * 
	 * @param groupName name of the group
	 * @return collection of person codes
	 */
	public Collection<String> listOfPeopleInGroup(String groupName) {
		Group g = groups.get(groupName);
		if(g==null) return null;
		return g.getMembers();
	}

	/**
	 * Retrieves the code of the person having the largest
	 * group of friends
	 * 
	 * @return the code of the person
	 */
	public String personWithLargestNumberOfFriends() {
		return
		people.values().stream()
				.max(Comparator.comparing( p -> p.getFriends().size() ))
				.map(Person::getCode).orElse("<none>");
	}

	private interface ExcFun<T,U,E extends Exception> { U apply(T t) throws E; }
	private static <T,U> Function<T,U> hideException(ExcFun<T,U,?> f){
		return x -> {
			try {
				return f.apply(x);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Find the code of the person with largest number
	 * of second level friends
	 * 
	 * @return the code of the person
	 */
	public String personWithMostFriendsOfFriends() {
		return people.values().stream()
				.max(Comparator.comparing(
						hideException( p -> friendsOfFriends(p.getCode()).size() )
				)).map(Person::getCode).orElse("<none>");
	}

	/**
	 * Find the name of group with the largest number of members
	 * 
	 * @return the name of the group
	 */
	public String largestGroup() {
		return groups.values().stream()
				.max(Comparator.comparing( g -> g.getMembers().size()))
				.map(Group::getName).orElse("<none>");
	}

	/**
	 * Find the code of the person that is member of
	 * the largest number of groups
	 * 
	 * @return the code of the person
	 */
	public String personInLargestNumberOfGroups() {
		return people.values().stream()
				.max(Comparator.comparing(
						hideException(p -> p.getGroups().size() )
				)).map(Person::getCode).orElse("<none>")
		;
	}

}