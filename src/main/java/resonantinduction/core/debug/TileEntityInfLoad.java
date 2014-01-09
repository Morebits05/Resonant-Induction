package resonantinduction.core.debug;

import resonantinduction.lib.prefab.TileEntityEnergyMachine;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityInfLoad extends TileEntityEnergyMachine
{

	@Override
	public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
	{
		return receive;
	}

	@Override
	public long onExtractEnergy(ForgeDirection from, long request, boolean doExtract)
	{
		return 0;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}

	@Override
	public long getMaxEnergyStored()
	{
		return Long.MAX_VALUE;
	}
}
