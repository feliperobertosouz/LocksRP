package me.sieg.locksrp.utils;

import me.sieg.locksrp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class SoundPlayer {

    public BukkitTask playSoundRepeatedly(Location loc, Sound sound, int repetitions, long delayTicks, float volume, float pitch) {
        AtomicInteger counter = new AtomicInteger(0);

        class SoundTask extends BukkitRunnable {
            @Override
            public void run() {
                if (counter.getAndIncrement() < repetitions) {
                    loc.getWorld().playSound(loc, sound, volume, pitch);
                } else {
                    // Se o número de repetições for atingido, cancelar a tarefa
                    cancel();
                }
            }
        }

        SoundTask soundTask = new SoundTask();
        return soundTask.runTaskTimer(Main.getPlugin(), 0L, delayTicks);
    }
}
