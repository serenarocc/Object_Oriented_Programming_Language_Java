package diet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant implements Comparable<Restaurant> {
	
	private final Food food;
	private final String name;
    private final ArrayList<WorkingHours> working_hours; //08:30-14:00  19:00-00:00
	private final Map<String, Menu> menus;
	private final List<Order> orders = new LinkedList<>();

	/**
	 * Creates a new restaurant
	 * @param name	name of the restaurant
	 * @param food	reference Food object for ingredients
	 */
	 Restaurant(String name, Food food) {
		this.name = name;
		this.food = food;
		working_hours = new ArrayList<>();
		menus = new HashMap<>();
	}

	/**
	 * retrieves the name of the restaurant.
	 *
	 * @return name of the restaurant
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define opening times.
	 * Accepts an array of strings (even number of elements) in the format {@code "HH:MM"},
	 * so that the closing hours follow the opening hours
	 * (e.g., for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00,
	 * arguments would be {@code "08:15", "14:00", "19:00", "00:00"}).
	 *
	 * @param hm sequence of opening and closing times
	 */
	public void setHours(String ... hm) {
		working_hours.clear();
		for(int i=0; i<hm.length/2; i++) {
			working_hours.add(new WorkingHours(hm[2*i], hm[2*i+1]));
		}
	}

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
	 */
	public boolean isOpenAt(String time){
		return isOpenAt(new Time(time));
	}

	boolean isOpenAt(Time t) {
		for(WorkingHours w : working_hours) {
			if( w.includes(t) ) return true;
		}
		return false;
	}

	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	public void addMenu(Menu menu) {
		menus.put(menu.getName(), menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		return menus.get(name);
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public String ordersWithStatus(OrderStatus status) {
		StringBuilder b = new StringBuilder();
		orders.sort(Comparator.comparing((Order o)->o.getRestaurant().getName())
				.thenComparing(Order::getUser)
				.thenComparing(Order::getDeliveryTime));
		for (Order o: orders) {
			if (o.getStatus() == status){
				b.append(o);
			}
		}
		return b.toString();
	}

	/**
	 * Adds a new order for the restaurant
	 *
	 * @param order the order
	 */
	void addOrder(Order order) {
		orders.add(order);
	}

	Time checkTime(Time t) {
		Collections.sort(working_hours);
		for(WorkingHours w : working_hours) {
			if( w.includes(t) ) return t;
		}
		for(WorkingHours w : working_hours) {
			if (w.getOpen().compareTo(t) > 0) {
				return w.getOpen();
			}
		}
		return working_hours.get(0).getOpen();
	}

	@Override
	public int compareTo(Restaurant o) {
		return this.getName().compareTo(o.getName());
	}

}
