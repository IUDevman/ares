package dev.tigr.ares.fabric.impl.modules.combat;

import dev.tigr.ares.core.feature.module.Category;
import dev.tigr.ares.core.feature.module.Module;
import dev.tigr.ares.core.setting.Setting;
import dev.tigr.ares.core.setting.settings.BooleanSetting;
import dev.tigr.ares.fabric.utils.HoleType;
import dev.tigr.ares.fabric.utils.InventoryUtils;
import dev.tigr.ares.fabric.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

/**
 * @author Tigermouthbear
 */
@Module.Info(name = "AntiBedAura", description = "automatically places string in hitbox when in hole to prevent beds", category = Category.COMBAT)
public class AntiBedAura extends Module {
    private final Setting<Boolean> rotate = register(new BooleanSetting("Rotate", true));
    // TODO: remove this or make it work better
    @Override
    public void onTick() {
        if(WorldUtils.isHole(MC.player.getBlockPos()) != HoleType.NONE && MC.world.getBlockState(MC.player.getBlockPos().up()).getBlock() != Block.getBlockFromItem(Items.STRING)) {
            int prev = MC.player.inventory.selectedSlot;
            if(MC.player.inventory.getMainHandStack().getItem() != Items.STRING) {
                int slot = InventoryUtils.findItemInHotbar(Items.STRING);
                if(slot != -1) {
                    MC.player.inventory.selectedSlot = slot;
                    MC.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket());
                }
            }
            WorldUtils.placeBlockMainHand(MC.player.getBlockPos().up(), rotate.getValue());
            MC.player.inventory.selectedSlot = prev;
        }
    }
}
