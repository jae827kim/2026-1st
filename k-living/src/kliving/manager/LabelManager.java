package kliving.manager;

import kliving.model.User;

/**
 * Assigns human-readable experience labels to users based on their
 * self-reported years of solo-living experience.
 */
public class LabelManager {

    public static String assignLabel(int experienceYear) {
        if (experienceYear == 0) return "뉴비 자취생";
        if (experienceYear == 1) return "초보 자취생";
        if (experienceYear <= 3) return "중급 자취생";
        if (experienceYear <= 5) return "숙련 자취생";
        return "고수 자취생";
    }

    /** Convenience: reads the year from a User object and applies the label in-place. */
    public static void updateLabel(User user) {
        user.setUserLabel(assignLabel(user.getExperienceYear()));
    }
}
