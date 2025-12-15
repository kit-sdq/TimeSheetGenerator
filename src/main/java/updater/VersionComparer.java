/* Licensed under MIT 2025. */
package updater;

import java.util.Arrays;
import java.util.List;

public final class VersionComparer {

	private VersionComparer() {
		// Don't allow instances of this class
	}

	/**
	 * Takes in two version strings of the format \d+(\.\d+)*, so for example
	 * 0.20.6, and returns if the first version parameter is newer than the second
	 * (other) parameter. If both are equal, the method returns false.
	 * <p>
	 * Important: Both versions must have the same number of levels. Comparing 0.12
	 * and 2.1.2 will yield false no matter the contents.
	 * </p>
	 * 
	 * @param version The version
	 * @param other   The version to compare it to.
	 */
	public static boolean isNewerThan(String version, String other) {
		List<Integer> splitVersion = Arrays.stream(version.split("\\.")).map(Integer::parseInt).toList();
		List<Integer> splitOther = Arrays.stream(other.split("\\.")).map(Integer::parseInt).toList();
		if (splitVersion.size() != splitOther.size())
			return false;

		for (int i = 0; i < splitVersion.size(); i++) {
			if (splitVersion.get(i) > splitOther.get(i))
				return true;
			if (splitVersion.get(i) < splitOther.get(i))
				return false;
		}

		return false;
	}

}
