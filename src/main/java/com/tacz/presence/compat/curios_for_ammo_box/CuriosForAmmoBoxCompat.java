package com.tacz.presence.compat.curios_for_ammo_box;

import com.nanaios.curios_for_ammo_box.util.InventoryWithCurios;
import net.minecraft.world.entity.player.Inventory;

public class CuriosForAmmoBoxCompat {
    public static Inventory transformToCuriosInventory(Inventory original) {
        return new InventoryWithCurios(original);
    }
}
