package diet;

import java.util.*;

import diet.Order.PaymentMethod;

/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {

	private final Food food;
	private final Collection<Customer> customers = new ArrayList<>();
	private final Map<String, Restaurant> restaurants = new HashMap<>();

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	public Takeaway(Food food){
		this.food = food;
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */
	public Restaurant addRestaurant(String restaurantName) {
		Restaurant r = new Restaurant(restaurantName, food);
		restaurants.put(r.getName(), r);
		return r;
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		return new LinkedList<>(restaurants.keySet());
	}

	/**
	 * Creates a new customer for the takeaway
	 * @param firstName first name of the customer
	 * @param lastName	last name of the customer
	 * @param email		email of the customer
	 * @param phoneNumber mobile phone number
	 *
	 * @return the object representing the newly created customer
	 */
	public Customer registerCustomer(String firstName, String lastName, String email, String phoneNumber) {
		Customer u = new Customer(firstName, lastName, email, phoneNumber);
		customers.add(u);
		return u;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers(){
		ArrayList<Customer> u = new ArrayList<>(customers);
		Collections.sort(u);
		return u;
	}


	/**
	 * Creates a new order for the chain.
	 *
	 * @param customer		 customer issuing the order
	 * @param restaurantName name of the restaurant that will take the order
	 * @param time	time of desired delivery
	 * @return order object
	 */
	public Order createOrder(Customer customer, String restaurantName, String time) {
		Restaurant restaurant = restaurants.get(restaurantName);
		
		Order o = new Order(restaurant, customer, time);
		restaurant.addOrder(o);
		customer.addOrder(o);

		return o;
	}

	/**
	 * Find all restaurants that are open at a given time.
	 *
	 * @param time the time with format {@code "HH:MM"}
	 * @return the sorted collection of restaurants
	 */
	public Collection<Restaurant> openRestaurants(String time){
		TreeSet<Restaurant> opened_r = new TreeSet<>();
		Time lt = new Time(time);
		for( Restaurant r : restaurants.values() ) {
			if (r.isOpenAt(lt)) {
				opened_r.add(r);
			}
		}
		return opened_r;
	}
}
