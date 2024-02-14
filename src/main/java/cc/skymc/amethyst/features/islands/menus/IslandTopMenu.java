package cc.skymc.amethyst.features.islands.menus;

import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.features.islands.Island;
import cc.skymc.amethyst.features.islands.IslandHandler;
import cc.skymc.amethyst.utils.inventory.SkullCreator;
import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IslandTopMenu extends Menu {
    IslandHandler islandHandler;

    public IslandTopMenu(Player player, String name, Main core) {
        super(player, name, 54);
        islandHandler = core.getIslandHandler();
    }

    @Override
    public void tick() {
        int count = 0;
        ArrayList<Island> islands = new ArrayList<>(islandHandler.getIslands().values());

        islands.sort(Comparator.comparingLong(Island::getCrystals));
        Collections.reverse(islands);

        for (Island island : islands) {
            if (count == 16)
                return;


            List<String> lores = new ArrayList<>();

            lores.add("&8ᴘʟᴀʏᴇʀ ᴄʟᴏᴜᴅ");
            lores.add("");
            lores.add("&a&lCrystals: &f" + island.getCrystals());
            lores.add("");
            lores.add("&9&lMembers");
            for (UUID uuid : island.getMembers()) {
                lores.add(" &b• &f" + Bukkit.getOfflinePlayer(uuid).getName());
            }

            int buttonPlacer = getButtonPlacer(count);

            this.buttons[buttonPlacer] = new Button(SkullCreator.itemFromUuid(island.getLeader())).setDisplayName("&9&lCloud: &b&l" + Bukkit
                    .getOfflinePlayer(island.getLeader()).getName()).setLore(lores.toArray(new String[0]));

            count++;
        }

        ArrayList<Integer> filled = new ArrayList<>(Arrays.asList(12, 13, 14, 19, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33));

        for (int i : filled) {
            if (getPlayer().getOpenInventory().getItem(i).getType() == Material.AIR) {
                this.buttons[i] = new Button(SkullCreator.itemFromBase64
                        ("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZX"
                                + "h0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE"
                                + "3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlN"
                                + "TUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19")).setDisplayName("&f&l???");
            }
        }
    }

    private int getButtonPlacer(int count) {
        int buttonPlacer = 0;

        switch (count) {
            case 0 -> buttonPlacer = 12;
            case 1 -> buttonPlacer = 13;
            case 2 -> buttonPlacer = 14;
            case 3 -> buttonPlacer = 19;
            case 4 -> buttonPlacer = 20;
            case 5 -> buttonPlacer = 21;
            case 6 -> buttonPlacer = 22;
            case 7 -> buttonPlacer = 23;
            case 8 -> buttonPlacer = 24;
            case 9 -> buttonPlacer = 25;
            case 10 -> buttonPlacer = 29;
            case 11 -> buttonPlacer = 30;
            case 12 -> buttonPlacer = 31;
            case 13 -> buttonPlacer = 32;
            case 14 -> buttonPlacer = 33;
        }
        return buttonPlacer;
    }
}
