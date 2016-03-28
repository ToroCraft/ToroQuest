package net.torocraft.games.chess;

import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;

public class ItemChessControlWand extends Item {

	public static String NAME = "chess_control_wand";

	private BlockPos chessControlBlockPostion;
	private ChessGame game;

	public ItemChessControlWand() {
		setUnlocalizedName(NAME);
		setMaxDamage(1);
	}

	public void setChessControlBlockPosition(BlockPos pos) {
		this.chessControlBlockPostion = pos;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			ChessGame game = getGame(worldIn);

			if (game == null) {
				System.out.println("No world");

			} else if (targetedEntity == null) {
				System.out.println("no target, clicked on [" + game.getPositionName(pos) + "]");

			} else {

				String chessPosition = game.getPositionName(pos);

				targetedEntity.setChessPosition(chessPosition);

				System.out.println("Sending target to [" + chessPosition + "] Block Pos[" + pos.toString() + "]");

			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	private EntityChessPiece targetedEntity;

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		
		
		
		if (!(entity instanceof EntityChessPiece)) {
			return false;
		}

		ChessGame game = getGame(entity.worldObj);
		if (game == null) {
			return true;
		}

		if (entity.worldObj.isRemote) {
			targetedEntity = (EntityChessPiece) entity;
			System.out.println("targeted a chess pieced at [" + targetedEntity.getChessPosition() + "]");
		}else{
			targetedEntity.spawnExplosionParticle();
		}
		

		return true;
	}

	private ChessGame getGame(World world) {
		if (game == null) {
			try {
				game = ((BlockChessControl) world.getBlockState(chessControlBlockPostion).getBlock()).getGame();
			} catch (Exception e) {
				System.out.println("game block missing: " + e.getMessage());
			}
		}

		return game;
	}

}
