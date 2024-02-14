package dev.revere.amethyst.utils.location;

import org.bukkit.Location;
import org.bukkit.World;

public class Region {
  public int x1, x2;
  public int y1, y2;
  public int z1, z2;

  public Region() {
    this.x1 = 0; this.x2 = 0;
    this.z1 = 0; this.z2 = 0;
  }

  public Region(int x1, int z1, int x2, int z2) {
    this.x1 = x1; this.x2 = x2;
    this.z1 = z1; this.z2 = z2;
    this.y1 = 0; this.y2 = 255;
  }

  public Region(int x1, int y1, int z1, int x2, int y2, int z2) {
    this.x1 = x1; this.x2 = x2;
    this.y1 = y1; this.y2 = y2;
    this.z1 = z1; this.z2 = z2;
  }

  public Region(Location p1, Location p2) {
    this.x1 = p1.getBlockX(); this.x2 = p2.getBlockX();
    this.y1 = p1.getBlockY(); this.y2 = p2.getBlockY();
    this.z1 = p1.getBlockZ(); this.z2 = p2.getBlockZ();
  }

  public Location getCenter(World world) {
    double dx = Math.abs(this.x2 - this.x1);
    double dy = Math.abs(this.y2 - this.y1);
    double dz = Math.abs(this.z2 - this.z1);

    return new Location(world,
        this.x1 + dx / 2,
        this.y1 + dy / 2,
        this.z1 + dz / 2
    );
  }
}
