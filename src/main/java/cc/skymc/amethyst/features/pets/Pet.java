package cc.skymc.amethyst.features.pets;

import cc.skymc.amethyst.utils.inventory.ItemBuilder;
import cc.skymc.amethyst.utils.inventory.SkullCreator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public enum Pet {

    RABBIT("&3&lRabbit Pet",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc2NTZmZmEzYjczOWY3OTA0YWEzNjI1YTEwYTE2NzY1NWY3MTNiODY2MWE1YWUxYWJkZjU1NTEwNjBmZWJlNCJ9fX0=",
            Material.PLAYER_HEAD,
            Arrays.asList("&8Clickable Pet", "", "&7ᴛʜɪs &fʀᴀʙʙɪᴛ ᴘᴇᴛ &7ɢɪᴠᴇs ʏᴏᴜ ᴄᴇʀᴛᴀɪɴ ʙᴜꜰꜰs sᴜᴄʜ",
                    "&7ᴀs &fsᴘᴇᴇᴅ &7ᴀɴᴅ &fᴊᴜᴍᴘ ʙᴏᴏsᴛ &7ᴛᴏ ʜᴇʟᴘ ʏᴏᴜ ᴇxᴘʟᴏʀᴇ.",
                    "", "&7&o(( &f&oClick &7&oto activate this pet ))")
    ),

    VILLAGER("&2&lVillager Pet",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RlMjE4MWU1ZWNkOWUzYzAzODMwMzkzODVlMmQyYjlkNTFkZTIxMmNiZTc4YWYyM2UwYTVhYjU0NjYyOWYxNiJ9fX0=",
            Material.PLAYER_HEAD,
            Arrays.asList("&8Clickable Pet", "", "&7ᴛʜɪs &fᴠɪʟʟᴀɢᴇʀ ᴘᴇᴛ &7ɢɪᴠᴇs ʏᴏᴜ ᴄᴇʀᴛᴀɪɴ ʙᴜꜰꜰs sᴜᴄʜ as",
                    "&fᴍᴏɴᴇʏ &7ʙᴏᴏsᴛᴇʀs &7ᴛᴏ ʜᴇʟᴘ ʏᴏᴜ ᴜᴘɢʀᴀᴅᴇ ʏᴏᴜʀ ɪᴛᴇᴍs.",
                    "", "&7&o(( &f&oClick &7&oto activate this pet ))")
    ),

    GOAT("&a&lGoat Pet",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5ZjhlMzMyNDk2OTk5MzdjNDA3YjFiMjJmYWJkN2JjOGQyODI1YjdhZGJiZTc4ZDFhNzAyOTY5YTcxM2Y0MiJ9fX0=",
            Material.PLAYER_HEAD,
            Arrays.asList("&8Clickable Pet", "", "&7ᴛʜɪs &fɢᴏᴀᴛ ᴘᴇᴛ &7ɢɪᴠᴇs ʏᴏᴜ ᴄᴇʀᴛᴀɪɴ ʙᴜꜰꜰs sᴜᴄʜ as",
                    "&fᴇxᴘᴇʀɪᴇɴᴄᴇ &7ʙᴏᴏsᴛᴇʀs &7ᴛᴏ ʜᴇʟᴘ ʏᴏᴜ ʟᴇᴠᴇʟ ᴜᴘ.",
                    "", "&7&o(( &f&oClick &7&oto activate this pet ))")
    );

    public String display;
    public String base64;
    public Material material;
    public List<String> lores;

    public static Optional<Pet> get(ItemStack item) {
        for (Pet type : Pet.values()) {
            ItemStack pet = SkullCreator.itemWithBase64(new ItemBuilder(type.material)
                    .name(type.display)
                    .lore(type.lores)
                    .build(), type.base64);

            if (item.isSimilar(pet)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public ItemStack itemStack() {
        return SkullCreator.itemWithBase64(new ItemBuilder(this.material)
                .name(this.display)
                .lore(this.lores)
                .build(), this.base64);
    }
}

class PetRunnable extends BukkitRunnable {
    private final Player target;
    private final ArmorStand stand;

    public PetRunnable(Player target, ArmorStand stand) {
        this.target = target;
        this.stand = stand;
    }

    @Override
    public void run() {
        stand.teleport(target.getLocation().clone().add(1, 1.0, 1));
    }
}

