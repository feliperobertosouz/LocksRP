package me.sieg.locksrp.traps;

import java.util.HashMap;
import java.util.Map;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.AlarmTrap;
import me.sieg.locksrp.traps.PoisonTrap;
import me.sieg.locksrp.traps.SpikeTrap;

public enum TrapType {
    ALARM("ALARM", new AlarmTrap(), "Alarme"),
    POISON("POISON", new PoisonTrap(), "Veneno"),
    SPIKE("SPIKE", new SpikeTrap(), "Espinhos"),
    MAGIC_ALARM("MAGIC_ALARM", new MagicAlarmTrap(), "Alarme Mágico");

    public final String value;
    public final Trap trap;
    public final String displayName;

    private static final Map<String, TrapType> trapTypeMap = new HashMap<>();

    static {
        for (TrapType trapType : values()) {
            trapTypeMap.put(trapType.value, trapType);
        }
    }

    private TrapType(String value, Trap trap, String displayName) {
        this.value = value;
        this.trap = trap;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public Trap getTrap() {
        return trap;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Trap getTrapByType(String trapType) {
        return trapTypeMap.get(trapType.toUpperCase()).trap;
    }
}
