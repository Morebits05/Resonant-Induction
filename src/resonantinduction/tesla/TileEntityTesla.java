/**
 * 
 */
package resonantinduction.tesla;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.BlockFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import resonantinduction.ITesla;
import resonantinduction.ResonantInduction;
import resonantinduction.base.TileEntityBase;
import resonantinduction.base.Vector3;

/**
 * @author Calclavia
 * 
 */
public class TileEntityTesla extends TileEntityBase implements ITesla
{
	private float energy = 0;
	private boolean doTransfer = false;

	@Override
	public void initiate()
	{
		TeslaGrid.instance().register(this);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 2 == 0 && this.getEnergyStored() > 0 && this.doTransfer)
		{
			Set<ITesla> transferTeslaCoils = new HashSet<ITesla>();

			for (ITesla tesla : TeslaGrid.instance().get())
			{
				if (new Vector3((TileEntity) tesla).distance(new Vector3(this)) < this.getRange())
				{
					transferTeslaCoils.add(tesla);
				}
			}

			float transferEnergy = this.getEnergyStored() / transferTeslaCoils.size();

			for (ITesla tesla : transferTeslaCoils)
			{
				tesla.transfer(transferEnergy);
				this.transfer(-transferEnergy);
			}
		}

		TileEntity tileEntity = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);

		if (tileEntity instanceof TileEntityFurnace)
		{
			TileEntityFurnace furnaceTile = (TileEntityFurnace) tileEntity;
			int burnTime = TileEntityFurnace.getItemBurnTime(furnaceTile.getStackInSlot(1));

			if (burnTime > 0)
			{
				boolean doBlockStateUpdate = false;

				if (furnaceTile.furnaceBurnTime == 0)
				{
					furnaceTile.decrStackSize(1, 1);
					furnaceTile.furnaceBurnTime = burnTime;
					doBlockStateUpdate = true;
				}
				else
				{
					furnaceTile.furnaceBurnTime--;
				}

				if (doBlockStateUpdate)
				{
					BlockFurnace.updateFurnaceBlockState(furnaceTile.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}

				this.transfer(ResonantInduction.POWER_PER_COAL / 20);
			}
		}
	}

	@Override
	public void transfer(float transferEnergy)
	{
		System.out.println(transferEnergy);
		this.energy += transferEnergy;
		this.doTransfer = true;
	}

	public float getEnergyStored()
	{
		return this.energy;
	}

	public int getRange()
	{
		return 8;
	}

	@Override
	public void invalidate()
	{
		TeslaGrid.instance().unregister(this);
		super.invalidate();
	}

}