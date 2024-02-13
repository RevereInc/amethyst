package cc.skymc.amethyst.features.pets;

import cc.skymc.amethyst.Locale;
import cc.skymc.amethyst.Main;
import cc.skymc.amethyst.profile.Profile;
import cc.skymc.amethyst.profile.ProfileHandler;
import cc.skymc.amethyst.utils.chat.Style;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import cc.skymc.amethyst.utils.inventory.SkullCreator;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class PetHandler implements Listener {

  private final ProfileHandler profileHandler;
  public static HashMap<UUID, Pet> petMap = new HashMap<>();
  @Getter private final HashMap<UUID, BukkitTask> activeTasks = new HashMap<>();
  @Getter private static final HashMap<UUID, ArmorStand> activeStands = new HashMap<>();

  public PetHandler(Main core) {
    this.profileHandler = core.getProfileHandler();
    core.getServer().getPluginManager().registerEvents(this, core);
  }

  @EventHandler
  public void onClick(PlayerInteractEvent event) {
    Action action = event.getAction();
    ItemStack item = event.getItem();

    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      if(item == null)
        return;

      Optional<Pet> pet = Pet.get(item);
      if(pet.isEmpty())
        return;

      Player player = event.getPlayer();
      UUID uuid = player.getUniqueId();

      Profile profile = profileHandler.getProfile(uuid);

      /* enable the pet */
      if(petMap.get(uuid) == null) {
        /* apply pet buffs */
        switch (pet.get()) {
          case RABBIT -> player.setWalkSpeed(0.35F);
          case GOAT -> profile.setXpMultiplier(profile.getXpMultiplier() + 0.25);
        }


        ArmorStand stand = player.getWorld().spawn(player.getLocation().clone().add(1, 1.0, 1), ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomName(Style.translate(pet.get().display));
        stand.setCustomNameVisible(true);
        stand.setHelmet(SkullCreator.itemFromBase64(pet.get().base64));

        activeStands.put(uuid, stand);
        activeTasks.put(uuid, new PetRunnable(player, stand).runTaskTimer(Main.getInstance(), 0L, 1L));
        petMap.put(uuid, pet.get());

        player.sendMessage(Locale.PET_ENABLE.getColoredString().replaceAll("<name>", Style.translate(pet.get().display)));
      /* disable the pet */
      } else {
        if(!pet.get().equals(petMap.get(uuid))) {
          player.sendMessage(Locale.PET_NOT_ACTIVE.getColoredString());
          return;
        }

        /* remove pet buffs */
        switch (pet.get()) {
          case RABBIT -> player.setWalkSpeed(0.2F);
          case GOAT -> profile.setXpMultiplier(profile.getXpMultiplier() - 0.25);
        }

        /* there has to be a nicer way of doing this other than having another map */
        activeStands.get(uuid).remove();
        activeTasks.get(uuid).cancel();

        petMap.remove(uuid);

        player.sendMessage(Locale.PET_DISABLE.getColoredString().replaceAll("<name>", Style.translate(pet.get().display)));
      }
    }
  }

  //fine.
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Optional<Pet> pet = Pet.get(event.getPlayer().getItemInHand());
    if (pet.isEmpty())
      return;

    event.setCancelled(true);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    System.out.println("player quit event infact gets called on server shutdown?");

    Profile profile = profileHandler.getProfile(uuid);

    Optional<Pet> pet = getPet(uuid);
    if(pet.isEmpty()) {
      System.out.println("did not have an enabled pet;");
      return;
    }

    switch(pet.get()) {
      case RABBIT -> player.setWalkSpeed(0.2F);
      /*
         may be confusing. if the server is shut down whilst a pet is enabled
         the pet effect saves to the profile before being removed.
       */
      case GOAT -> profile.setXpMultiplier(profile.getXpMultiplier() - 0.25);
    }

    /* there has to be a nicer way of doing this other than having another map */
    activeStands.get(uuid).remove();
    activeTasks.get(uuid).cancel();

    petMap.remove(uuid);
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    Profile profile = profileHandler.getProfile(uuid);

    Optional<Pet> pet = getPet(uuid);
    if(pet.isEmpty())
      return;

    switch(pet.get()) {
      case RABBIT -> player.setWalkSpeed(0.2F);
      case GOAT -> profile.setXpMultiplier(profile.getXpMultiplier() - 0.25);
    }

    /* there has to be a nicer way of doing this other than having another map */
    activeStands.get(uuid).remove();
    activeTasks.get(uuid).cancel();

    petMap.remove(uuid);
  }

  public static Optional<Pet> getPet(UUID uuid) {
    return Optional.ofNullable(petMap.get(uuid));
  }
}
