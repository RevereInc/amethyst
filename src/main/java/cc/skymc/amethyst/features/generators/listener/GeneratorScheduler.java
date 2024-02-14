package cc.skymc.amethyst.features.generators.listener;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.generators.GeneratorType;
import cc.skymc.amethyst.profile.Profile;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GeneratorScheduler {
    private static HashMap<UUID, BukkitTask> generatorTasks = new HashMap<>();

    public static void removeTask(Generator generator) {
        generatorTasks.remove(generator.getUuid()).cancel();
    }

    public static void createTask(Generator generator) {
        if (!generator.getType().equals(GeneratorType.CRYSTAL_GENERATOR)) {
            generatorTasks.put(generator.getUuid(), new BukkitRunnable() {
                @Override
                public void run() {
                    Location location = generator.getLocation().clone();
                    ItemStack item = new ItemStack(generator.getType().dropMaterial);
                    item.setAmount(generator.getAmount());
                    location.getWorld().dropItemNaturally(location.add(0, 0.2, 0), item);
                }
            }.runTaskTimer(Main.getInstance(), 0L, 100L));
        } else {
            generatorTasks.put(generator.getUuid(), new BukkitRunnable() {
                @Override
                public void run() {
                    Profile profile = Main.getInstance().getProfileHandler().getProfile(generator.getOwner());
                    profile.setCrystals(profile.getCrystals() + 5);
                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 100L));
        }
    }
}
