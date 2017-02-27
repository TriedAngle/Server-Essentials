package wurmcraft.serveressentials.common.api.storage;

import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.RankManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to hold the data about a player
 */
public class PlayerData {

    private String rank;
    private int max_homes = 4;
    private long teleport_timer;
    private long lastseen;
    private Home[] homes = new Home[max_homes];
    private List<Mail> currentMail = new ArrayList<>();

    public PlayerData(IRank group) {
        this.rank = group.getName();
    }

    public Home[] getHomes() {
        return homes;
    }

    public Home getHome(String name) {
        if (homes != null)
            for (Home h : homes)
                if (h != null && h.getName().equalsIgnoreCase(name))
                    return h;
        return null;
    }

    public void setHomes(Home[] homes) {
        if (homes != null && homes.length > 0)
            this.homes = homes;
    }

    public String addHome(Home home) {
        if (home != null && homes != null && home.getName() != null) {
            boolean temp = false;
            for (int index = 0; index < homes.length; index++) {
                if (homes[index] != null && homes[index].getName().equalsIgnoreCase(home.getName())) {
                    homes[index] = home;
                    return Local.HOME_REPLACED.replaceAll("#", home.getName());
                } else if (homes[index] == null) {
                    homes[index] = home;
                    return Local.HOME_SET.replaceAll("#", home.getName());
                }
            }
            return Local.HOME_MAX.replaceAll("#", Integer.toString(max_homes));
        }
        return Local.HOME_INVALID;
    }

    public String delHome(String name) {
        if (name != null && homes != null) {
            for (int index = 0; index < homes.length; index++) {
                if (homes[index] != null && homes[index].getName().equalsIgnoreCase(name)) {
                    homes[index] = null;
                    return Local.HOME_DELETED.replaceAll("#", name);
                }
            }
        }
        return Local.HOME_ERROR_DELETION.replaceAll("#", name);
    }

    public long getTeleport_timer() {
        return teleport_timer;
    }

    public void setTeleport_timer(long time) {
        teleport_timer = time;
    }

    public List<Mail> getMail() {
        return currentMail;
    }

    public void addMail(Mail mail) {
        currentMail.add(mail);
    }

    public long getLastseen() {
        return lastseen;
    }

    public void setLastseen(long lastseen) {
        this.lastseen = lastseen;
    }

    public IRank getRank() {
        IRank group = RankManager.getRankFromName(rank);
        if (group != null)
            return group;
        setRank(RankManager.getDefaultRank());
        return RankManager.getDefaultRank();
    }

    public void setRank(String rank) {
        IRank group = RankManager.getRankFromName(rank);
        if (group != null)
            setRank(group);
        else
            setRank(RankManager.getDefaultRank());
    }

    public void setRank(IRank rank) {
        if (RankManager.getRanks().contains(rank))
            this.rank = rank.getName();
        else
            this.rank = RankManager.getDefaultRank().getName();
    }
}
