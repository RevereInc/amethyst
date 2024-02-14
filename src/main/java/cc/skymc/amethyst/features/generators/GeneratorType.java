package cc.skymc.amethyst.features.generators;

import cc.skymc.amethyst.utils.inventory.ItemBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum GeneratorType {

    COBBLESTONE("&7Cobblestone Generator", Material.COBBLESTONE, Material.COBBLESTONE),
    STONE("&8Stone Generator", Material.STONE, Material.STONE),
    STONEBRICK("&eStonebrick Generator", Material.STONE_BRICKS, Material.STONE_BRICKS),
    COAL("&8Coal Generator", Material.COAL, Material.COAL_BLOCK),
    IRON("&7Iron Generator", Material.IRON_INGOT, Material.IRON_BLOCK),
    GOLD("&6Gold Generator", Material.GOLD_INGOT, Material.GOLD_BLOCK),
    DIAMOND("&bDiamond Generator", Material.DIAMOND, Material.DIAMOND_BLOCK),
    EMERALD("&aEmerald Generator", Material.EMERALD, Material.EMERALD_BLOCK),
    AMETHYST("&dAmethyst Generator", Material.AMETHYST_SHARD, Material.AMETHYST_BLOCK),
    PRISMARINE("&3Prismarine Generator", Material.PRISMARINE_SHARD, Material.PRISMARINE),
    TUBE_CORAL("&9Tuble Coral Generator", Material.TUBE_CORAL, Material.TUBE_CORAL_BLOCK),
    FIRE_CORAL("&cFire Coral Generator", Material.FIRE_CORAL, Material.FIRE_CORAL_BLOCK),
    BUBBLE_CORAL("&5Bubble Coral Generator", Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_BLOCK),
    NETHER_WART("&4Nether Wart Generator", Material.NETHER_WART, Material.NETHER_WART_BLOCK),
    NETHERITE("&8Netherite Generator", Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK),
    NETHER_STAR("&fNether Star Generator", Material.NETHER_STAR, Material.BEACON),
    CRYSTAL_GENERATOR("&2&lCrystal Generator", Material.EMERALD, Material.EMERALD_BLOCK);

    public String display;
    public Material dropMaterial;
    public Material blockMaterial;

    public List<String> getLores() {
        return Arrays.asList("", "&7This " + display + " &7will allow you to earn",
                "&7items which can be sold in the &2/shop.",
                "", "&7&o(( &f&oPlace &7&othis generator ))");
    }

    public static Optional<GeneratorType> get(ItemStack item) {
        for (GeneratorType type : GeneratorType.values()) {
            ItemStack generator = new ItemBuilder(type.blockMaterial).name(type.display).lore(type.getLores()).build();
            if (item.isSimilar(generator)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public ItemStack itemStack() {
        return new ItemBuilder(this.blockMaterial)
                .name(this.display)
                .lore(this.getLores())
                .build();
    }
}
