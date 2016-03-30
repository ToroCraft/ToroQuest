package net.torocraft.games;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.games.checkerboard.CheckboardCreateCommand;
import net.torocraft.games.chess.BlockChessControl;
import net.torocraft.games.chess.BlockChessControl.TileEntityChessControl;
import net.torocraft.games.chess.ChessCreateCommand;
import net.torocraft.games.chess.ItemChessControlWand;
import net.torocraft.games.chess.pieces.enities.EntityBishop;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;
import net.torocraft.games.chess.pieces.enities.EntityKing;
import net.torocraft.games.chess.pieces.enities.EntityKnight;
import net.torocraft.games.chess.pieces.enities.EntityPawn;
import net.torocraft.games.chess.pieces.enities.EntityQueen;
import net.torocraft.games.chess.pieces.enities.EntityRook;
import net.torocraft.games.chess.pieces.render.RenderBishop;
import net.torocraft.games.chess.pieces.render.RenderKing;
import net.torocraft.games.chess.pieces.render.RenderKnight;
import net.torocraft.games.chess.pieces.render.RenderPawn;
import net.torocraft.games.chess.pieces.render.RenderQueen;
import net.torocraft.games.chess.pieces.render.RenderRook;
import net.torocraft.toroutils.generation.CuboidCommand;
import net.torocraft.toroutils.generation.ToroGenCommand;

@Mod(modid = ToroGamesMod.MODID, name = ToroGamesMod.MODNAME, version = ToroGamesMod.VERSION)
public class ToroGamesMod {

	public static final String MODID = "torogamesmod";
	public static final String MODNAME = "ToroGames Mod";
	public static final String VERSION = "0.0.0";

	@Mod.Instance(MODID)
	public static ToroGamesMod instance;

	public static Block chessControlBlock;
	public static Item chessControlWand;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		chessControlBlock = new BlockChessControl();
		chessControlWand = new ItemChessControlWand();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		registerEntities();
		registerEntityRenders();
		registerChessControlWand(event);
		registerChessControlBlock();
	}

	private void registerEntities() {
		int id = 1;
		registerEntity(EntityRook.class, "Rook", id++);
		registerEntity(EntityBishop.class, "Bishop", id++);
		registerEntity(EntityKnight.class, "Knight", id++);
		registerEntity(EntityPawn.class, "Pawn", id++);
		registerEntity(EntityQueen.class, "Queen", id++);
		registerEntity(EntityKing.class, "King", id++);
	}

	private void registerEntity(Class<? extends EntityChessPiece> entity, String name, int id) {
		EntityRegistry.registerModEntity(entity, name, id, ToroGamesMod.instance, 40, 2, true);
	}

	@SideOnly(Side.CLIENT)
	private void registerEntityRenders() {
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		registerRender(EntityKing.class, new RenderKing(rm));
		registerRender(EntityKnight.class, new RenderKnight(rm));
		registerRender(EntityPawn.class, new RenderPawn(rm));
		registerRender(EntityQueen.class, new RenderQueen(rm));
		registerRender(EntityRook.class, new RenderRook(rm));
		registerRender(EntityBishop.class, new RenderBishop(rm));
	}

	private void registerRender(Class<? extends Entity> e, Render<? extends Entity> renderer) {
		RenderingRegistry.registerEntityRenderingHandler(e, renderer);
	}

	private void registerChessControlBlock() {
		GameRegistry.registerBlock(chessControlBlock, BlockChessControl.NAME);
		GameRegistry.registerTileEntity(TileEntityChessControl.class, TileEntityChessControl.NAME);

		Item controlBlockItem = GameRegistry.findItem(MODID, BlockChessControl.NAME);

		ModelResourceLocation model = new ModelResourceLocation(MODID + ":" + BlockChessControl.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(controlBlockItem, 0, model);
	}

	private void registerChessControlWand(FMLInitializationEvent event) {

		GameRegistry.registerItem(chessControlWand, ItemChessControlWand.NAME);

		ModelResourceLocation model = new ModelResourceLocation(MODID + ":" + ItemChessControlWand.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chessControlWand, 0, model);

		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.getItemModelMesher().register(chessControlWand, 0,
					new ModelResourceLocation(MODID + ":" + ItemChessControlWand.NAME, "inventory"));
		}
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		ICommandManager comManager = event.getServer().getCommandManager();
		((ServerCommandManager) comManager).registerCommand(new CheckboardCreateCommand());
		((ServerCommandManager) comManager).registerCommand(new ChessCreateCommand());
		((ServerCommandManager) comManager).registerCommand(new CuboidCommand());
		((ServerCommandManager) comManager).registerCommand(new ToroGenCommand());
	}

}
