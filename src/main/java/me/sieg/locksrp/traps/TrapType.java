package me.sieg.locksrp.traps;

import java.util.HashMap;
import java.util.Map;
import me.sieg.locksrp.traps.Trap;
import me.sieg.locksrp.traps.AlarmTrap;
import me.sieg.locksrp.traps.PoisonTrap;
import me.sieg.locksrp.traps.SpikeTrap;

public enum TrapType {
    ALARM("ALARM", new AlarmTrap(), "Alarme", 9999),
    POISON("POISON", new PoisonTrap(), "Veneno", 9998),
    SPIKE("SPIKE", new SpikeTrap(), "Espinhos", 9997),
    MAGIC_ALARM("MAGIC_ALARM", new MagicAlarmTrap(), "Alarme Mágico", 9996),
    REINFORCEMENT("REINFORCEMENT", new ReinforcementTrap(), "Reforçamento", 9995),
    SPECTRAL("SPECTRAL", new SpectralTrap(), "Espectral", 9994),
    EXPLOSIVE("EXPLOSIVE", new ExplosiveTrap(), "Explosiva", 9993);

    public final String value;
    public final Trap trap;
    public final String displayName;
    public final int customModelData;

    private static final Map<String, TrapType> trapTypeMap = new HashMap<>();

    static {
        for (TrapType trapType : values()) {
            trapTypeMap.put(trapType.value, trapType);
        }
    }

    private TrapType(String value, Trap trap, String displayName, int customModelData) {
        this.value = value;
        this.trap = trap;
        this.displayName = displayName;
        this.customModelData = customModelData;
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

    public static Trap getTrapByType(TrapType trapType) {
        return trapType.getTrap();
    }
}
