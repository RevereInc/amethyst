package cc.skymc.amethyst.utils.location;

import lombok.Getter;

import java.util.Optional;

@Getter
public class Grid {

  private final int size;
  private final int spacing;

  private Region filledRegion;
  private FillingRegion fillingRegion;

  public Grid(int size, int spacing) {
    this.size = size;
    this.spacing = spacing;
    this.filledRegion = new Region();
    this.fillingRegion = new FillingRegion(new Region(
        0,
        0,
        size + spacing,
        size + spacing
    ), Direction.Across);
  }

  public Grid(int size, int spacing, Region filled, Region filling, Direction direction) {
    this.size = size;
    this.spacing = spacing;
    this.filledRegion = filled;
    this.fillingRegion = new FillingRegion(filling, direction);
  }

  public Optional<Region> nextRegion() {
    Optional<Region> nextRegion = this.fillingRegion.nextRegion();
    if(nextRegion.isPresent())
      return nextRegion;

    /* expand the filled region to include what we have filled */
    this.filledRegion = new Region(
        this.filledRegion.x1,
        this.filledRegion.z1,
        this.fillingRegion.region.x2,
        this.fillingRegion.region.z2
    );

    System.out.println("grid: filled "
        + this.filledRegion.x1 + " "
        + this.filledRegion.z1 + " "
        + this.filledRegion.x2 + " "
        + this.filledRegion.z2);

    /* find a new filling region */
    Region region;
    Direction direction;
    switch(this.fillingRegion.direction) {
      case Across:
        /* we were going across, the new filling region is a column */
        direction = Direction.Down;
        region = new Region(
            this.filledRegion.x2,
            this.filledRegion.z1,
            this.filledRegion.x2 + size + spacing,
            this.filledRegion.z2
        );

        System.out.println("grid: next (across) "
            + region.x1 + " "
            + region.z1 + " "
            + region.x2 + " "
            + region.z2);
        break;

      case Down:
        /* we were going down, the new filling region is a row */
        direction = Direction.Across;
        region = new Region(
            this.filledRegion.x1,
            this.filledRegion.z2,
            this.filledRegion.x2,
            this.filledRegion.z2 + size + spacing
        );

        System.out.println("grid: next (down) "
            + region.x1 + " "
            + region.z1 + " "
            + region.x2 + " "
            + region.z2);
        break;

      default:
        return Optional.empty();
    }

    this.fillingRegion = new FillingRegion(region, direction);
    return this.fillingRegion.nextRegion();
  }

  public enum Direction {
    Across,
    Down,
  }

  public class FillingRegion {
    public Region region;
    public Direction direction;

    public FillingRegion(Region region, Direction direction) {
      this.region = region;
      this.direction = direction;
    }

    private int currentPos() {
      if(this.direction == Direction.Across)
        return this.region.x1;
      else
        return this.region.z1;
    }

    private int endPos() {
      if(this.direction == Direction.Across)
        return this.region.x2;
      else
        return this.region.z2;
    }

    private void nextPos() {
      if(this.direction == Direction.Across)
        this.region.x1 += size + spacing;
      else
        this.region.z1 += size + spacing;

      System.out.println("grid: fill: next " + this.region.x1 + " " + this.region.z1);
    }

    public Optional<Region> nextRegion() {
      if(this.currentPos() + size + spacing > this.endPos()) {
        System.out.println("grid: fill: region was full");
        return Optional.empty();
      }

      Region region = new Region(
          this.region.x1,
          this.region.z1,
          this.region.x1 + size,
          this.region.z1 + size
      );

      System.out.println("grid: fill: new region "
      + this.region.x1 + " "
      + this.region.z1 + " "
      + this.region.x2 + " "
      + this.region.z2);

      this.nextPos();
      return Optional.of(region);
    }
  }
}