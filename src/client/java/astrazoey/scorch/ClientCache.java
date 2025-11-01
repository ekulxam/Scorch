package astrazoey.scorch;

public class ClientCache {
    static int clientHealthPenalty = 0;
    static int clientCurseAmount = 0;

    public static void setHealthPenalty(int healthPenalty) {
        clientHealthPenalty = healthPenalty;
    }

    public static int getHealthPenalty() {
        return clientHealthPenalty;
    }

    public static void setCurseActive(int curseAmount) {
        clientCurseAmount = curseAmount;
    }

    public static int getCurseActive() {
        return clientCurseAmount;
    }

}
