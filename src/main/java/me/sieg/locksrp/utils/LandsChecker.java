package me.sieg.locksrp.utils;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.enums.FlagTarget;
import me.angeschossen.lands.api.flags.enums.RoleFlagCategory;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.flags.type.RoleFlag;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.role.Role;
import me.sieg.locksrp.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;


public class LandsChecker {


    public static boolean PlayerCanBreakLand(Player player) {
        if(!Main.getLandsPluginPresent()){
            return true;
        }else{
            LandsIntegration api = LandsIntegration.of(Main.getPlugin());
            // Obtém a localização atual do jogador
            Location playerLocation = player.getLocation();

            Area area = api.getArea(playerLocation);
            if (area != null) {

                Role role = area.getRole(player.getUniqueId());

                LandPlayer landPlayer = api.getLandPlayer(player.getUniqueId());


                if (role.hasFlag(Flags.BLOCK_BREAK)) {
                    return true;
                } else {
                    return false;
                }
            }else if(area == null){
                return true;
            }
            return false;
        }
        }

        public static boolean PlayerCanPlaceLand(Player player) {
            if(!Main.getLandsPluginPresent()){
                return true;
            }else{
                LandsIntegration api = LandsIntegration.of(Main.getPlugin());
                Location playerLocation = player.getLocation();

                Area area = api.getArea(playerLocation);
                if (area != null) {

                    Role role = area.getRole(player.getUniqueId());

                    LandPlayer landPlayer = api.getLandPlayer(player.getUniqueId());


                    if (role.hasFlag(Flags.BLOCK_PLACE)) {
                        return true;
                    } else {
                        return false;
                    }
                }else if(area == null){
                    return true;
                }
                return false;

            }
        }

}
