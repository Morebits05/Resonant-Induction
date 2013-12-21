package resonantinduction.multimeter;

import java.util.HashMap;

import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import universalelectricity.api.electricity.ElectricalEvent.EnergyUpdateEvent;
import universalelectricity.api.energy.IEnergyNetwork;

/**
 * @author Calclavia
 * 
 */
public class MultimeterEventHandler
{
	private static final HashMap<IEnergyNetwork, Long> networkEnergyCache = new HashMap<IEnergyNetwork, Long>();
	private static long lastCheckTime = 0;

	public static HashMap<IEnergyNetwork, Long> getCache(World worldObj)
	{
		HashMap<IEnergyNetwork, Long> returnCache = new HashMap<IEnergyNetwork, Long>(networkEnergyCache);

		if (Math.abs(worldObj.getWorldTime() - lastCheckTime) >= 40)
		{
			lastCheckTime = worldObj.getWorldTime();
			networkEnergyCache.clear();
		}

		return returnCache;
	}

	@ForgeSubscribe
	public void event(EnergyUpdateEvent evt)
	{
		IEnergyNetwork network = evt.network;

		if (network.getDistribution(null) != 0)
		{
			networkEnergyCache.put(network, network.getDistribution(null));
		}
	}
}
