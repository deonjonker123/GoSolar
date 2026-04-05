package com.misterd.gosolar.block.custom;

import com.misterd.gosolar.blockentity.GSBlockEntities;
import com.misterd.gosolar.blockentity.custom.EnergyReceiverBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class EnergyReceiverBlock extends BaseEntityBlock {

    public static final MapCodec<EnergyReceiverBlock> CODEC = simpleCodec(p -> new EnergyReceiverBlock(0, p));
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(6, 0, 6, 10, 0.25, 10),
            Block.box(7, 0.25, 7, 9, 0.45, 9),
            Block.box(7.25, 0.25, 7.25, 8.75, 0.75, 8.75)
    );
    private static final VoxelShape SHAPE_DOWN = Shapes.or(
            Block.box(6, 15.75, 6, 10, 16, 10),
            Block.box(7, 15.55, 7, 9, 15.75, 9),
            Block.box(7.25, 15.25, 7.25, 8.75, 15.75, 8.75)
    );
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(6, 6, 15.75, 10, 10, 16),
            Block.box(7, 7, 15.55, 9, 9, 15.75),
            Block.box(7.25, 7.25, 15.25, 8.75, 8.75, 15.75)
    );
    private static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(6, 6, 0, 10, 10, 0.25),
            Block.box(7, 7, 0.25, 9, 9, 0.45),
            Block.box(7.25, 7.25, 0.25, 8.75, 8.75, 0.75)
    );
    private static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(0, 6, 6, 0.25, 10, 10),
            Block.box(0.25, 7, 7, 0.45, 9, 9),
            Block.box(0.25, 7.25, 7.25, 0.75, 8.75, 8.75)
    );
    private static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(15.75, 6, 6, 16, 10, 10),
            Block.box(15.55, 7, 7, 15.75, 9, 9),
            Block.box(15.25, 7.25, 7.25, 15.75, 8.75, 8.75)
    );

    private final long transferRate;

    public EnergyReceiverBlock(long transferRate, BlockBehaviour.Properties properties) {
        super(properties);
        this.transferRate = transferRate;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public long getTransferRate() { return transferRate; }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP    -> SHAPE_UP;
            case DOWN  -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST  -> SHAPE_EAST;
            case WEST  -> SHAPE_WEST;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof EnergyReceiverBlockEntity be) {
            be.setOwner(player.getUUID());
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyReceiverBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, GSBlockEntities.ENERGY_RECEIVER_BE.get(), EnergyReceiverBlockEntity::tick);
    }
}