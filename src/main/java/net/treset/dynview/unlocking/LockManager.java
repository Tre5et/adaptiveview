package net.treset.dynview.unlocking;

import net.treset.dynview.config.Config;
import net.treset.dynview.distance.ViewDistanceHandler;

import java.util.ArrayList;
import java.util.List;

public class LockManager {
    private static List<ViewDistanceLocker> unlockers = new ArrayList<>();
    private static int lockedManually = 0;

    private static ViewDistanceLocker currentLocker = null;

    public static ViewDistanceLocker getCurrentLocker() {
        return currentLocker;
    }

    public static int isLockedManually() { return lockedManually; }
    public static void lockManually(int chunks) {
        lockedManually = chunks;
        lock(chunks);
    }

    public static int getNumUnlockers() { return unlockers.size(); }

    public static void addUnlocker(ViewDistanceLocker unlocker) {
        unlockers.add(unlocker);
        updateUnlocker();
    }

    public static void clearUnlockers() {
        unlockers.clear();
    }


    private static final List<ViewDistanceLocker> toRemove = new ArrayList<>();
    public static void finishUnlocker(ViewDistanceLocker unlocker) {
        toRemove.add(unlocker);
    }

    public static void updateUnlocker() {
        if(isLockedManually() != 0) return;

        if(unlockers.size() == 0) {
            clear();
            return;
        }

        int smallestViewDistance = unlockers.get(0).getDistance();
        ViewDistanceLocker newLocker = unlockers.get(0);
        for(ViewDistanceLocker e : unlockers) {
            if(e.getDistance() < smallestViewDistance) {
                smallestViewDistance = e.getDistance();
                newLocker = e;
            }
        }

        currentLocker = newLocker;
        lock(smallestViewDistance);
    }

    public static void lock(int chunks) {
        Config.setLocked(chunks);
        ViewDistanceHandler.setViewDistance(chunks);
    }

    public static void clear() {
        clearUnlockers();

        if(lockedManually > 0) {
            lock(lockedManually);
            currentLocker = null;
        } else unlock();
    }

    public static void unlockManually() {
        lockedManually = 0;
        updateUnlocker();
    }

    public static void unlock() {
        currentLocker = null;

        Config.setLocked(0);
        ViewDistanceHandler.addViewDitance(0);
    }

    public static void onTick() {
        for(ViewDistanceLocker e : unlockers) {
            e.onTick();
        }

        unlockers.removeAll(toRemove);

        updateUnlocker();
    }
}
