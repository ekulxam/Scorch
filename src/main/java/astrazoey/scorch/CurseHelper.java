package astrazoey.scorch;

public class CurseHelper {
    public static int getPenalty(int curseExposure, float maxHp) {
        float healthPenalty = 0;

        // just in case other mods increase max hp
        float penaltyFactor = maxHp / 20;

        if (curseExposure > 120) {
            healthPenalty = 9 * penaltyFactor;
        } else if (curseExposure > 100) {
            healthPenalty = 8 * penaltyFactor;
        } else if (curseExposure > 80) {
            healthPenalty = 7 * penaltyFactor;
        } else if (curseExposure > 60) {
            healthPenalty = 6 * penaltyFactor;
        } else if (curseExposure > 40) {
            healthPenalty = 5 * penaltyFactor;
        } else if (curseExposure > 30) {
            healthPenalty = 4 * penaltyFactor;
        } else if (curseExposure > 20) {
            healthPenalty = 3 * penaltyFactor;
        } else if (curseExposure > 10) {
            healthPenalty = 2 * penaltyFactor;
        } else if (curseExposure > 5) {
            healthPenalty = 1 * penaltyFactor;
        }

        // health bar stuff gets sussy after 3 lines of hearts
        if (healthPenalty > 29) {
            healthPenalty = 29;
        }

        return (int) healthPenalty;
    }
}
