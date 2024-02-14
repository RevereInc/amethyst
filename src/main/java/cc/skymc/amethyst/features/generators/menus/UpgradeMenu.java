package cc.skymc.amethyst.features.generators.menus;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.generators.Generator;
import cc.skymc.amethyst.features.generators.GeneratorHandler;
import cc.skymc.amethyst.features.generators.GeneratorType;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.profile.ProfileHandler;
import cc.skymc.amethyst.utils.chat.Style;
import cc.skymc.amethyst.utils.inventory.SkullCreator;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UpgradeMenu extends Menu {
    private final ProfileHandler profileHandler;
    private final GeneratorHandler generatorHandler;
    private final Generator generator;

    public UpgradeMenu(Player player, Main core, Generator generator) {
        super(player, "Modify Generator", 27);
        this.generator = generator;
        profileHandler = core.getProfileHandler();
        generatorHandler = core.getGeneratorHandler();
    }

    @Override
    public void tick() {
        List<String> addGeneratorLore = Arrays.asList(
                "&8ɢᴇɴᴇʀᴀᴛᴏʀ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to add &e&ogenerators &f&oto the",
                "&f&ostack to reduce space taken by &c&omultiple generators&f&o.",
                "",
                "&aStacked Amount: &f" + generator.getAmount(),
                "",
                "&7&o(( &f&oClick &7&oto upgrade your generator &7&o))"
        );

        this.buttons[11] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy"
                + "5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNhYzdhZWE0MDJ"
                + "jZTYyOWE0ODMzYTQ1MjI4ZGY0NWE1MDYyZDExMmYwZjgyN"
                + "mMzYzM0M2NmMDY4MDkxY2JlYSJ9fX0="))
                .setDisplayName("&a&lᴀᴅᴅ ɢᴇɴᴇʀᴀᴛᴏʀ")
                .setLore(addGeneratorLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);

                    Profile profile = profileHandler.getProfile(event.getWhoClicked().getUniqueId());

                    if (!generator.getType().equals(GeneratorType.CRYSTAL_GENERATOR)) {
                        if (profile.getSlots() == 25) {
                            event.getWhoClicked().sendMessage(Style.translate("&4&l[!] &cYou have reached the &4generator limit &cof 25."));
                            return;
                        }
                    }

                    profile.setSlots(profile.getSlots() + 1);

                    for (ItemStack item : event.getWhoClicked().getInventory()) {
                        if (item == null)
                            continue;

                        if (item.isSimilar(generator.getType().itemStack())) {
                            item.setAmount(item.getAmount() - 1);
                            generator.setAmount(generator.getAmount() + 1);
                            this.updateMenu();
                            return;
                        }
                    }
                });


        List<String> upgradeLore = Arrays.asList(
                "&8ɢᴇɴᴇʀᴀᴛᴏʀ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to upgrade your",
                "&6&ogenerator &f&oto the next tier for a &2&oreduced cost&f&o.",
                "",
                "&7&o(( &f&oClick &7&oto upgrade your generator &7&o))"
        );

        this.buttons[13] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh"
                + "0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhm"
                + "Y2EzNDVlNGZkOWM0MjJiNzNjNGMxYzUwNTZmMzc5Z"
                + "GU5MjUxMGZjOTRiNGNjOTA3ZmIyMGNlNzUwZGM5MCJ9fX0="))
                .setDisplayName("&6&lᴜᴘɢʀᴀᴅᴇ ɢᴇɴᴇʀᴀᴛᴏʀ").setLore(upgradeLore.toArray(new String[0]));


        List<String> removeGeneratorLore = Arrays.asList(
                "&8ɢᴇɴᴇʀᴀᴛᴏʀ ᴏᴘᴛɪᴏɴ",
                "",
                "&f&oThis &e&obutton &f&oallows you to add &e&ogenerators &f&oto the",
                "&f&ostack to reduce space taken by &c&omultiple generators&f&o.",
                "",
                "&cStacked Amount: &f" + generator.getAmount(),
                "",
                "&7&o(( &f&oClick &7&oto upgrade your generator &7&o))"
        );

        this.buttons[15] = new Button(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJ"
                + "lcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYT"
                + "czZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYz"
                + "NiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ=="))
                .setDisplayName("&c&lʀᴇᴍᴏᴠᴇ ɢᴇɴᴇʀᴀᴛᴏʀ")
                .setLore(removeGeneratorLore.toArray(new String[0]))
                .setClickAction(event -> {
                    event.setCancelled(true);

                    if (generator.getAmount() < 0)
                        return;

                    Profile profile = profileHandler.getProfile(event.getWhoClicked().getUniqueId());
                    profile.setSlots(profile.getSlots() - 1);

                    if (generator.amount <= 1) {
                        generatorHandler.remove(generator, true);
                        profile.getGenerators().remove(generator);
                        generator.getLocation().getBlock().setType(Material.AIR);
                        event.getWhoClicked().closeInventory();
                    } else {
                        generator.setAmount(generator.getAmount() - 1);
                        updateMenu();
                    }

                    event.getWhoClicked().getInventory().addItem(generator.getType().itemStack());
                });
    }
}
