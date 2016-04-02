package net.torocraft.games.chess;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.games.checkerboard.CheckerBoard;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;

public class ItemChessControlWand extends Item {

	private static final String NBT_XGAME_POS_KEY = "xgamepos";
	private static final String NBT_YGAME_POS_KEY = "ygamepos";
	private static final String NBT_ZGAME_POS_KEY = "zgamepos";

	public static final String NAME = "chess_control_wand";

	public ItemChessControlWand() {
		setUnlocalizedName(NAME);
		setMaxDamage(1);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("Game at: " + getGamePosition(stack));
	};

	@Override
	public boolean canItemEditBlocks() {
		return false;
	};

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		stack.setTagCompound(new NBTTagCompound());
	}

	public void setGame(BlockPos gamePos, ItemStack stack) {
		NBTTagCompound t = stack.getTagCompound();
		
		if(t == null){
			stack.setTagCompound(new NBTTagCompound());
			t = stack.getTagCompound();
		}
		
		if (t != null) {
			t.setInteger(NBT_XGAME_POS_KEY, gamePos.getX());
			t.setInteger(NBT_YGAME_POS_KEY, gamePos.getY());
			t.setInteger(NBT_ZGAME_POS_KEY, gamePos.getZ());
		}
	}

	private BlockPos getGamePosition(ItemStack stack) {

		NBTTagCompound t = stack.getTagCompound();

		if (t == null) {
			return null;
		}

		return new BlockPos(t.getInteger(NBT_XGAME_POS_KEY), t.getInteger(NBT_YGAME_POS_KEY),
				t.getInteger(NBT_ZGAME_POS_KEY));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {

			BlockPos gamePosition = getGamePosition(stack);

			// ChessGame game = getGame(stack, worldIn);

			if (gamePosition == null) {
				System.out.println("onItemUse(): No game found");

			} else if (targetedEntity == null) {
				System.out.println(
						"onItemUse(): no target, clicked on [" + CheckerBoard.getPositionName(gamePosition, pos) + "]");

			} else {

				String chessPosition = CheckerBoard.getPositionName(gamePosition, pos);

				targetedEntity.setChessPosition(chessPosition);

				System.out.println("onItemUse(): Sending target [" + targetedEntity.getClass().getSimpleName()
						+ "] to [" + chessPosition + "] Block Pos[" + pos.toString() + "]");

			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	private EntityChessPiece targetedEntity;

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {

		if (!(entity instanceof EntityChessPiece)) {
			return true;
		}

		if (!entity.worldObj.isRemote) {
			targetedEntity = (EntityChessPiece) entity;
			System.out.println(
					"onLeftClickEntity(): targeted a chess pieced at [" + targetedEntity.getChessPosition() + "]");
			targetedEntity.spawnExplosionParticle();
		}

		return true;
	}

}
