package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {
	private final String name;
	private final List<AltitudeRange> altitudeRanges;
	private final Collection<Municipality> municipalities;
	private final Collection<MountainHut> mountainHuts;

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name = name;
		altitudeRanges = new LinkedList<>();
		municipalities = new LinkedList<>();
		mountainHuts = new LinkedList<>();
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		for (String range : ranges) {
			altitudeRanges.add(new AltitudeRange(range));
		}
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		return altitudeRanges.stream()
				.filter( r -> r.contains(altitude) )
				.findFirst()
				.orElse(AltitudeRange.DEFAULT).toString();
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return Collections.unmodifiableCollection(municipalities);
		// a little bit of defensive programming.
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return Collections.unmodifiableCollection(mountainHuts);
		// a little bit of defensive programming.
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		return municipalities.stream()
				.filter( m -> m.getName().equals(name)).findFirst().orElseGet(() -> {
			Municipality m = new Municipality(name, province, altitude);
			municipalities.add(m);
			return m;
		});
		// an alternative would be using a map to store municipalities by name 
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,
			Municipality municipality) {
		return createOrGetMountainHut(name, null, category, bedsNumber, municipality);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,
			Municipality municipality) {
		return mountainHuts.stream().filter( x -> x.getName().equals(name)).findFirst().orElseGet(() -> {
			MountainHut h = new MountainHut(name, altitude, category, bedsNumber, municipality);
			mountainHuts.add(h);
			return h;
		});
		// an alternative would be using a map to store municipalities by name 
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Region r = new Region(name);
		List<String> lines = Region.readData(file);
		if(lines==null) return null;

		// Extract headers and build the relative map
		String[] headers = lines.remove(0).split(";");
		Map<String, Integer> h2i = new HashMap<>();
		for (int i = 0; i < headers.length; ++i) {
			h2i.put(headers[i], i);
		}
		// Iterate on data rows
		lines.forEach(row -> {
			String[] cells = row.split(";");

			String municipalityName = cells[h2i.get("Municipality")];
			String municipalityProvince = cells[h2i.get("Province")];
			Integer municipalityAltitude = Integer.parseInt(cells[h2i.get("MunicipalityAltitude")]);
			Municipality municipality = r.createOrGetMunicipality(municipalityName, municipalityProvince,
					municipalityAltitude);

			String hutName = cells[h2i.get("Name")];
			String altitude = cells[h2i.get("Altitude")];
			String category = cells[h2i.get("Category")];
			Integer bedsNumber = Integer.parseInt(cells[h2i.get("BedsNumber")]);

			if (altitude.equals("")) {
				r.createOrGetMountainHut(hutName, category, bedsNumber, municipality);
			} else {
				r.createOrGetMountainHut(hutName, Integer.parseInt(altitude), category, bedsNumber, municipality);
			}
		});

		return r;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		return municipalities.stream()
				.collect(groupingBy(Municipality::getProvince, // key mapper
								    counting()));			   // downstream
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		return mountainHuts.stream()
				.collect(groupingBy(x -> x.getMunicipality().getProvince(), // key mapper
									groupingBy(								// downstream
											   x -> x.getMunicipality().getName(), // key mapper
											   counting())));					   // downstream
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		Map<String,Long> res = mountainHuts.stream()
				.map(this::getAltitudeRange)
				.collect(groupingBy(r->r, 			// key mapper
						            counting()));	// downstream
		// adds also altitude ranges with no mountain huts
		altitudeRanges.stream().map(AltitudeRange::toString).forEach(r -> res.putIfAbsent(r, 0L));
		return res;
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		return mountainHuts.stream()
				.collect(groupingBy(x -> x.getMunicipality().getProvince(),
								    summingInt(MountainHut::getBedsNumber)));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		Map<String, Optional<Integer>> res= mountainHuts.stream()
				.collect(groupingBy(this::getAltitudeRange,
									mapping(MountainHut::getBedsNumber, 
											maxBy(Comparator.naturalOrder()))));
		// adds also altitude ranges with no mountain huts
		altitudeRanges.stream().map(AltitudeRange::toString).forEach(r -> res.putIfAbsent(r, Optional.of(0)));
		return res;
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		return mountainHuts.stream().map(x -> x.getMunicipality().getName())
				.collect(groupingBy(x -> x, TreeMap::new, counting())).entrySet().stream()
				.collect(groupingBy(Map.Entry::getValue,
						mapping(Map.Entry::getKey, toList())));
	}

	private String getAltitudeRange(MountainHut x) {
		if (x.getAltitude().isPresent()) {
			return getAltitudeRange(x.getAltitude().get());
		} else {
			return getAltitudeRange(x.getMunicipality().getAltitude());
		}
	}

}
